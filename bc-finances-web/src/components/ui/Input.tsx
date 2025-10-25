import { forwardRef } from 'react'
import type { InputHTMLAttributes } from 'react'
import clsx from 'clsx'

type InputProps = {
  hasError?: boolean
} & InputHTMLAttributes<HTMLInputElement>

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ className, hasError, ...props }, ref) => (
    <input
      ref={ref}
      className={clsx(
        'w-full rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm text-slate-900 shadow-sm transition focus:border-brand-400 focus:outline-none focus:ring-2 focus:ring-brand-100 disabled:cursor-not-allowed disabled:bg-slate-100',
        hasError && 'border-red-400 focus:border-red-500 focus:ring-red-100',
        className,
      )}
      {...props}
    />
  ),
)

Input.displayName = 'Input'
