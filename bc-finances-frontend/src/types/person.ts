export type State = {
  id: number
  name: string
}

export type City = {
  id: number
  name: string
  state?: State
}

export type Address = {
  street: string
  number: string
  complement: string
  neighborhood: string
  zipCode: string
  city: City
}

export type Contact = {
  id?: number
  name: string
  email: string
  phone: string
}

export type Person = {
  id?: number
  name: string
  active: boolean
  address: Address
  contacts: Contact[]
}

export type PersonMinimal = {
  id: number
  name: string
}

export type PersonSummary = {
  id: number
  name: string
  city: string
  state: string
  active: boolean
}

export type PersonFilter = {
  name?: string
  page: number
  size: number
}
