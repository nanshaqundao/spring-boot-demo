CREATE TABLE IF NOT EXISTS assets (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        symbol VARCHAR(10) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS asset_price_history (
                                     id SERIAL PRIMARY KEY,
                                     asset_id INTEGER REFERENCES assets(id),
                                     price DECIMAL(18, 8) NOT NULL,
                                     timestamp TIMESTAMP NOT NULL
);
CREATE VIEW latest_asset_prices AS
SELECT a.id, a.name, a.symbol, p.price, p.timestamp
FROM assets a
         JOIN (
    SELECT asset_id, price, timestamp,
        ROW_NUMBER() OVER(PARTITION BY asset_id ORDER BY timestamp DESC) AS rn
    FROM asset_price_history
) p ON a.id = p.asset_id
WHERE p.rn = 1;
