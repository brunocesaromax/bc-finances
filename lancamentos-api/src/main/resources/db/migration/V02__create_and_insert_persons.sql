CREATE TABLE person(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	active BOOLEAN NOT NULL,
	street VARCHAR (50),
	number VARCHAR (5),
	complement VARCHAR (50),
	neighborhood VARCHAR (50),
	zipCode VARCHAR (10),
	city VARCHAR (25),
	state VARCHAR (25)
);

insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Bruno',true,'Rua 1','SN','qd 5 lt 44','Jardim Itaipu','74355509','Goiânia','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Jessica',false,'Rua 4','SN','qd 10 lt 89','Jardim Itaipu','74355510','Goiatuba','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Marcos',true,'Rua 5','SN','qd 90 lt 02','Cidade Jardim','7897949','Goiânia','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Carlos Lacerda',true,'Rua 1','SN','qd 5 lt 44','Jardim Itaipu','74355509','Goiânia','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Danila',true,'Rua 1','SN','qd 89 lt 34','Jardim Itaipu','71325509','Goiânia','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Geisson',true,'Rua 9','SN','qd 3 lt 20','Centro','4567982','Goiânia','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Valdevan',true,'Rua 56','SN','qd 1 lt 02','Residencial Itaipu','74355521','Goiânia','Goiás');
insert into person (name,active,street,number,complement,neighborhood,zipCode,city,state)
values ('Mel',true,'Rua 67','SN','qd 3 lt 14','Setor Oeste','1234567','Goiânia','Goiás');
