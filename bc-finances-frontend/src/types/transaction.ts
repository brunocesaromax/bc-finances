export type TransactionType = 'RECIPE' | 'EXPENSE'

export type TransactionSummary = {
  id: number
  description: string
  dueDay: string | null
  payday: string | null
  value: number
  type: TransactionType
  personName: string
  categoryName?: string
}

export type Transaction = {
  id?: number
  description: string
  dueDay: string | null
  payday: string | null
  value: number
  type: TransactionType
  observation: string
  person: {
    id: number
    name?: string
  }
  category: {
    id: number
    name?: string
  }
  attachment?: string | null
  urlAttachment?: string | null
}

export type TransactionFilter = {
  description?: string
  dueDayStart?: string
  dueDayEnd?: string
  page: number
  size: number
}

export type AttachmentResponse = {
  name: string
  url: string
}
