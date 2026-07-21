import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FuncionService } from '../../../core/services/funcion.service';
import { ObraService } from '../../../core/services/obra.service';
import { Obra } from '../../../shared/models/obra.model';

@Component({
  selector: 'app-funcion-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],
  templateUrl: './funcion-form.component.html',
  styleUrl: './funcion-form.component.scss'
})
export class FuncionFormComponent implements OnInit {

  form: FormGroup;
  loading = false;
  guardando = false;
  funcionId: number | null = null;
  obras: Obra[] = [];

  get esEdicion(): boolean {
    return this.funcionId !== null;
  }

  constructor(
    private fb: FormBuilder,
    private funcionService: FuncionService,
    private obraService: ObraService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      obraId: [null, Validators.required],
      fechaHora: ['', Validators.required],
      sala: ['', [Validators.required, Validators.maxLength(100)]],
      aforoTotal: [null, [Validators.required, Validators.min(1)]],
      precio: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.cargarObras();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.funcionId = +id;
      this.cargarFuncion();
    }
  }

  cargarObras(): void {
    this.obraService.listarAdmin(0, 100).subscribe({
      next: (response) => this.obras = response.content
    });
  }

  cargarFuncion(): void {
    this.loading = true;
    this.funcionService.obtenerPorId(this.funcionId!).subscribe({
      next: (funcion) => {
        this.form.patchValue({
          obraId: funcion.obraId,
          fechaHora: this.formatearFechaParaInput(funcion.fechaHora),
          sala: funcion.sala,
          aforoTotal: funcion.aforoTotal,
          precio: funcion.precio
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar la función', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/funciones']);
      }
    });
  }

  formatearFechaParaInput(fecha: string): string {
    // Convierte "dd-MM-yyyy HH:mm:ss" a "yyyy-MM-ddTHH:mm"
    const partes = fecha.split(' ');
    const [dia, mes, anio] = partes[0].split('-');
    const hora = partes[1].substring(0, 5);
    return `${anio}-${mes}-${dia}T${hora}`;
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.guardando = true;
    const formValue = this.form.value;

    const request = {
      ...formValue,
      fechaHora: formValue.fechaHora.replace('T', ' ') + ':00'
    };

    const operacion = this.esEdicion
      ? this.funcionService.actualizar(this.funcionId!, request)
      : this.funcionService.crear(request);

    operacion.subscribe({
      next: () => {
        this.guardando = false;
        this.snackBar.open(
          this.esEdicion ? 'Función actualizada correctamente' : 'Función creada correctamente',
          'Cerrar',
          { duration: 3000 }
        );
        this.router.navigate(['/funciones']);
      },
      error: (error) => {
        this.guardando = false;
        let mensaje = 'Error al guardar la función';

        if (error.error?.detalles && error.error.detalles.length > 0) {
          mensaje = error.error.detalles.join(', ');
        } else if (error.error?.mensaje) {
          mensaje = error.error.mensaje;
        }

        this.snackBar.open(mensaje, 'Cerrar', { duration: 5000 });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/funciones']);
  }
}
