import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../core/services/auth.service';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})

export class DashboardComponent {
  sidenavOpen = true;
  usuario = this.authService.getUsuario();

  menuItems = [
    { label: 'Obras', icon: 'movie', route: '/obras', adminOnly: false },
    { label: 'Funciones', icon: 'event', route: '/funciones', adminOnly: false },
    { label: 'Mis Reservas', icon: 'confirmation_number', route: '/reservas', adminOnly: false },
    { label: 'Gestión de Reservas', icon: 'admin_panel_settings', route: '/reservas/admin', adminOnly: true }
  ];

  constructor(private authService: AuthService, private router: Router) {}

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  get itemsVisibles() {
    return this.menuItems.filter(item => !item.adminOnly || this.isAdmin);
  }

  logout(): void {
    this.authService.logout();
  }

  toggleSidenav(): void {
    this.sidenavOpen = !this.sidenavOpen;
  }
}
