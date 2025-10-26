export type PageableResponse<T> = {
  content: T[]
  totalElements: number
  size: number
  number: number
}

export type Option = {
  label: string
  value: string | number
}
