import { apiClient } from '@/services/apiClient'
import type {
  AttachmentResponse,
  Transaction,
  TransactionFilter,
  TransactionSummary,
} from '@/types/transaction'
import type { PageableResponse } from '@/types/common'

export const transactionService = {
  async search(
    filter: TransactionFilter,
  ): Promise<PageableResponse<TransactionSummary>> {
    const params: Record<string, string> = {
      page: filter.page.toString(),
      size: filter.size.toString(),
    }

    if (filter.description) {
      params.description = filter.description
    }

    if (filter.dueDayStart) {
      params.dueDayStart = filter.dueDayStart
    }

    if (filter.dueDayEnd) {
      params.dueDayEnd = filter.dueDayEnd
    }

    const { data } = await apiClient.get<PageableResponse<TransactionSummary>>(
      '/transactions',
      {
        params: {
          ...params,
          summary: '',
        },
      },
    )

    return data
  },

  async findById(id: number): Promise<Transaction> {
    const { data } = await apiClient.get<Transaction>(`/transactions/${id}`)
    return data
  },

  async save(transaction: Transaction): Promise<Transaction> {
    const { data } = await apiClient.post<Transaction>(
      '/transactions',
      transaction,
    )
    return data
  },

  async update(transaction: Transaction): Promise<Transaction> {
    if (!transaction.id) {
      throw new Error('Transaction id is required to update')
    }

    const { data } = await apiClient.put<Transaction>(
      `/transactions/${transaction.id}`,
      transaction,
    )
    return data
  },

  async delete(id: number): Promise<void> {
    await apiClient.delete(`/transactions/${id}`)
  },

  async uploadAttachment(file: File): Promise<AttachmentResponse> {
    const formData = new FormData()
    formData.append('attachment', file)

    const { data } = await apiClient.post<AttachmentResponse>(
      '/transactions/attachment',
      formData,
      {
        headers: { 'Content-Type': 'multipart/form-data' },
      },
    )

    return data
  },
}
