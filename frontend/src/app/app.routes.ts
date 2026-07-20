import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/layout/layout.component').then(m => m.LayoutComponent),
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'obras',
        loadComponent: () =>
          import('./features/obras/obras-list/obra-list.component').then(m => m.ObrasListComponent)
      },
      {
        path: 'obras/nueva',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/obras/obra-form/obra-form.component').then(m => m.ObraFormComponent)
      },
      {
        path: 'obras/editar/:id',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/obras/obra-form/obra-form.component').then(m => m.ObraFormComponent)
      },
      {
        path: 'funciones',
        loadComponent: () =>
          import('./features/funciones/funciones-list/funcion-list.component')
          .then(m => m.FuncionesListComponent)
      },
      {
        path: 'funciones/nueva',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/funciones/funciones-form/funcion-form.component')
          .then(m => m.FuncionFormComponent)
      },
      {
        path: 'funciones/editar/:id',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/funciones/funciones-form/funcion-form.component')
          .then(m => m.FuncionFormComponent)
      },
      // {
      //   path: 'reservas',
      //   loadComponent: () =>
      //     import('./features/reservas/reservas-list/reservas-list.component').then(m => m.ReservasListComponent)
      // },
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
