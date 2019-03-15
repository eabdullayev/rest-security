insert into product (id, prod_desc, prod_image, prod_name, prod_price)
values (SEQ_PRODUCT_ID.nextVal, 'test product', 'https://dummyimage.com/600x400/000/fff.gif&text=test', 'Test', 1.23);
insert into product(id, prod_desc, prod_image, prod_name, prod_price)
values (SEQ_PRODUCT_ID.nextVal, 'test2 product', 'https://dummyimage.com/600x400/000/fff.gif&text=test2', 'Test2', 1.23);

insert into role (id, role) values (SEQ_ROLE_ID.nextVal, 'ADMIN');
