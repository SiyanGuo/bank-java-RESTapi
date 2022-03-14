drop table if exists accounts;
drop table if exists clients;

create table clients(
id serial primary key,
first_name varchar(50) not null,
last_name varchar(50) not null,
age integer not null check(age>0),
phone_number varchar(12) not null
);

-- one client has many accounts
create table accounts(
id serial primary key,
a_type varchar(50) not null,
a_number integer not null,
balance decimal(19,4) not null default 0 check(balance>=0),
date_opened date not null,
client_id integer not null,

constraint fk_client foreign key(client_id) references clients(id) on delete cascade
);

-- create auto increment for account number
create sequence account_number
start 10101112
increment 1
minvalue 10101112
owned by accounts.a_number;

-- DML queries
INSERT INTO clients (first_name, last_name, age, phone_number) 
VALUES 
('Jessica', 'Black', 41, '416-100-1111'),
('Emily', 'Velasquez', 57, '416-100-1112'),
('Kate', 'Wilson', 25, '416-100-1113'),
('Joel', 'Smith', 33, '416-100-1114'),
('Mark', 'Lee', 18, '416-100-1115');


INSERT INTO accounts (a_type, a_number, balance, date_opened, client_id) 
VALUES 
('Chequing Account', nextval('account_number'), '225.33','2012-03-19', 1),
('Chequing Account', nextval('account_number'), '710.85','2014-03-01', 1),
('Savings Account', nextval('account_number'), '1070.98', '2020-02-02', 1),
('Chequing Account', nextval('account_number'), '86.07','2009-10-17', 2),
('Savings Account', nextval('account_number'), '6660.00','2011-12-21', 2),
('Chequing Account', nextval('account_number'), '5.04', '2003-04-05', 3),
('Chequing Account', nextval('account_number'), '333.69', '2000-08-08', 3),
('Savings Account', nextval('account_number'), '245.40', '2001-06-06', 3),
('Chequing Account', nextval('account_number'), '710.85','2005-05-29', 4),
('Savings Account', nextval('account_number'), '10000.01','2008-09-13', 4),
('Savings Account', nextval('account_number'), '950.77','2009-10-17', 4),
('Chequing Account', nextval('account_number'), '101.42','2017-12-05', 5),
('Savings Account', nextval('account_number'), '860.90','2021-12-09', 5),
('Savings Account', nextval('account_number'), '2060.00','2015-01-11', 5);