export class Person {
  id: number;
  name: string;
  active: boolean;
  address: Address;
}

export interface Address {
  street: string;
  number: string;
  complement: string;
  neighborhood: string;
  zipCode: string;
  city: string;
  state: string;
}
