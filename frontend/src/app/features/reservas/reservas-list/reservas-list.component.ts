import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { ReservaService } from '../../../core/services/reserva.service';
import { AuthService } from '../../../core/services/auth.service';
import { Reserva, EstadoReserva } from '../../../shared/models/reserva.model';

@Component({
  selector: 'app-reservas-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatSelectModule
  ],
  templateUrl: './reservas-list.component.html',
  styleUrl: './reservas-list.component.scss'
})
export class ReservasListComponent implements OnInit {

  displayedColumns: string[] = ['obra', 'fechaFuncion', 'sala', 'cantidad', 'total', 'estado', 'acciones'];
  dataSource = new MatTableDataSource<Reserva>();
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  loading = false;
  filtroEstado: EstadoReserva | '' = '';

  estados: { valor: EstadoReserva | '', label: string }[] = [
    { valor: '', label: 'Todos' },
    { valor: 'PENDIENTE', label: 'Pendiente' },
    { valor: 'CONFIRMADA', label: 'Confirmada' },
    { valor: 'RECHAZADA', label: 'Rechazada' },
    { valor: 'CANCELADA', label: 'Cancelada' }
  ];

  constructor(
    private reservaService: ReservaService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.loading = true;

    const servicio = this.isAdmin && this.filtroEstado
      ? this.reservaService.listarPorEstado(this.filtroEstado, this.currentPage, this.pageSize)
      : this.isAdmin
        ? this.reservaService.listarPorEstado('PENDIENTE', this.currentPage, this.pageSize)
        : this.reservaService.misReservas(this.currentPage, this.pageSize);

    servicio.subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar las reservas', 'Cerrar', { duration: 3000 });
      }
    });
  }

  onFiltroChange(): void {
    this.currentPage = 0;
    this.cargarReservas();
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.cargarReservas();
  }

  confirmar(reserva: Reserva): void {
    this.reservaService.confirmar(reserva.id).subscribe({
      next: () => {
        this.snackBar.open('Reserva confirmada', 'Cerrar', { duration: 3000 });
        this.cargarReservas();
      },
      error: (error) => {
        const mensaje = error.error?.mensaje || 'Error al confirmar la reserva';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      }
    });
  }

  rechazar(reserva: Reserva): void {
    this.reservaService.rechazar(reserva.id).subscribe({
      next: () => {
        this.snackBar.open('Reserva rechazada', 'Cerrar', { duration: 3000 });
        this.cargarReservas();
      },
      error: (error) => {
        const mensaje = error.error?.mensaje || 'Error al rechazar la reserva';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      }
    });
  }

  cancelar(reserva: Reserva): void {
    if (!confirm('¿Estás seguro de cancelar esta reserva?')) return;
    this.reservaService.cancelar(reserva.id).subscribe({
      next: () => {
        this.snackBar.open('Reserva cancelada', 'Cerrar', { duration: 3000 });
        this.cargarReservas();
      },
      error: (error) => {
        const mensaje = error.error?.mensaje || 'Error al cancelar la reserva';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      }
    });
  }

  getEstadoClass(estado: EstadoReserva): string {
    const clases: Record<EstadoReserva, string> = {
      'PENDIENTE': 'chip-pendiente',
      'CONFIRMADA': 'chip-confirmada',
      'RECHAZADA': 'chip-rechazada',
      'CANCELADA': 'chip-cancelada'
    };
    return clases[estado];
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }
}
