export type EstadoReserva = 'PENDIENTE' | 'CONFIRMADA' | 'RECHAZADA' | 'CANCELADA';

export interface Reserva {
  id: number;
  funcionId: number;
  obraTitulo: string;
  sala: string;
  fechaFuncion: string;
  cantidad: number;
  precioUnitario: number;
  total: number;
  estado: EstadoReserva;
  fechaReserva: string;
  usuarioEmail: string;
}

export interface ReservaRequest {
  funcionId: number;
  cantidad: number;
}
