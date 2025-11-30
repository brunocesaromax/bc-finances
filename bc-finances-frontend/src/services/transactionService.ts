import { apiClient } from '@/services/apiClient'
import type { PageableResponse } from '@/types/common'
import type {
  Attachment,
  TransactionDetail,
  TransactionFilter,
  TransactionPayload,
  TransactionSummary,
  TransactionUpdatePayload,
} from '@/types/transaction'

type RequestOptions = {
  signal?: AbortSignal
}

export const transactionService = {
  async search(
    filter: TransactionFilter,
    options?: RequestOptions,
  ): Promise<PageableResponse<TransactionSummary>> {
    const params = new URLSearchParams()
    params.set('page', filter.page.toString())
    params.set('size', filter.size.toString())

    if (filter.description) {
      params.set('description', filter.description)
    }

    if (filter.dueDayStart) {
      params.set('dueDayStart', filter.dueDayStart)
    }

    if (filter.dueDayEnd) {
      params.set('dueDayEnd', filter.dueDayEnd)
    }

    if (filter.type) {
      params.set('type', filter.type)
    }

    if (filter.categoryId) {
      params.set('categoryId', filter.categoryId.toString())
    }

    if (filter.tags?.length) {
      filter.tags.forEach((tag) => params.append('tags', tag))
    }

    const { data } = await apiClient.get<PageableResponse<TransactionSummary>>(
      '/transactions',
      {
        params,
        signal: options?.signal,
      },
    )

    return data
  },

  async findById(id: number): Promise<TransactionDetail> {
    const { data } = await apiClient.get<TransactionDetail>(`/transactions/${id}`)
    return data
  },

  async save(payload: TransactionPayload): Promise<TransactionDetail> {
    const { data } = await apiClient.post<TransactionDetail>('/transactions', payload)
    return data
  },

  async update(payload: TransactionUpdatePayload): Promise<TransactionDetail> {
    const { id, ...rest } = payload
    if (!id) {
      throw new Error('Transaction id is required to update')
    }
    const { data } = await apiClient.put<TransactionDetail>(`/transactions/${id}`, rest)
    return data
  },

  async delete(id: number): Promise<void> {
    await apiClient.delete(`/transactions/${id}`)
  },

  async uploadAttachments(transactionId: number, files: File[]): Promise<Attachment[]> {
    if (!transactionId || !files || files.length === 0) {
      return []
    }

    const formData = new FormData()
    formData.append('transactionId', transactionId.toString())
    files.forEach((file) => formData.append('attachments', file))

    const { data } = await apiClient.post<Attachment[]>(
      '/transactions/attachments',
      formData,
      {
        headers: { 'Content-Type': 'multipart/form-data' },
      },
    )

    return data
  },
}
