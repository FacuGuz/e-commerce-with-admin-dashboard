import { HttpInterceptorFn } from '@angular/common/http';

export const AuthInterceptor: HttpInterceptorFn = (request, next) => {
  // Obtener el token del localStorage (usando la clave correcta)
  const token = localStorage.getItem('auth_token');
  
  console.log('AuthInterceptor - Request URL:', request.url);
  console.log('AuthInterceptor - Token found:', !!token);
  console.log('AuthInterceptor - Token value:', token ? token.substring(0, 20) + '...' : 'null');
  
  if (token) {
    // Clonar la request y agregar el header de autorización
    const authReq = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    
    console.log('AuthInterceptor - Adding Authorization header');
    return next(authReq);
  }
  
  console.log('AuthInterceptor - No token found, proceeding without auth');
  return next(request);
};
