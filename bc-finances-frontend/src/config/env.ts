export const env = {
  apiUrl:
    import.meta.env.VITE_API_URL?.toString().trim() ||
    (import.meta.env.DEV ? 'http://localhost:8080' : '/api'),
}
