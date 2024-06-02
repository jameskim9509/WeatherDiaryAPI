create table if not exists weather(
    date date not null primary key,
    temp double precision not null,
    main varchar(255) not null,
    icon varchar(255)
);

create table if not exists diary(
    id bigint not null auto_increment primary key,
    weather_date date,
    text varchar(255),
    foreign key (weather_date) references weather(date)
)