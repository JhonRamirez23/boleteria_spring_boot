export interface Funcion {
  id: number;
  obraId: number;
  obraTitulo: string;
  fechaHora: string;
  sala: string;
  aforoTotal: number;
  aforoDisponible: number;
  precio: number;
}

export interface FuncionRequest {
  obraId: number;
  fechaHora: string;
  sala: string;
  aforoTotal: number;
  precio: number;
}
