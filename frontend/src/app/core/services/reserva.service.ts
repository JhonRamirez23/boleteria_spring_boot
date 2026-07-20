import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Reserva, ReservaRequest, EstadoReserva } from '../../shared/models/reserva.model';
import { PageResponse } from '../../shared/models/paginacion.model';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private readonly apiUrl = `${environment.apiUrl}/reservas`;

  constructor(private http: HttpClient) {}

  misReservas(page: number = 0, size: number = 10): Observable<PageResponse<Reserva>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<Reserva>>(`${this.apiUrl}/mis-reservas`, { params });
  }

  listarPorEstado(estado: EstadoReserva, page: number = 0,
                  size: number = 10): Observable<PageResponse<Reserva>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<Reserva>>(
      `${this.apiUrl}/estado/${estado}`, { params });
  }

  listarPorFuncion(funcionId: number, page: number = 0,
                   size: number = 10): Observable<PageResponse<Reserva>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<Reserva>>(
      `${this.apiUrl}/funcion/${funcionId}`, { params });
  }

  crear(request: ReservaRequest): Observable<Reserva> {
    return this.http.post<Reserva>(this.apiUrl, request);
  }

  confirmar(id: number): Observable<Reserva> {
    return this.http.patch<Reserva>(`${this.apiUrl}/${id}/confirmar`, {});
  }

  rechazar(id: number): Observable<Reserva> {
    return this.http.patch<Reserva>(`${this.apiUrl}/${id}/rechazar`, {});
  }

  cancelar(id: number): Observable<Reserva> {
    return this.http.patch<Reserva>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}
