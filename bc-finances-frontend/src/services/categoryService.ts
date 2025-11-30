import { apiClient } from '@/services/apiClient'
import type { Category } from '@/types/category'
import type { TransactionType } from '@/types/finance'

export const categoryService = {
  async findAll(transactionType?: TransactionType): Promise<Category[]> {
    const { data } = await apiClient.get<Category[]>('/categories', {
      params: transactionType ? { type: transactionType } : undefined,
    })
    return data
  },
}
