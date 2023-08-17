-- PIN según el usuario
SELECT pin FROM usuarios WHERE alias = ?;

-- Saldo del usuario
SELECT saldo FROM usuario WHERE id = ?;

-- Realizar un depósito o retiro
UPDATE usuario SET saldo = ? WHERE id = ?;
INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?,?,?);

-- Cambiar PIN
UPDATE usuario SET pin = ? WHERE id = ?: