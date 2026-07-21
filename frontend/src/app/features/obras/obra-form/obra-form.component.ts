import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ObraService } from '../../../core/services/obra.service';

@Component({
  selector: 'app-obra-form',
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
    MatSlideToggleModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './obra-form.component.html',
  styleUrl: './obra-form.component.scss'
})
export class ObraFormComponent implements OnInit {

  form: FormGroup;
  loading = false;
  guardando = false;
  obraId: number | null = null;

  get esEdicion(): boolean {
    return this.obraId !== null;
  }

  constructor(
    private fb: FormBuilder,
    private obraService: ObraService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(200)]],
      descripcion: [''],
      genero: ['', Validators.maxLength(100)],
      duracionMinutos: [null, [Validators.min(1)]],
      activa: [true]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.obraId = +id;
      this.cargarObra();
    }
  }

  cargarObra(): void {
    this.loading = true;
    this.obraService.obtenerPorId(this.obraId!).subscribe({
      next: (obra) => {
        this.form.patchValue(obra);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar la obra', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/obras']);
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.guardando = true;
    const request = this.form.value;

    const operacion = this.esEdicion
      ? this.obraService.actualizar(this.obraId!, request)
      : this.obraService.crear(request);

    operacion.subscribe({
      next: () => {
        this.guardando = false;
        this.snackBar.open(
          this.esEdicion ? 'Obra actualizada correctamente' : 'Obra creada correctamente',
          'Cerrar',
          { duration: 3000 }
        );
        this.router.navigate(['/obras']);
      },
      error: (error) => {
        this.guardando = false;
        let mensaje = 'Error al guardar la obra';

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
    this.router.navigate(['/obras']);
  }
}
