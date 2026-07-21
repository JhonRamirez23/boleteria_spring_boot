import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Obra, ObraRequest } from '../../shared/models/obra.model';
import { PageResponse } from '../../shared/models/paginacion.model';

@Injectable({
  providedIn: 'root'
})
export class ObraService {

  private readonly apiUrl = `${environment.apiUrl}/obras`;

  constructor(private http: HttpClient) {}

  listar(page: number = 0, size: number = 10, titulo?: string): Observable<PageResponse<Obra>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', 'titulo');

    if (titulo) {
      params = params.set('titulo', titulo);
    }

    return this.http.get<PageResponse<Obra>>(this.apiUrl, { params });
  }

  listarAdmin(page: number = 0, size: number = 10): Observable<PageResponse<Obra>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', 'titulo');

    return this.http.get<PageResponse<Obra>>(`${this.apiUrl}/admin`, { params });
  }

  obtenerPorId(id: number): Observable<Obra> {
    return this.http.get<Obra>(`${this.apiUrl}/${id}`);
  }

  crear(obra: ObraRequest): Observable<Obra> {
    return this.http.post<Obra>(this.apiUrl, obra);
  }

  actualizar(id: number, obra: ObraRequest): Observable<Obra> {
    return this.http.put<Obra>(`${this.apiUrl}/${id}`, obra);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
