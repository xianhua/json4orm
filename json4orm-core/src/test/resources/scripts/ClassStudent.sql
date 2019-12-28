CREATE TABLE class_student(
   class_student_id serial PRIMARY KEY,
   class_id integer NOT NULL,
   student_id integer NOT NULL,
   score real
);