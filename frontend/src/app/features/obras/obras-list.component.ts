import { AuthService } from './../../core/services/auth.service';
import { ObraService } from './../../core/services/obra.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Obra } from '../../shared/models/obra.model';

@Component({
  selector: 'app-obras-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatChipsModule,
    MatTooltipModule,
    MatDialogModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './obras-list.component.html',
  styleUrl: './obras-list.component.scss'
})
export class ObrasListComponent implements OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  displayedColumns: string[] = ['titulo', 'genero', 'duracionMinutos', 'activa', 'acciones'];
  dataSource = new MatTableDataSource<Obra>();
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  loading = false;
  busqueda = '';

  constructor(
    private ObraService: ObraService,
    private AuthService: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  get isAdmin(): boolean {
    return this.AuthService.isAdmin();
  }

  ngOnInit(): void {
    this.cargarObras();
  }

  cargarObras(): void {
    this.loading = true;
    const servicio = this.isAdmin
      ? this.ObraService.listarAdmin(this.currentPage, this.pageSize)
      : this.ObraService.listar(this.currentPage, this.pageSize, this.busqueda || undefined);

    servicio.subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar las obras', 'Cerrar', { duration: 3000 });
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.cargarObras();
  }

  onBuscar(): void {
    this.currentPage = 0;
    this.cargarObras();
  }

  eliminar(obra: Obra): void {
    if (!confirm(`¿Estás seguro de eliminar "${obra.titulo}"?`)) return;

    this.ObraService.eliminar(obra.id).subscribe({
      next: () => {
        this.snackBar.open('Obra eliminada correctamente', 'Cerrar', { duration: 3000 });
        this.cargarObras();
      },
      error: (error) => {
        const mensaje = error.error?.mensaje || 'Error al eliminar la obra';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      }
    });
  }
}
