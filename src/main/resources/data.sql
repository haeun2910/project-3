-- Inserting sample shops
INSERT INTO shop (id, name, description, category, open_status, application_submitted, close_requested, close_reason)
VALUES
    (1, 'Shop 1', 'Description for Shop 1', 'ELECTRONICS', false, false, false, NULL),
    (2, 'Shop 2', 'Description for Shop 2', 'FASHION', true, true, false, NULL),
    (3, 'Shop 3', 'Description for Shop 3', 'HOME_AND_GARDEN', true, true, false, NULL),
    (4, 'Shop 4', 'Description for Shop 4', 'BEAUTY', false, false, false, NULL),
    (5, 'Shop 5', 'Description for Shop 5', 'SPORTS', false, false, false, NULL);

-- Inserting sample products
INSERT INTO product (id, name, description, price, stock, shop_id)
VALUES
    (1, 'Product 1', 'Description for Product 1', 100.00, 50, 2),
    (2, 'Product 2', 'Description for Product 2', 200.00, 30, 2),
    (3, 'Product 3', 'Description for Product 3', 150.00, 20, 3),
    (4, 'Product 4', 'Description for Product 4', 300.00, 40, 3),
    (5, 'Product 5', 'Description for Product 5', 250.00, 25, 2),
    (6, 'Product 6', 'Description for Product 6', 50.00, 60, 3),
    (7, 'Product 7', 'Description for Product 7', 500.00, 10, 1),
    (8, 'Product 8', 'Description for Product 8', 400.00, 15, 4);