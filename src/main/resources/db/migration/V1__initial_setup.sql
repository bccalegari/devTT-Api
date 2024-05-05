CREATE SCHEMA security;

CREATE TABLE security.permission(
	idPermission SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL
);

CREATE TABLE security.role(
	idRole SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL
);

CREATE TABLE security.rolePermission(
	idRole INT,
	idPermission INT,
	PRIMARY KEY(idRole, idPermission),
	FOREIGN KEY (idRole) REFERENCES security.role(idRole),
	FOREIGN KEY (idPermission) REFERENCES security.permission(idPermission)
);

CREATE SCHEMA info;

CREATE TABLE info.state(
	idState SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	isoAlpha2 CHAR(2) NOT NULL
);

CREATE TABLE info.city(
	idCity SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	stateAcronym CHAR(2) NOT NULL,
	idState INT NOT NULL,
	FOREIGN KEY(idState) REFERENCES info.state(idState)
);

CREATE SCHEMA client;

CREATE TABLE client.company(
	idCompany SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL
);

CREATE TABLE client.user(
	idUser BIGSERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	lastName VARCHAR NOT NULL,
	phone BIGINT UNIQUE NOT NULL,
	email VARCHAR UNIQUE NOT NULL,
	password VARCHAR NOT NULL,
	cpf BIGINT NOT NULL,
	cnpj BIGINT NOT NULL,
	birthDate DATE NOT NULL,
	sex CHAR NOT NULL,
	street VARCHAR NOT NULL,
	streetNumber INT NOT NULL,
	district VARCHAR NOT NULL,
	complement VARCHAR,
	cep VARCHAR NOT NULL,
	idCity INT NOT NULL,
	createdBy INT NOT NULL,
	createdDt TIMESTAMP DEFAULT NOW() NOT NULL,
	updatedBy INT,
	updatedDt TIMESTAMP,
	deletedBy INT,
	deletedDt TIMESTAMP,
	idRole INT NOT NULL,
	idCompany INT NOT NULL,
	UNIQUE(cpf, idCompany),
	UNIQUE(cnpj, idCompany),
	FOREIGN KEY(idCity) REFERENCES info.city(idCity),
	FOREIGN KEY(createdBy) REFERENCES client.user(idUser),
	FOREIGN KEY(updatedBy) REFERENCES client.user(idUser),
	FOREIGN KEY(deletedBy) REFERENCES client.user(idUser),
	FOREIGN KEY(idRole) REFERENCES security.role(idRole),
	FOREIGN KEY(idCompany) REFERENCES client.company(idCompany)
);