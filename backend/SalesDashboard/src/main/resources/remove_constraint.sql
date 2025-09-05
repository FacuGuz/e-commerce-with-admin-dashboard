-- ELIMINAR COMPLETAMENTE la restricción CHECK de payment_method
-- Esto permite cualquier valor en el campo

-- Eliminar la restricción CHECK existente
ALTER TABLE invoice DROP CONSTRAINT IF EXISTS invoice_payment_method_check;

-- Verificar que se eliminó
SELECT conname, pg_get_constraintdef(oid) 
FROM pg_constraint 
WHERE conrelid = 'invoice'::regclass AND contype = 'c';
