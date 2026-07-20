export interface Obra {
  id: number;
  titulo: string;
  descripcion: string;
  duracionMinutos: number;
  activa: boolean;
}

export interface ObraRequest {
  titulo: string;
  descripcion: string;
  genero: number;
  duracionMinutos: number;
  activa: boolean;
}
