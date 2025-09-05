import { HttpInterceptorFn } from '@angular/common/http';

export const AuthInterceptor: HttpInterceptorFn = (request, next) => {
  // Obtener el token del localStorage
  const token = localStorage.getItem('auth_token');
  
  if (token) {
    // Agregar el header de autorización
    const authReq = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    
    return next(authReq);
  }
  
  return next(request);
};
