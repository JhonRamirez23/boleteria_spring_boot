export interface ApiError {
  status: number;
  error: string;
  mensaje: string;
  detalles: string[] | null;
  timestamp: string;
}
