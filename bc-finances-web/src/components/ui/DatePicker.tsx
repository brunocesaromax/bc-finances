import { useEffect, useMemo, useRef, useState, type MouseEvent as ReactMouseEvent } from 'react'
import { DayPicker } from 'react-day-picker'
import { ptBR } from 'react-day-picker/locale'
import 'react-day-picker/dist/style.css'
import clsx from 'clsx'
import { format, isValid, parse, parseISO } from 'date-fns'

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

export const DatePicker = ({
  id,
  name,
  value,
  onChange,
  onBlur,
  placeholder = 'dd/mm/aaaa',
  disabled = false,
  hasError = false,
  required = false,
  className,
}: DatePickerProps) => {
  const containerRef = useRef<HTMLDivElement | null>(null)
  const [isOpen, setIsOpen] = useState(false)

  const selectedDate = useMemo(() => parseDateValue(value), [value])
  const displayValue = useMemo(
    () => (selectedDate ? format(selectedDate, 'dd/MM/yyyy') : ''),
    [selectedDate],
  )

  useEffect(() => {
    if (!isOpen) {
      return
    }

    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false)
        onBlur?.()
      }
    }

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        setIsOpen(false)
        onBlur?.()
      }
    }

    document.addEventListener('mousedown', handleClickOutside)
    document.addEventListener('keydown', handleKeyDown)

    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
      document.removeEventListener('keydown', handleKeyDown)
    }
  }, [isOpen, onBlur])

  const handleSelect = (day?: Date) => {
    if (!day) {
      return
    }

    onChange?.(format(day, 'yyyy-MM-dd'))
    setIsOpen(false)
    onBlur?.()
  }

  const handleToggle = () => {
    if (disabled) {
      return
    }

    setIsOpen((prev) => !prev)
  }

  const handleClear = (event: ReactMouseEvent<HTMLButtonElement>) => {
    event.stopPropagation()
    onChange?.(null)
    setIsOpen(false)
    onBlur?.()
  }

  return (
    <div
      ref={containerRef}
      className={clsx('relative w-full', className)}
      data-date-picker=""
    >
      <button
        id={id}
        type="button"
        disabled={disabled}
        onClick={handleToggle}
        className={clsx(
          'flex w-full items-center justify-between rounded-xl border border-slate-200 bg-white px-4 py-2 text-left text-sm text-slate-900 shadow-sm transition focus:border-brand-400 focus:outline-none focus:ring-2 focus:ring-brand-100 disabled:cursor-not-allowed disabled:bg-slate-100',
          hasError && 'border-red-400 focus:border-red-500 focus:ring-red-100',
        )}
        aria-haspopup="dialog"
        aria-expanded={isOpen}
      >
        <span
          className={clsx('select-none', !displayValue && 'text-slate-400')}
        >
          {displayValue || placeholder}
        </span>

        <span className="ml-3 text-slate-400">
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
      </button>

      {value && !disabled ? (
        <button
          type="button"
          onClick={handleClear}
          className="absolute right-9 top-1/2 -translate-y-1/2 text-slate-400 transition hover:text-red-500"
          aria-label="Limpar data selecionada"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
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

      <input
        type="hidden"
        name={name}
        value={value ?? ''}
        readOnly
        aria-hidden="true"
        required={required}
      />

      {isOpen ? (
        <div className="absolute z-20 mt-2 w-[280px] rounded-2xl border border-slate-200 bg-white p-3 shadow-lg">
          <DayPicker
            mode="single"
            locale={ptBR}
            selected={selectedDate ?? undefined}
            onSelect={handleSelect}
            defaultMonth={selectedDate ?? new Date()}
            showOutsideDays
            classNames={{
              months: 'flex flex-col gap-4',
              month: 'space-y-2',
              caption: 'flex items-center justify-between px-1',
              caption_label: 'text-sm font-semibold text-slate-800',
              nav: 'flex items-center gap-2',
              nav_button: 'rdp-nav_button',
              table: 'rdp-table',
              head_row: 'rdp-head_row',
              head_cell: 'rdp-head_cell',
              row: 'rdp-row',
              cell: 'rdp-cell',
              day: 'rdp-day',
              day_selected: 'rdp-day_selected',
              day_today: 'rdp-day_today',
              day_outside: 'rdp-day_outside',
              day_disabled: 'rdp-day_disabled',
            }}
          />
        </div>
      ) : null}
    </div>
  )
}
