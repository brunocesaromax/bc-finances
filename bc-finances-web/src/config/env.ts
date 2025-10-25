const DEFAULT_API_URL = 'http://localhost:8080'

export const env = {
  apiUrl: import.meta.env.VITE_API_URL?.toString().trim() || DEFAULT_API_URL,
}
