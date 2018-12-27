create table recipe_steps (
recipe_id varchar(255) not null,
sequence integer,
step varchar(255)
constraint recipe_steps_pk primary key (recipe_id, sequence)
);