CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    available BOOLEAN NOT NULL,
    default_ingredients JSON NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    table_number VARCHAR(100),
    is_for_takeaway BOOLEAN NOT NULL,
    customer_name VARCHAR(255),
    custom_notes VARCHAR(500),
    payment_method VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    tip NUMERIC(19, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS order_lines (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    sub_total NUMERIC(19, 2) NOT NULL,
    notes VARCHAR(255),
    selected_ingredients JSON,
    CONSTRAINT fk_order_lines_order
        FOREIGN KEY (order_id)
        REFERENCES orders (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS expenses (
    id VARCHAR(36) PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(500) NOT NULL,
    date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

