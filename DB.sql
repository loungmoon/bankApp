create table bank_account(
id serial primary key,
account_no decimal,
account_holder text,
account_type text,
open_date date
);


create table transaction(
id serial primary key,
transaction_date date,
amount decimal,
transaction_type text,
bank_account_id int references bank_account(id)
);