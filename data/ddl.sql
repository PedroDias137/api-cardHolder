CREATE TABLE IF NOT EXISTS CARD_HOLDER (
    id UUID PRIMARY KEY,
    STATUS VARCHAR(10),
    limit_total decimal(7,2),
    client_id uuid unique NOT NULL,
    created_at TIMESTAMP,
    bank_account_id uuid unique ,
    FOREIGN KEY (bank_account_id) REFERENCES BANK_ACCOUNT (id)
);

CREATE TABLE IF NOT EXISTS CARD (
    id UUID PRIMARY KEY,
    card_number int,
    cvv int,
    card_limit decimal(7,2),
    dueDate TIMESTAMP,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS BANK_ACCOUNT(

    id UUID PRIMARY KEY,
    bank_account varchar(10),
    agency varchar(4),
    bank_code varchar(3)
);


