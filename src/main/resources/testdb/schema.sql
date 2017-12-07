drop table USERS if exists;

create table USERS (ID bigint identity primary key, LOGIN varchar(9),
                        NAME varchar(50) not null, unique(LOGIN));
