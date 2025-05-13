create table if not exists discipline (
    id serial primary key,
    name varchar(255),
    json_body json
);

create table if not exists person (
    id serial primary key,
    name varchar(255),
    lastname varchar(255),
    patronymic varchar(255),
    email varchar(255)
);

create table if not exists university_position (
    id serial primary key,
    position varchar(255),
    academic_degree varchar(255),
    scientific_title varchar(255)
);

create table if not exists teacher (
    id serial primary key
        references person(id)
            on update restrict
            on delete cascade,
    position_id integer
        references university_position(id)
            on update restrict
            on delete cascade
);

create table if not exists teacher_discipline(
    id serial primary key,
    teacher_id integer references teacher(id)
        on delete cascade
        on update restrict,
    discipline_id integer references discipline(id)
        on delete cascade
        on update restrict
);

create table if not exists role (
    id serial primary key,
    name varchar(255) unique
);

create table if not exists account (
    id serial primary key,
    login varchar(255) unique,
    password varchar(255),
    person_id integer not null
        references person(id)
            on update restrict
            on delete cascade
);

create table if not exists account_role (
    id serial primary key,
    role_id integer not null
        references role(id)
            on update restrict
            on delete cascade,
    account_id integer not null
        references account(id)
            on update restrict
            on delete cascade
);

create table if not exists rpd (
    id serial primary key,
    author_name varchar(255),
    author_position varchar(255),
    discipline_name varchar(255),
    enroll_year integer,
    json_body json
);

insert into role (name) values ('TEACHER');
insert into role (name) values ('ADMIN');