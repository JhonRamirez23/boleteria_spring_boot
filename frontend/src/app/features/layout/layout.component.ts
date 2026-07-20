import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-layout',
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
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

  sidenavOpen = true;
  usuario = this.authService.getUsuario();

  menuItems: { label: string, icon: string, route: string, adminOnly: boolean, userOnly: boolean }[] = [
    { label: 'Obras', icon: 'movie', route: '/obras', adminOnly: false, userOnly: false },
    { label: 'Funciones', icon: 'event', route: '/funciones', adminOnly: false, userOnly: false },
    { label: 'Mis Reservas', icon: 'confirmation_number', route: '/reservas', adminOnly: false, userOnly: true },
    { label: 'Gestión de Reservas', icon: 'admin_panel_settings', route: '/reservas/admin', adminOnly: true, userOnly: false }
  ];

  constructor(private authService: AuthService) {}

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  get itemsVisibles() {
    return this.menuItems.filter(item => {
      if (item.adminOnly && !this.isAdmin) return false;
      if (item.userOnly && this.isAdmin) return false;
      return true;
    });
  }

  logout(): void {
    this.authService.logout();
  }

  toggleSidenav(): void {
    this.sidenavOpen = !this.sidenavOpen;
  }
}
