import { apiClient } from '@/services/apiClient'
import type { Tag } from '@/types/tag'

export const tagService = {
  async findAll(query?: string): Promise<Tag[]> {
    const { data } = await apiClient.get<Tag[]>('/tags', {
      params: query ? { query } : undefined,
    })
    return data
  },
}
