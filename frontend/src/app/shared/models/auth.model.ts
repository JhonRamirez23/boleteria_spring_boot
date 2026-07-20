export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  email: string;
  rol: string;
}

export interface Usuario {
  email: string;
  rol: string;
}
