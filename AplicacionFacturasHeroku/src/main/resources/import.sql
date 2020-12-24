INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Alejandro', 'Muñoz', 'aleja@mail.com', '2020-08-28','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Pedro', 'Lara', 'pedrola@gmail.com', '2020-08-28','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Andres', 'Guzman', 'profesor@bolsadeideas.com', '2017-08-01','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('John', 'Doe', 'john.doe@gmail.com', '2017-08-02','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2017-08-10','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('John', 'Roe', 'john.roe@gmail.com', '2017-08-13','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2017-08-14','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2017-08-15','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2017-08-16','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2017-08-17','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2017-08-18','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2017-08-19','');  
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2017-08-20',''); 
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2017-08-21','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('John', 'Smith', 'john.smith@gmail.com', '2017-08-22','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2017-08-23','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2017-08-24','');
INSERT INTO customers (name, surname, email, create_at,photo) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2017-08-25','');


/* Populate tabla products */
INSERT INTO products (name, price, create_at) VALUES('Panasonic Pantalla LCD', 2599, NOW());
INSERT INTO products (name, price, create_at) VALUES('Sony Camara digital DSC-W320B', 1234, NOW());
INSERT INTO products (name, price, create_at) VALUES('Apple iPod shuffle', 14999, NOW());
INSERT INTO products (name, price, create_at) VALUES('Sony Notebook Z110', 3799, NOW());
INSERT INTO products (name, price, create_at) VALUES('Hewlett Packard Multifuncional F2280', 6999, NOW());
INSERT INTO products (name, price, create_at) VALUES('Bianchi Bicicleta Aro 26', 699, NOW());
INSERT INTO products (name, price, create_at) VALUES('Mica Comoda 5 Cajones', 2999, NOW());

/* Invoice */
INSERT INTO invoices (description, comments, customer_id, create_at) VALUES('Factura equipos de oficina', null, 1, NOW());
INSERT INTO invoice_items (amount, invoice_id, product_id) VALUES(1, 1, 1);
INSERT INTO invoice_items (amount, invoice_id, product_id) VALUES(2, 1, 4);
INSERT INTO invoice_items (amount, invoice_id, product_id) VALUES(1, 1, 5);
INSERT INTO invoice_items (amount, invoice_id, product_id) VALUES(1, 1, 7);

INSERT INTO invoices (description, comments, customer_id, create_at) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO invoice_items (amount, invoice_id, product_id) VALUES(3, 2, 6);
/*Users*/
INSERT INTO public.users(username,password,enabled) VALUES('admin','$2a$10$y/2CuURPg70./d0OnOmwkehQzPrrGhpBdi4jZf4e17olNolFmS4Lu',true);
INSERT INTO public.users(username,password,enabled) VALUES('user','$2a$10$ehH22NbxXpuYrsmF/KXtCugLIsuF7DC8sqVXloGsVRqfOwQX/gC0O',true);

/*Authorities*/
INSERT INTO authorities(user_id,authority) VALUES(1,'ROLE_USER');
INSERT INTO authorities(user_id,authority) VALUES(1,'ROLE_ADMIN');
INSERT INTO authorities(user_id,authority) VALUES(2,'ROLE_USER');