import { apiClient } from '@/services/apiClient'
import type { LoginResponse } from '@/types/auth'

export type LoginPayload = {
  email: string
  password: string
}

export const authService = {
  async login(payload: LoginPayload): Promise<LoginResponse> {
    const { data } = await apiClient.post<LoginResponse>('/auth/login', payload)
    return data
  },

  async logout(): Promise<void> {
    await apiClient.delete('/auth/logout')
  },
}
