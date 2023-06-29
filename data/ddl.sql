CREATE TABLE IF NOT EXISTS BANK_ACCOUNT(

    id UUID PRIMARY KEY,
    bank_account varchar(10),
    agency varchar(4),
    bank_code varchar(3)
);

CREATE TABLE IF NOT EXISTS CARD_HOLDER (
    id UUID PRIMARY KEY,
    STATUS VARCHAR(10),
    limit_total decimal(7,2),
    available_limit decimal(7,2),
    credit_analysis_id uuid unique,
    client_id uuid unique NOT NULL,
    created_at TIMESTAMP,
    bank_account_id uuid unique,
    FOREIGN KEY (bank_account_id) REFERENCES BANK_ACCOUNT (id)
);

CREATE TABLE IF NOT EXISTS CARD (
    id UUID PRIMARY KEY,
    card_number varchar(19),
    cvv int,
    card_limit decimal(7,2),
    due_date TIMESTAMP,
    created_at TIMESTAMP,
    card_holder_id uuid NOT NULL,
    FOREIGN KEY (card_holder_id) REFERENCES CARD_HOLDER (id)
);




