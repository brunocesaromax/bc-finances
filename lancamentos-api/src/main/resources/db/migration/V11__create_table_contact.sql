CREATE TABLE contact
(
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person(id)
);

insert into contact (person_id, name, email, phone)
values (1, 'Marcos Henrique', 'marcos@algamoney.com', '00 0000-0000');