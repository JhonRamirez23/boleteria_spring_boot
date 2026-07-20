import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ReservaService } from '../../../core/services/reserva.service';
import { Funcion } from '../../../shared/models/funcion.model';

@Component({
  selector: 'app-reserva-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './reserva-dialog.component.html',
  styleUrl: './reserva-dialog.component.scss'
})
export class ReservaDialogComponent implements OnInit {

  form: FormGroup;
  guardando = false;

  constructor(
    private fb: FormBuilder,
    private reservaService: ReservaService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<ReservaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public funcion: Funcion
  ) {
    this.form = this.fb.group({
      cantidad: [1, [
        Validators.required,
        Validators.min(1),
        Validators.max(funcion.aforoDisponible)
      ]]
    });
  }

  ngOnInit(): void {}

  get totalEstimado(): number {
    const cantidad = this.form.get('cantidad')?.value || 0;
    return cantidad * this.funcion.precio;
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.guardando = true;
    this.reservaService.crear({
      funcionId: this.funcion.id,
      cantidad: this.form.get('cantidad')!.value
    }).subscribe({
      next: (reserva) => {
        this.guardando = false;
        this.dialogRef.close(true);
        this.snackBar.open(
          `Reserva creada exitosamente. Total: ${this.formatearPrecio(reserva.total)}`,
          'Cerrar',
          { duration: 5000 }
        );
      },
      error: (error) => {
        this.guardando = false;
        let mensaje = 'Error al crear la reserva';
        if (error.error?.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.error?.detalles?.length > 0) {
          mensaje = error.error.detalles.join(', ');
        }
        this.snackBar.open(mensaje, 'Cerrar', { duration: 5000 });
      }
    });
  }

  cancelar(): void {
    this.dialogRef.close(false);
  }
}
