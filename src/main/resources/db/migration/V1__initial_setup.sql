CREATE SCHEMA security;

CREATE TABLE security.role(
	"id" SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL
);

CREATE SCHEMA info;

CREATE TABLE info.state(
	"id" SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	"isoAlpha2" VARCHAR(2) NOT NULL
);

CREATE TABLE info.city(
	"id" BIGSERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	"stateAcronym" VARCHAR(2) NOT NULL,
	"idState" INT NOT NULL,
	FOREIGN KEY("idState") REFERENCES info.state("id")
);

CREATE SCHEMA client;

CREATE TABLE client.company(
	"id" SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	cnpj VARCHAR NOT NULL,
    "createdBy" BIGINT NOT NULL,
    "createdDt" TIMESTAMP DEFAULT NOW() NOT NULL,
    "updatedBy" BIGINT,
    "updatedDt" TIMESTAMP,
    "deletedBy" BIGINT,
    "deletedDt" TIMESTAMP,
	UNIQUE(cnpj)
);

CREATE TABLE client.user(
	"id" BIGSERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	"lastName" VARCHAR NOT NULL,
	phone BIGINT UNIQUE NOT NULL,
	email VARCHAR UNIQUE NOT NULL,
	password VARCHAR NOT NULL,
	cpf VARCHAR NOT NULL,
	"birthDate" DATE NOT NULL,
	sex CHAR NOT NULL,
	street VARCHAR NOT NULL,
	"streetNumber" INT NOT NULL,
	district VARCHAR NOT NULL,
	complement VARCHAR,
	cep VARCHAR NOT NULL,
	"idCity" BIGINT NOT NULL,
	"createdBy" BIGINT NOT NULL,
	"createdDt" TIMESTAMP DEFAULT NOW() NOT NULL,
	"updatedBy" BIGINT,
	"updatedDt" TIMESTAMP,
	"deletedBy" BIGINT,
	"deletedDt" TIMESTAMP,
	"idRole" INT NOT NULL,
	"idCompany" INT NOT NULL,
	"firstAccess" BOOLEAN DEFAULT TRUE NOT NULL,
	UNIQUE(cpf, "idCompany"),
	FOREIGN KEY("idCity") REFERENCES info.city("id"),
	FOREIGN KEY("createdBy") REFERENCES client.user("id"),
	FOREIGN KEY("updatedBy") REFERENCES client.user("id"),
	FOREIGN KEY("deletedBy") REFERENCES client.user("id"),
	FOREIGN KEY("idRole") REFERENCES security.role("id"),
	FOREIGN KEY("idCompany") REFERENCES client.company("id")
);

ALTER TABLE client.company ADD FOREIGN KEY("createdBy") REFERENCES client.user("id");
ALTER TABLE client.company ADD FOREIGN KEY("updatedBy") REFERENCES client.user("id");
ALTER TABLE client.company ADD FOREIGN KEY("deletedBy") REFERENCES client.user("id");