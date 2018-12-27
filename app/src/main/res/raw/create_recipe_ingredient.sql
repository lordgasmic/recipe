create table recipe_ingredient (
recipe_id varchar(255) not null,
item_id varchar(255) not null,
sequence integer not null,
quantity_whole integer,
quantity_code varchar(255),
uom_code varchar(255)
constraint recipe_ingredient_pk primary key (recipe_id, item_id)
);