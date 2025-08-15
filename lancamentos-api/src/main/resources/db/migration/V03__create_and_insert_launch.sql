CREATE TABLE launch(
	id BIGSERIAL PRIMARY KEY,
	description VARCHAR(50) NOT NULL,
	due_day DATE NOT NULL,
	payday DATE,
	value DECIMAL(10,2) NOT NULL,
	observation VARCHAR (100),
	type VARCHAR (20) NOT NULL,
	category_id BIGINT NOT NULL,
	person_id BIGINT NOT NULL,
	CONSTRAINT category_fk FOREIGN KEY (category_id) references category(id),
    CONSTRAINT person_fk FOREIGN KEY (person_id) references person(id)
);

insert into launch (description, due_day, payday, value, observation, type, category_id, person_id)
values ('Salário Mensal', '2019-09-19', '2019-09-21', 150.88, null, 'DESPESA', 5, 1);
insert into launch (description, due_day, payday, value, observation, type, category_id, person_id)
values ('Lanche', '2018-02-19', '2019-09-21', 18.88, null, 'DESPESA', 4, 3);
insert into launch (description, due_day, payday, value, observation, type, category_id, person_id)
values ('Conta de luz', '2015-10-14', '2019-09-21', 180.00, null, 'DESPESA',2 , 2);
insert into launch (description, due_day, payday, value, observation, type, category_id, person_id)
values ('Internet fibra optica', '2020-03-12', '2030-09-21', 119.90, null, 'DESPESA', 1, 5);
