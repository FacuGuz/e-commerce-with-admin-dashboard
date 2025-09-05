-- SCRIPT PARA ELIMINAR TODAS LAS RESTRICCIONES PROBLEMÁTICAS
-- Ejecutar este script en tu base de datos PostgreSQL

-- 1. ELIMINAR restricción CHECK de payment_method
ALTER TABLE invoice DROP CONSTRAINT IF EXISTS invoice_payment_method_check;

-- 2. ELIMINAR llave foránea de product_id en invoice_detail
ALTER TABLE invoice_detail DROP CONSTRAINT IF EXISTS FKbe6c21nke5fy4m3vw00f23qsf;

-- 3. ELIMINAR cualquier otra restricción CHECK relacionada
ALTER TABLE invoice DROP CONSTRAINT IF EXISTS invoice_status_check;
ALTER TABLE invoice DROP CONSTRAINT IF EXISTS invoice_payment_status_check;

-- 4. Verificar que se eliminaron
SELECT conname, pg_get_constraintdef(oid) 
FROM pg_constraint 
WHERE conrelid = 'invoice'::regclass;

SELECT conname, pg_get_constraintdef(oid) 
FROM pg_constraint 
WHERE conrelid = 'invoice_detail'::regclass;
