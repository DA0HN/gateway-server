create database gateway_server;

create table estatisticas_ar_condicionado
(
    timestamp         bigint       not null primary key,
    created           datetime(6)  null,
    current           double       null,
    destiny           varchar(255) null,
    elapsedTime       bigint       null,
    energyConsumption double       null,
    humidity          double       null,
    power             double       null,
    source            varchar(255) null,
    temperature       double       null
);
create table received_data
(
    raw          bigint       not null primary key,
    elapsedTime  bigint       null,
    _from        varchar(255) null,
    receivedTime datetime(6)  null,
    sendTime     datetime(6)  null,
    _to          varchar(255) null
);
create table received_message
(
    received_id bigint       not null,
    message     varchar(255) null,
    constraint FK5b9lqbjowobi8lw4kenaf9k1g foreign key (received_id) references received_data (raw)
);
