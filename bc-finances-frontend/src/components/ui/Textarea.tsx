import { forwardRef } from 'react'
import type { TextareaHTMLAttributes } from 'react'
import clsx from 'clsx'

type TextareaProps = {
  hasError?: boolean
  minRows?: number
} & TextareaHTMLAttributes<HTMLTextAreaElement>

export const Textarea = forwardRef<HTMLTextAreaElement, TextareaProps>(
  ({ className, hasError, rows = 3, minRows, ...props }, ref) => (
    <textarea
      ref={ref}
      rows={minRows ?? rows}
      className={clsx(
        'w-full rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm text-slate-900 shadow-sm transition focus:border-brand-400 focus:outline-none focus:ring-2 focus:ring-brand-100 disabled:cursor-not-allowed disabled:bg-slate-100',
        hasError && 'border-red-400 focus:border-red-500 focus:ring-red-100',
        className,
      )}
      {...props}
    />
  ),
)

Textarea.displayName = 'Textarea'
