import {
  forwardRef,
  useMemo,
  type MouseEvent as ReactMouseEvent,
} from 'react'
import ReactDatePicker from 'react-datepicker'
import { ptBR } from 'date-fns/locale/pt-BR'
import 'react-datepicker/dist/react-datepicker.css'
import clsx from 'clsx'
import { format, isValid, parse, parseISO } from 'date-fns'
import { Input } from '@/components/ui/Input'

type DatePickerProps = {
  id?: string
  name?: string
  value?: string | null
  onChange?: (value: string | null) => void
  onBlur?: () => void
  placeholder?: string
  disabled?: boolean
  hasError?: boolean
  required?: boolean
  className?: string
}

type CustomInputProps = {
  value?: string
  onClick?: () => void
  placeholder: string
  disabled?: boolean
  hasError?: boolean
  inputId?: string
  onClear?: (event: ReactMouseEvent<HTMLButtonElement>) => void
}

const parseDateValue = (value?: string | null) => {
  if (!value) {
    return null
  }

  const isoParsed = parseISO(value)
  if (isValid(isoParsed)) {
    return isoParsed
  }

  const fallback = parse(value, 'yyyy-MM-dd', new Date())
  return isValid(fallback) ? fallback : null
}

const DatePickerInput = forwardRef<HTMLInputElement, CustomInputProps>(
  (
    { value, onClick, placeholder, disabled, hasError, inputId, onClear },
    ref,
  ) => (
    <div className="relative w-full">
      <Input
        ref={ref}
        id={inputId}
        value={value ?? ''}
        placeholder={placeholder}
        onClick={onClick}
        readOnly
        disabled={disabled}
        hasError={hasError}
        className="pr-12"
      />

      {value && !disabled ? (
        <button
          type="button"
          className="absolute right-9 top-1/2 -translate-y-1/2 text-slate-300 transition hover:text-red-500"
          onClick={(event) => {
            event.preventDefault()
            event.stopPropagation()
            onClear?.(event)
          }}
          aria-label="Limpar data selecionada"
        >
          <svg
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            aria-hidden="true"
          >
            <path
              d="M18 6L6 18"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <path
              d="M6 6L18 18"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>
      ) : null}

      <span className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-slate-400">
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
        >
          <path
            d="M7 11H17"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <path
            d="M7 15H14"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <path
            d="M16 3V7"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <path
            d="M8 3V7"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <path
            d="M5 9H19"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <path
            d="M9 21H15C18 21 19 20 19 17V11C19 8 18 7 15 7H9C6 7 5 8 5 11V17C5 20 6 21 9 21Z"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </span>
    </div>
  ),
)

DatePickerInput.displayName = 'DatePickerInput'

export const DatePicker = ({
  id,
  name,
  value,
  onChange,
  onBlur,
  placeholder,
  disabled = false,
  hasError = false,
  required = false,
  className,
}: DatePickerProps) => {
  const selectedDate = useMemo(() => parseDateValue(value), [value])
  const handleChange = (date: Date | null) => {
    if (onChange) {
      onChange(date ? format(date, 'yyyy-MM-dd') : null)
    }
    onBlur?.()
  }

  const handleClear = (event: ReactMouseEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if (!disabled) {
      handleChange(null)
      onBlur?.()
    }
  }

  const placeholderValue = useMemo(
    () => format(new Date(), 'dd/MM/yyyy'),
    [],
  )

  return (
    <div className={clsx('w-full', className)}>
      <ReactDatePicker
        id={id}
        selected={selectedDate}
        onChange={(date) => handleChange(date)}
        dateFormat="dd/MM/yyyy"
        locale={ptBR}
        disabled={disabled}
        placeholderText={placeholder ?? placeholderValue}
        customInput={
          <DatePickerInput
            value={selectedDate ? format(selectedDate, 'dd/MM/yyyy') : ''}
            placeholder={placeholder ?? placeholderValue}
            disabled={disabled}
            hasError={hasError}
            inputId={id}
            onClear={handleClear}
          />
        }
        showPopperArrow={false}
        calendarClassName="date-picker-calendar"
        popperClassName="date-picker-popper"
        wrapperClassName="date-picker-wrapper"
        shouldCloseOnSelect
      />

      <input
        type="hidden"
        name={name}
        value={value ?? ''}
        readOnly
        aria-hidden="true"
        required={required}
      />
    </div>
  )
}
