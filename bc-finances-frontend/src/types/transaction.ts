import type { Category } from './category'
import type { TransactionType } from './finance'

export type Attachment = {
  name: string
  originalName: string
  contentType?: string | null
  size?: number | null
  url?: string | null
}

export type TransactionSummary = {
  id: number
  description: string
  dueDay: string | null
  payday: string | null
  value: number
  type: TransactionType
  categoryName?: string | null
  tags: string[]
  hasAttachments: boolean
}

export type TransactionDetail = {
  id: number
  description: string
  dueDay: string | null
  payday: string | null
  value: number
  type: TransactionType
  observation: string
  category: Category
  tags: string[]
  attachments: Attachment[]
}

export type TransactionPayload = {
  description: string
  dueDay: string
  payday?: string | null
  value: number
  type: TransactionType
  observation?: string | null
  categoryId: number
  tags: string[]
  attachments: Attachment[]
}

export type TransactionUpdatePayload = TransactionPayload & { id: number }

export type TransactionFilter = {
  description?: string
  dueDayStart?: string
  dueDayEnd?: string
  type?: TransactionType
  categoryId?: number
  tags?: string[]
  page: number
  size: number
}
