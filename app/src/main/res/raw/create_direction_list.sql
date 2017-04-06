create table direction_list (
recipe_id varchar(255) not null,
direction_id varchar(255) not null,
sequence integer,
constraint direction_list_pk primary key (recipe_id, direction_id)
);