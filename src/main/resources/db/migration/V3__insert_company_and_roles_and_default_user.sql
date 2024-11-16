SET session_replication_role = replica;

INSERT INTO client.company(name, cnpj, "createdBy")
    VALUES ('devTT', '11111111111111', 1);

INSERT INTO security.role(name)
    VALUES ('Master'),
        ('Manager'),
        ('User');

-- Password: 123456
INSERT INTO client.user(name, "lastName", phone, email, password, cpf, "birthDate", sex, street, "streetNumber",
    district, complement, cep, "idCity", "createdBy", "idRole", "idCompany", "firstAccess")
    VALUES ('dev', 'TT', 1111111111, 'devtt@email.com',
        '$2a$10$ooLYWMSN5I2bsWPIoyfXUO0yhlPUTz7CYC1z/hRW/L63zP3DZxmnq','11111111111',
        '2000-10-29', 'M', 'test', 1, 'test', '',
        '11111111', 1, 1, 1, 1, false);