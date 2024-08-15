CREATE TABLE client."userRegistrationInvitation"(
    "id" BIGSERIAL PRIMARY KEY,
    "email" VARCHAR NOT NULL,
    "idUser" BIGINT NOT NULL,
    "createdBy" BIGINT NOT NULL,
    "createdDt" TIMESTAMP DEFAULT NOW() NOT NULL,
    "updatedBy" BIGINT,
    "updatedDt" TIMESTAMP,
    "deletedBy" BIGINT,
    "deletedDt" TIMESTAMP,
    "expirationDt" TIMESTAMP NOT NULL,
    "token" VARCHAR NOT NULL,
    "consumedDt" TIMESTAMP,
    FOREIGN KEY("idUser") REFERENCES client.user,
    FOREIGN KEY("createdBy") REFERENCES client.user("id"),
    FOREIGN KEY("updatedBy") REFERENCES client.user("id"),
    FOREIGN KEY("deletedBy") REFERENCES client.user("id")
);