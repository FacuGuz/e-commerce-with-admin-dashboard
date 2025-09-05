#  SalesDashboard - E-commerce Full Stack

Un e-commerce completo hecho con **Spring Boot** y **Angular**. Tiene todo lo basico: productos, usuarios, carrito y pagos con **MercadoPago**.

##  Que tiene

### Backend (Spring Boot)
- **JWT** para login con roles de usuario y admin
- **API REST** para productos, usuarios y ordenes
- **MercadoPago** para los pagos
- **PostgreSQL** con JPA/Hibernate
- **Spring Security** para la seguridad
- **Sistema de facturas** con estados

### Frontend (Angular)
- **Tailwind CSS** para que se vea lindo
- **Catálogo** de productos
- **Carrito** que se guarda
- **Checkout** con validaciones
- **Panel de admin** para manejar todo
- **Guards** para proteger rutas
- **Responsive** para celular y desktop

##  Stack tecnológico

### Backend
- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security** con JWT
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **MercadoPago SDK**

### Frontend
- **Angular 18**
- **TypeScript**
- **Tailwind CSS**
- **RxJS**

## ¿Que necesitas?

- **Java 17** o mas nuevo
- **Node.js 18** o mas nuevo
- **PostgreSQL 12** o mas nuevo
- **Maven 3.6** o mas nuevo
- **Cuenta de MercadoPago** (para los pagos)

## Como instalarlo

### 1. Clonar el repo
```bash
git clone https://github.com/tu-usuario/sales-dashboard.git
cd sales-dashboard
```

### 2. Configurar la base de datos
```sql
-- Crear la base
CREATE DATABASE sales_dashboard;

-- Crear usuario (opcional)
CREATE USER sales_user WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE sales_dashboard TO sales_user;
```

### 3. Configurar el Backend

1. Copia el archivo de config:
```bash
cp backend/SalesDashboard/src/main/resources/application.properties.example backend/SalesDashboard/src/main/resources/application.properties
```

2. Edita `application.properties` con tus datos:
```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/sales_dashboard
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# JWT Secret (genera una clave segura)
jwt.secret.key=clave_jwt

# MercadoPago
mercadopago.access.token=ACCESS_TOKEN
```

3. Ejecutar el backend:
```bash
cd backend/SalesDashboard
./mvnw spring-boot:run
```

### 4. Configurar el Frontend

1. Instalar dependencias:
```bash
cd frontend
npm install
```

2. Copia el archivo de config:
```bash
cp src/environments/environment.example.ts src/environments/environment.ts
```

3. Edita `environment.ts` con tu public key de MercadoPago:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  mercadopago: {
    publicKey: 'PUBLIC_KEY'
  }
};
```

4. Ejecutar el frontend:
```bash
npm start
```

## ¿Que hace?

### Para Usuarios
- ✅ **Registro y login** con validaciones
- ✅ **Catálogo** de productos con filtros
- ✅ **Carrito** que se guarda
- ✅ **Checkout** con autocompletado
- ✅ **Pagos** con MercadoPago
- ✅ **Historial** de compras

### Para Admins
- ✅ **Panel de admin** completo
- ✅ **CRUD** de productos
- ✅ **Gestión** de usuarios con roles
- ✅ **Ver compras** por usuario
- ✅ **Estados** de ordenes (PENDING, PAID, FAILED)

## Seguridad

- **JWT** para autenticacion
- **Roles** USER/ADMIN
- **Validaciones** en frontend y backend
- **CORS** configurado
- **Rutas protegidas**


## 🚀 Deploy

### Backend (Heroku/Railway)
```bash
# Variables de entorno
DATABASE_URL=postgresql://...
JWT_SECRET=tu_clave_secreta
MERCADOPAGO_ACCESS_TOKEN=tu_token
```

### Frontend (Vercel/Netlify)
```bash
# Variables de entorno
API_URL=https://tu-backend.herokuapp.com/api
MERCADOPAGO_PUBLIC_KEY=tu_public_key
```

## 📊 Estructura

```
sales-dashboard/
├── backend/
│   └── SalesDashboard/
│       ├── src/main/java/guzman/SalesDashboard/
│       │   ├── controllers/     # REST endpoints
│       │   ├── services/        # Logica de negocio
│       │   ├── entities/        # Entidades JPA
│       │   ├── repositories/    # Repositorios
│       │   ├── dtos/           # DTOs
│       │   ├── config/         # Configuraciones
│       │   └── Auth/           # JWT
│       └── src/main/resources/
│           └── application.properties
├── frontend/
│   └── src/
│       ├── app/
│       │   ├── components/     # Componentes Angular
│       │   ├── services/       # Servicios HTTP
│       │   ├── interfaces/     # Interfaces TS
│       │   ├── guards/         # Guards
│       │   └── interceptors/   # Interceptors
│       └── environments/       # Configs
└── README.md
```

## Contribuciones

Si queres contribuir:

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/nueva-feature`)
3. Commit (`git commit -m 'Agregar nueva feature'`)
4. Push (`git push origin feature/nueva-feature`)
5. Abre un PR

## 🚧 Proximos cambios

- [ ] **Sistema de notificaciones** en tiempo real
- [ ] **Dashboard de metricas** para admins (ingresos, productos mas vendidos)
- [ ] **Integracion con WhatsApp** para notificaciones
- [ ] **Modo oscuro** en el frontend
- [ ] **Tests unitarios** y de integracion
- [ ] **Docker** para deployment




⭐ **Si te gusta, deja una estrella!** ⭐
