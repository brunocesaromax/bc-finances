import { format, parseISO } from 'date-fns'

export const formatCurrencyBRL = (value: number) =>
  new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
    minimumFractionDigits: 2,
  }).format(value ?? 0)

export const formatDate = (value?: string | null, fallback = '—') => {
  if (!value) {
    return fallback
  }

  try {
    return format(parseISO(value), 'dd/MM/yyyy')
  } catch {
    return fallback
  }
}

export const parseDateInputValue = (value?: string | null): string | null => {
  if (!value) {
    return null
  }

  try {
    return format(parseISO(value), 'yyyy-MM-dd')
  } catch {
    return null
  }
}

export const ensureDateString = (value?: string | Date | null) => {
  if (!value) {
    return null
  }

  if (typeof value === 'string') {
    return value
  }

  return format(value, 'yyyy-MM-dd')
}
