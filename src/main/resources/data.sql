## Create in plazoleta-powerup
INSERT INTO restaurant (name, direction, id_proprietary, phone, url_logo, nit)
VALUES ('Restaurante A', 'Calle 123', '1231231231', '1234567890', 'https://example.com/logo1.png', '1234567890');
INSERT INTO restaurant (name, direction, id_proprietary, phone, url_logo, nit)
VALUES ('Restaurante B', 'Avenida 456', '1231231231', '9876543210', 'https://example.com/logo2.png', '0987654321');
## Create in user-powerup
INSERT INTO user (name, surname, mail, phone, address, idDniType, dniNumber, idPersonType, password, tokenPassword, birthDate, id_role)
VALUES ('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St', 'DNI', '1231231231', 'PERSON', 'password123', 'token123', '2000-01-01', 3);
