import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Funcion, FuncionRequest } from '../../shared/models/funcion.model';
import { PageResponse } from '../../shared/models/paginacion.model';

@Injectable({
  providedIn: 'root'
})
export class FuncionService {

  private readonly apiUrl = `${environment.apiUrl}/funciones`;

  constructor(private http: HttpClient) {}

  listarPorObra(obraId: number, page: number = 0, size: number = 10,
                soloFuturas: boolean = true): Observable<PageResponse<Funcion>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('soloFuturas', soloFuturas)
      .set('sort', 'fechaHora');

    return this.http.get<PageResponse<Funcion>>(
      `${this.apiUrl}/obra/${obraId}`, { params });
  }

  obtenerPorId(id: number): Observable<Funcion> {
    return this.http.get<Funcion>(`${this.apiUrl}/${id}`);
  }

  crear(funcion: FuncionRequest): Observable<Funcion> {
    return this.http.post<Funcion>(this.apiUrl, funcion);
  }

  actualizar(id: number, funcion: FuncionRequest): Observable<Funcion> {
    return this.http.put<Funcion>(`${this.apiUrl}/${id}`, funcion);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
