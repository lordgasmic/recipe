create table ingredient_list (
recipe_id varchar(255) not null,
ingredient_id varchar(255) not null,
sequence_number integer not null,
constraint ingredient_list_pk primary key (recipe_id, ingredient_id)
);