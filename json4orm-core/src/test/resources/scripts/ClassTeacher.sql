CREATE TABLE class_teacher(
   class_teacher_id serial PRIMARY KEY,
   class_id integer NOT NULL,
   teacher_id integer NOT NULL
);