create table Currencies
(
    ID       integer
        constraint Currencies_pk
            primary key autoincrement,
    Code     varchar not null,
    FullName varchar not null,
    Sign     varchar not null
);

