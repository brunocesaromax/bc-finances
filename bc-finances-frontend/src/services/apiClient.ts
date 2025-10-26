import axios from 'axios'
import { env } from '@/config/env'
import { authStorage } from '@/storage/authStorage'

type UnauthorizedHandler = () => void

let unauthorizedHandler: UnauthorizedHandler | null = null

export const setUnauthorizedHandler = (handler: UnauthorizedHandler | null) => {
  unauthorizedHandler = handler
}

export const apiClient = axios.create({
  baseURL: env.apiUrl,
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use(
  (config) => {
    const token = authStorage.getToken()

    if (token) {
      config.headers = config.headers ?? {}
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error) => Promise.reject(error),
)

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    const requestUrl: string = error.config?.url ?? ''

    if (
      status === 401 &&
      !requestUrl.endsWith('/auth/login') &&
      !requestUrl.endsWith('/auth/logout')
    ) {
      authStorage.clearToken()
      unauthorizedHandler?.()
    }

    return Promise.reject(error)
  },
)
