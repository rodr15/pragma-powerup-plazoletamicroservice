## Create in plazoleta-powerup


# Init Restaurants
INSERT INTO `plazoleta-powerup`.restaurant (name, direction, id_proprietary, phone, url_logo, nit)
VALUES
    ('El Sabor Del Mar', 'Calle Principal 123', '1231231232', '555-1234', 'logo1.png', 'NIT1234' ),
    ('La Trattoria', 'Avenida Central 456', '1231231232', '555-5678', 'logo2.png', 'NIT5678'),
    ('Sabores Exóticos', 'Calle Secundaria 789', '1231231233', '555-9012', 'logo3.png', 'NIT9012' ),
    ('Delicias Gourmet', 'Paseo de los Sabores 321', '1231231233', '555-3456', 'logo4.png', 'NIT3456');

# Init Category
INSERT INTO `plazoleta-powerup`.category (id, name, description)
VALUES
    (1, 'Entrantes', 'Deliciosos platos para empezar la comida'),
    (2, 'Platos Principales', 'Sabrosos platos para el plato fuerte'),
    (3, 'Postres', 'Dulces y tentadores postres para finalizar la comida'),
    (4, 'Bebidas Refrescantes', 'Refrescantes y deliciosas bebidas para acompañar tus platos');

# Init Dish
INSERT INTO `plazoleta-powerup`.dish (name, category_id, description, price, restaurant_id, url_image, active)
VALUES
    ('Ceviche de camarones', 1, 'Delicioso ceviche de camarones frescos', 15, 1, 'ceviche.jpg', true),
    ('Lomo saltado', 2, 'Sabroso lomo saltado con carne jugosa y papas fritas', 20, 1, 'lomo_saltado.jpg', true),
    ('Tiramisú', 3, 'Clásico postre italiano con capas de bizcocho, café y crema', 8, 1, 'tiramisu.jpg', true),
    ('Pisco Sour', 4, 'Tradicional cóctel peruano con pisco y jugo de limón', 10, 1, 'pisco_sour.jpg', true),
    ('Sopa de lentejas', 1, 'Caliente y reconfortante sopa de lentejas con verduras', 12, 2, 'sopa_lentejas.jpg', true),
    ('Pollo a la brasa', 2, 'Jugoso pollo a la brasa con papas y ensalada', 18, 2, 'pollo_brasa.jpg', true),
    ('Brownie de chocolate', 3, 'Irresistible brownie de chocolate con helado de vainilla', 9, 2, 'brownie_chocolate.jpg', true),
    ('Chicha morada', 4, 'Refrescante bebida peruana hecha a base de maíz morado', 6, 2, 'chicha_morada.jpg', true),
    ('Ensalada caprese', 1, 'Ensalada fresca con tomate, mozzarella y albahaca', 14, 3, 'ensalada_caprese.jpg', true),
    ('Pasta carbonara', 2, 'Clásica pasta italiana con salsa carbonara', 16, 3, 'pasta_carbonara.jpg', true),
    ('Tarta de manzana', 3, 'Deliciosa tarta de manzana casera con canela', 7, 3, 'tarta_manzana.jpg', true),
    ('Mojito', 4, 'Refrescante cóctel cubano con ron, menta y lima', 11, 3, 'mojito.jpg', true),
    ('Sushi variado', 1, 'Selección de sushi fresco con diferentes tipos de pescado', 22, 4, 'sushi_variado.jpg', true),
    ('Ramen de pollo', 2, 'Caldo sabroso con fideos y trozos de pollo', 17, 4, 'ramen_pollo.jpg', true),
    ('Cheesecake de fresa', 3, 'Delicioso cheesecake de fresa con base de galleta', 10, 4, 'cheesecake_fresa.jpg', true),
    ('Margarita', 4, 'Cóctel clásico a base de tequila, jugo de limón y triple sec', 12, 4, 'margarita.jpg', true);