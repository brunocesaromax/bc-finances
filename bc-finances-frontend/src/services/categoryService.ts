import { apiClient } from '@/services/apiClient'
import type { Category } from '@/types/category'

export const categoryService = {
  async findAll(): Promise<Category[]> {
    const { data } = await apiClient.get<Category[]>('/categories', {
      params: {
        pagination: '',
      },
    })
    return data
  },
}
