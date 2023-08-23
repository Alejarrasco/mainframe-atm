
-- MOdificar el tipo de dato de una columna de double a decimal
ALTER TABLE usuarios MODIFY COLUMN saldo DECIMAL(10,2);
ALTER TABLE historico MODIFY COLUMN cantidad DECIMAL(10,2);