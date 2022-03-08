drop table if exists clients cascade;

create table clients(
id serial primary key,
first_name varchar(50) not null,
last_name varchar(50) not null,
age integer not null check(age>=0),
phone_number varchar(12) not null unique
)

drop table if exists accounts;

-- one client has many accounts
create table accounts(
id serial primary key,
a_type varchar(50) not null,
a_number char(8) not null unique,
balance money not null,
date_opened date not null,
client_id integer not null,

constraint fk_client foreign key(client_id) references clients(id) on delete cascade
)

-- DML queries
INSERT INTO clients (first_name, last_name, age, phone_number) 
VALUES 
('Jessica', 'Black', 41, '416-100-1111'),
('Emily', 'Velasquez', 57, '416-100-1112'),
('Kate', 'Wilson', 25, '416-100-1113'),
('Joel', 'Smith', 20, '416-100-1114'),
('Mark', 'Miller', 18, '416-100-1115'),
('Yang', 'Lee', 33, '416-100-1116');


select * from clients;

INSERT INTO accounts (a_type, a_number, balance, date_opened, client_id) 
VALUES 
('Checking Account', '12121212', '225.33','2012-03-19', 1),
('Chequing Account', '12121313', '710.85','2014-03-01', 1),
('Savings Account', '12121414', '10,000.00', '2020-02-02', 1),
('Chequing Account', '12121515', '86.07','2009-10-17', 2),
('Savings Account', '12121616', '6,660.00','2011-12-21', 2),
('Savings Account', '12121717', '10,000.00', '2001-06-06', 3),
('Chequing Account', '12121818', '333.69', '2000-08-08', 3),
('Chequing Account', '12121919', '710.85','2005-05-29', 4),
('Savings Account', '12122111', '10,000','2008-09-13', 4),
('Savings Account', '12122112', '49,000','2009-10-17', 4),
('Chequing Account', '12122113', '101.42','2017-12-05', 5),
('Savings Account', '12122114', '8,000.00','2021-12-09', 5),
('Chequing Account', '12122115', '555.04', '2003-04-05', 6),
('Savings Account', '12122116', '2,060.00','2015-01-11', 6);


select * from accounts;
