CREATE TABLE category(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL
);

insert into category (name) values ('Lazer');
insert into category (name) values ('Alimentação');
insert into category (name) values ('Supermecado');
insert into category (name) values ('Farmácia');
insert into category (name) values ('Outros');
