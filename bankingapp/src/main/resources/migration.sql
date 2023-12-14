-- LIQUIBASE FORMATTED SQL

CREATE TABLE USER_ACCOUNT (
    USER_ID                 BIGINT IDENTITY,
    EMAIL                   VARCHAR(255) NOT NULL,
    FIRST_NAME              VARCHAR(255) NOT NULL,
    LAST_NAME               VARCHAR(255) NOT NULL,
    PASSWORD                VARCHAR(255) NOT NULL,
    ROLE                    VARCHAR(255),
    EMAIL_VERIFIED BOOLEAN  BOOLEAN DEFAULT FALSE
) AUTO_INCREMENT=1;

CREATE TABLE REGISTRATION_TOKEN (
    ID                     BIGINT IDENTITY,
    TOKEN                  VARCHAR(255) NOT NULL,
    EMAIL                  VARCHAR(255) NOT NULL,
    EXPIRATION             DATE NOT NULL
) AUTO_INCREMENT=1;

CREATE TABLE BANKING_ACCOUNT (
    ACCOUNT_ID             BIGINT IDENTITY,
    IBAN                   VARCHAR(34),
    MONEY                  FLOAT NOT NULL,
    INTEREST_RATE_PA       FLOAT NOT NULL,
    ACTIVE                 BOOLEAN DEFAULT TRUE,
    CREATION_DATE          DATE DEFAULT NOT NULL CURRENT_DATE(),
    CURRENCY               VARCHAR(20) DEFAULT "EUR" NOT NULL,
    ACCOUNT_TYPE           VARCHAR(255) DEFAULT "CHECKING_ACCOUNT" NOT NULL

    FOREIGN KEY (USER_ID) REFERENCES USER_ACCOUNT(USER_ID)
) AUTO_INCREMENT=1;

CREATE TABLE BANKING_TRANSACTION (
    TRANSACTION_ID          BIGINT IDENTITY,
    CREATION_TIME           DATE NOT NULL,
    AMOUNT                  FLOAT NOT NULL,
    DESCRIPTION             VARCHAR(255),
    SENDING                 BOOLEAN,
    RECEIVER_IBAN           VARCHAR(34),
    RECEIVER_BIC            VARCHAR(11) NOT NULL

    FOREIGN KEY (ACCOUNT_ID) REFERENCES BANKING_ACCOUNT(ACCOUNT_ID)
) AUTO_INCREMENT=1;

CREATE TABLE ACCOUNT_TYPE_INTEREST_RATE (
    ACCOUNT_TYPE_INTEREST_RATE_ID   BIGINT IDENTITY,
    ACCOUNT_TYPE                    VARCHAR(255) NOT NULL,
    INTEREST_RATE                   FLOAT NOT NULL,
    CREATION_DATE                   DATE DEFAULT NOT NULL CURRENT_DATE()
) AUTO_INCREMENT=1;
