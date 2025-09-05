# Configuración de MercadoPago para el Checkout

## Descripción
Este proyecto incluye una integración completa con MercadoPago para procesar pagos de forma segura. El checkout está configurado para funcionar tanto en modo sandbox (testing) como en producción.

## Características Implementadas

### Frontend (Angular)
- ✅ Formulario de checkout simplificado y funcional
- ✅ Visualización de productos con imágenes, nombres y precios
- ✅ Cálculo automático de subtotales, envío e impuestos
- ✅ Integración directa con MercadoPago
- ✅ Manejo de estados de pago (éxito, fallo, pendiente)
- ✅ Diseño responsive y moderno

### Backend (Spring Boot)
- ✅ API para crear preferencias de MercadoPago
- ✅ Webhook para recibir notificaciones de pago
- ✅ Manejo de errores y logging
- ✅ Configuración flexible para sandbox/producción

## Configuración Inicial

### 1. Crear Cuenta en MercadoPago
1. Ve a [MercadoPago Developers](https://www.mercadopago.com.ar/developers)
2. Crea una cuenta de desarrollador
3. Accede al [Panel de Desarrolladores](https://www.mercadopago.com.ar/developers/panel)

### 2. Obtener Credenciales
1. En el panel de desarrolladores, ve a "Tus integraciones"
2. Selecciona tu aplicación o crea una nueva
3. Copia el **Access Token** (público para sandbox, privado para producción)

### 3. Configurar Variables de Entorno

#### Backend (application.properties)
```properties
# MercadoPago Configuration
mercadopago.access.token=TU_ACCESS_TOKEN_AQUI
mercadopago.sandbox.enabled=true
mercadopago.success.url=http://localhost:4200/checkout/success
mercadopago.failure.url=http://localhost:4200/checkout/failure
mercadopago.pending.url=http://localhost:4200/checkout/pending
```

#### Frontend (environment.ts)
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  mercadopago: {
    publicKey: 'TU_PUBLIC_KEY_AQUI'
  }
};
```

## Modo Sandbox vs Producción

### Sandbox (Testing)
- ✅ Ideal para desarrollo y testing
- ✅ No se procesan pagos reales
- ✅ Usar credenciales de sandbox
- ✅ URLs locales (localhost)

### Producción
- ✅ Para usuarios reales
- ✅ Se procesan pagos reales
- ✅ Usar credenciales de producción
- ✅ URLs de tu dominio

## Flujo de Pago

1. **Usuario completa checkout** → Se valida información
2. **Se crea orden** → Se guarda en base de datos
3. **Se crea preferencia** → MercadoPago genera URL de pago
4. **Usuario es redirigido** → A MercadoPago para completar pago
5. **MercadoPago procesa pago** → Tarjeta, efectivo, transferencia, etc.
6. **Webhook recibe notificación** → Se actualiza estado de la orden
7. **Usuario regresa** → A página de éxito/fallo según resultado

## URLs de Retorno

### Éxito
```
http://localhost:4200/checkout/success?payment_id=123&status=approved
```

### Fallo
```
http://localhost:4200/checkout/failure?payment_id=123&status=rejected
```

### Pendiente
```
http://localhost:4200/checkout/pending?payment_id=123&status=pending
```

## Webhook de MercadoPago

El webhook recibe notificaciones automáticas cuando cambia el estado de un pago:

```json
{
  "type": "payment",
  "data": {
    "id": "123456789",
    "status": "approved",
    "external_reference": "ORDER_123",
    "transaction_amount": 100.50
  }
}
```

## Testing

### Tarjetas de Prueba (Sandbox)

#### Tarjeta de Crédito Aprobada
- Número: `4509 9535 6623 3704`
- CVV: `123`
- Fecha: Cualquier fecha futura
- Nombre: Cualquier nombre

#### Tarjeta de Crédito Rechazada
- Número: `4000 0000 0000 0002`
- CVV: `123`
- Fecha: Cualquier fecha futura

#### Tarjeta de Débito
- Número: `4000 0000 0000 0010`
- CVV: `123`
- Fecha: Cualquier fecha futura

### Otros Métodos de Pago
- **Efectivo**: Usar RapiPago o PagoFácil
- **Transferencia**: Usar cuenta bancaria de prueba
- **Billetera**: Usar cuenta de MercadoPago de prueba

## Solución de Problemas

### Error: "Invalid access token"
- Verificar que el token esté correctamente configurado
- Asegurar que el token sea válido para el entorno (sandbox/producción)

### Error: "Invalid preference data"
- Verificar que todos los campos requeridos estén presentes
- Asegurar que los precios sean números válidos
- Verificar que las URLs de retorno sean válidas

### Webhook no recibe notificaciones
- Verificar que la URL del webhook sea accesible públicamente
- Asegurar que el servidor esté funcionando
- Verificar logs del servidor para errores

## Seguridad

- ✅ Todas las comunicaciones usan HTTPS
- ✅ Los tokens de acceso están en variables de entorno
- ✅ Se valida la autenticidad de las notificaciones del webhook
- ✅ No se almacenan datos sensibles de tarjetas

## Próximos Pasos

1. **Implementar manejo de reembolsos**
2. **Agregar reportes de pagos**
3. **Implementar pagos recurrentes**
4. **Agregar múltiples monedas**
5. **Implementar pagos en cuotas**

## Recursos Útiles

- [Documentación Oficial de MercadoPago](https://www.mercadopago.com.ar/developers/es/docs)
- [SDK de Java](https://github.com/mercadopago/sdk-java)
- [Panel de Desarrolladores](https://www.mercadopago.com.ar/developers/panel)
- [Herramientas de Testing](https://www.mercadopago.com.ar/developers/es/docs/checkout-api/integration-test)

## Soporte

Si tienes problemas con la integración:
1. Revisa los logs del servidor
2. Verifica la configuración de credenciales
3. Consulta la documentación oficial
4. Contacta al equipo de desarrollo
