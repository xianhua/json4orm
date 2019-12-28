CREATE TABLE schedule(
   schedule_id serial PRIMARY KEY,
   class_teacher_id integer,
   day_of_week VARCHAR (255) NOT NULL,
   time_of_day time NOT NULL
);