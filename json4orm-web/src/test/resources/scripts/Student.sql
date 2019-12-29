CREATE TABLE student(
   student_id serial PRIMARY KEY,
   first_name VARCHAR (255) NOT NULL,
   last_name VARCHAR (255) NOT NULL,
   middle_name VARCHAR (255),
   birth_date date NOT NULL
);