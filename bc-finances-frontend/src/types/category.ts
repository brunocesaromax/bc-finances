import type { TransactionType } from './finance'

export type Category = {
  id: number
  name: string
  transactionType: TransactionType
}
