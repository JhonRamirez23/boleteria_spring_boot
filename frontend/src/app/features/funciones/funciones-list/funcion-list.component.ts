import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { FormsModule } from '@angular/forms';
import { FuncionService } from '../../../core/services/funcion.service';
import { ObraService } from '../../../core/services/obra.service';
import { AuthService } from '../../../core/services/auth.service';
import { Funcion } from '../../../shared/models/funcion.model';
import { Obra } from '../../../shared/models/obra.model';

@Component({
  selector: 'app-funciones-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatSelectModule,
    MatFormFieldModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatChipsModule
  ],
  templateUrl: './funcion-list.component.html',
  styleUrl: './funcion-list.component.scss'
})
export class FuncionesListComponent implements OnInit {
  displayedColumns: string[] = ['obra', 'fechaHora', 'sala', 'aforo', 'precio', 'acciones'];
  dataSource = new MatTableDataSource<Funcion>();
  obras: Obra[] = [];
  obraSeleccionada: number | null = null;
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  loading = false;

  constructor(
    private funcionService: FuncionService,
    private obraService: ObraService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  ngOnInit(): void {
    this.cargarObras();
  }

  cargarObras(): void {
    const servicio = this.isAdmin
      ? this.obraService.listarAdmin(0, 100)
      : this.obraService.listar(0, 100);

    servicio.subscribe({
      next: (response) => {
        this.obras = response.content;
        if (this.obras.length > 0) {
          this.obraSeleccionada = this.obras[0].id;
          this.cargarFunciones();
        }
      },
      error: (error) => {
        console.error('Error al cargar obras:', error);
      }
    });
  }

  cargarFunciones(): void {
    if (!this.obraSeleccionada) return;
    this.loading = true;
    const soloFuturas = !this.isAdmin;

    this.funcionService.listarPorObra(
      this.obraSeleccionada, this.currentPage, this.pageSize, soloFuturas
    ).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar las funciones', 'Cerrar', { duration: 3000 });
      }
    });
  }

  onObraChange(): void {
    this.currentPage = 0;
    this.cargarFunciones();
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.cargarFunciones();
  }

  eliminar(funcion: Funcion): void {
    if (!confirm(`¿Eliminar la función del ${funcion.fechaHora}?`)) return;

    this.funcionService.eliminar(funcion.id).subscribe({
      next: () => {
        this.snackBar.open('Función eliminada correctamente', 'Cerrar', { duration: 3000 });
        this.cargarFunciones();
      },
      error: (error) => {
        let mensaje = 'Error al eliminar la función';
        if (error.error?.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.error?.detalles?.length > 0) {
          mensaje = error.error.detalles.join(', ');
        }
        this.snackBar.open(mensaje, 'Cerrar', { duration: 5000 });
      }
    });
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }
}
