-- create location table
CREATE TABLE location (
    location_id SERIAL PRIMARY KEY,
    location_name VARCHAR(255) NOT NULL
);

-- Insert product table
CREATE TABLE product (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    location_id INT NOT NULL,
    CONSTRAINT fk_location
        FOREIGN KEY(location_id)
        REFERENCES Location(location_id)
        ON DELETE CASCADE
);

-- Insert locations
INSERT INTO location (location_id, location_name)
VALUES
    (1, 'Location A'),
    (2, 'Location B'),
    (3, 'Location C');

-- Insert products for the locations
INSERT INTO product (product_id, product_name, location_id)
VALUES
    (1, 'Product 1', 1),
    (2, 'Product 2', 1),
    (3, 'Product 3', 2),
    (4, 'Product 4', 2),
    (5, 'Product 5', 3),
    (6, 'Product 6', 3);