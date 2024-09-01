CREATE TABLE IF NOT EXISTS exchange_rate (
    id SERIAL PRIMARY KEY,
    currency VARCHAR(3) NOT NULL,
    rate DECIMAL(10, 4) NOT NULL,
    date DATE NOT NULL
);
