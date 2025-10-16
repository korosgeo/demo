CREATE TABLE IF NOT EXISTS computer_sale (
    id SERIAL PRIMARY KEY,
    country VARCHAR(100),
    timescale VARCHAR(50),
    vendor VARCHAR(100),
    units BIGINT
);
