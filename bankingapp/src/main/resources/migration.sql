-- liquibase formatted sql

CREATE TABLE ACCOUNT (
    USER_ID                 BIGINT IDENTITY,
    EMAIL                   VARCHAR(255) NOT NULL,
    FIRST_NAME              VARCHAR(255) NOT NULL,
    LAST_NAME               VARCHAR(255) NOT NULL,
    PASSWORD                VARCHAR(255) NOT NULL,
    ROLE                    VARCHAR(255),
    EMAIL_VERIFIED BOOLEAN  BOOLEAN DEFAULT FALSE
);

CREATE TABLE REGISTRATION_TOKEN (
    ID                     BIGINT IDENTITY,
    TOKEN                  VARCHAR(255) NOT NULL,
    EMAIL                  VARCHAR(255) NOT NULL
);