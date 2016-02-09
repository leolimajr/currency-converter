create table if not exists customer (
  username varchar(256) not null,
  birth_date date,
  password varchar(64),
  street varchar(256),
  zip_code varchar(10),
  city varchar(256),
  country varchar(2),
  authority varchar(32),
     constraint pk_customer primary key (username)
);

create table if not exists currency_conversion (
  id int auto_increment,
  from_currency char(3),
  to char(3),
  amount  decimal(20,7),
  rate  decimal(20,7),
  customer_username varchar(256),
  reference date
);

alter table currency_conversion add constraint if not exists fk_customer_username foreign key (customer_username) references customer (username);