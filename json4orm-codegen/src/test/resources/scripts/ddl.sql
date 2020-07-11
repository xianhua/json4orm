CREATE TABLE school(
   school_id serial PRIMARY KEY,
   name VARCHAR (255) NOT NULL,
   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE class(
   class_id serial PRIMARY KEY,
   name VARCHAR (255) NOT NULL,
   school_id integer,
   created_at TIMESTAMP DEFAULT NOW() 
);

CREATE TABLE student(
   student_id serial PRIMARY KEY,
   first_name VARCHAR (255) NOT NULL,
   last_name VARCHAR (255) NOT NULL,
   middle_name VARCHAR (255),
   birth_date date NOT NULL,
   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE teacher(
   teacher_id serial PRIMARY KEY, 
   first_name VARCHAR (255) NOT NULL,
   last_name VARCHAR (255) NOT NULL,
   middle_name VARCHAR (255),
   birth_date date NOT NULL,
   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE class_student(
   class_student_id serial PRIMARY KEY,
   class_id integer NOT NULL,
   student_id integer NOT NULL,
   score real,
   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE class_teacher(
   class_teacher_id serial PRIMARY KEY,
   class_id integer NOT NULL,
   teacher_id integer NOT NULL,
   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE schedule(
   schedule_id serial PRIMARY KEY,
   class_teacher_id integer,
   day_of_week VARCHAR (255) NOT NULL,
   time_of_day time NOT NULL,
   created_at TIMESTAMP DEFAULT NOW()
);
