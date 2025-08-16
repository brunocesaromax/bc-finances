export class State {
  id: number;
  name: string;
}

export class City {
  id: number;
  name: string;
  state = new State();
}

export class Address {
  street: string;
  number: string;
  complement: string;
  neighborhood: string;
  zipCode: string;
  city = new City();
}

export class Contact {
  id: number;
  name: string;
  email: string;
  phone: string;

  constructor(id?: number, name?: string, email?: string, phone?: string) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
  }
}

export class Person {
  id: number;
  name: string;
  active = true;
  address = new Address();
  contacts = new Array<Contact>();
}

export class Category {
  id: number;
}

export class Transaction {
  id: string;
  description: string;
  dueDay: Date;
  payday: Date;
  value: number;
  observation: string;
  type = 'RECIPE';
  person = new Person();
  category = new Category();
  attachment: string;
  urlAttachment: string;
}
