import clsx from 'clsx'
import {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from 'react'

type CurrencyInputProps = {
  id?: string
  name?: string
  value?: number | null
  onChange?: (value: number | null) => void
  onBlur?: () => void
  placeholder?: string
  disabled?: boolean
  hasError?: boolean
  required?: boolean
}

const formatCurrency = (value: number | null | undefined) => {
  if (value == null || Number.isNaN(value)) {
    return ''
  }

  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
    minimumFractionDigits: 2,
  }).format(value)
}

const digitsToNumber = (digits: string): number | null => {
  if (!digits) {
    return null
  }

  const parsed = Number.parseInt(digits, 10)
  if (Number.isNaN(parsed)) {
    return null
  }

  return parsed / 100
}

export const CurrencyInput = forwardRef<HTMLInputElement, CurrencyInputProps>(
  (
    {
      id,
      name,
      value,
      onChange,
      onBlur,
      placeholder = 'R$ 0,00',
      disabled = false,
      hasError = false,
      required = false,
    },
    ref,
  ) => {
    const inputRef = useRef<HTMLInputElement | null>(null)
    useImperativeHandle(ref, () => inputRef.current as HTMLInputElement)

    const [displayValue, setDisplayValue] = useState(() => formatCurrency(value))

    useEffect(() => {
      setDisplayValue(formatCurrency(value))
    }, [value])

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      const digitsOnly = event.target.value.replace(/\D/g, '')
      const numericValue = digitsToNumber(digitsOnly)

      setDisplayValue(
        numericValue === null ? '' : formatCurrency(numericValue),
      )
      onChange?.(numericValue)
    }

    const handleBlur = () => {
      if (!displayValue) {
        onChange?.(null)
      }
      onBlur?.()
    }

    const handleFocus = () => {
      if (!displayValue) {
        setDisplayValue('')
      }
    }

    return (
      <input
        id={id}
        name={name}
        ref={inputRef}
        value={displayValue}
        onChange={handleChange}
        onBlur={handleBlur}
        onFocus={handleFocus}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        inputMode="decimal"
        autoComplete="off"
        className={clsx(
          'w-full rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm text-slate-900 shadow-sm transition focus:border-brand-400 focus:outline-none focus:ring-2 focus:ring-brand-100 disabled:cursor-not-allowed disabled:bg-slate-100',
          hasError && 'border-red-400 focus:border-red-500 focus:ring-red-100',
        )}
      />
    )
  },
)

CurrencyInput.displayName = 'CurrencyInput'
