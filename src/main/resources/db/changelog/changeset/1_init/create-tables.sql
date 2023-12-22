CREATE TABLE _user (
    id uuid PRIMARY KEY
);

CREATE TABLE task (
    id serial PRIMARY KEY ,
    title varchar(255) NOT NULL ,
    description text NOT NULL,
    author_id uuid REFERENCES _user(id),
    performer_id uuid REFERENCES _user(id),
    status varchar(20) NOT NULL,
    priority varchar(20) NOT NULL
);

CREATE TABLE comment (
    id serial PRIMARY KEY,
    author_id uuid REFERENCES _user(id),
    task_id integer REFERENCES task(id),
    text varchar(255) NOT NULL
);
