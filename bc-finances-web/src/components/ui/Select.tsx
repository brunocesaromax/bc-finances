import { forwardRef } from 'react'
import type { SelectHTMLAttributes } from 'react'
import clsx from 'clsx'

type SelectProps = {
  hasError?: boolean
} & SelectHTMLAttributes<HTMLSelectElement>

export const Select = forwardRef<HTMLSelectElement, SelectProps>(
  ({ className, hasError, children, ...props }, ref) => (
    <div className="relative">
      <select
        ref={ref}
        className={clsx(
          'w-full appearance-none rounded-2xl border border-slate-200 bg-white px-4 py-2.5 text-sm text-slate-900 shadow-sm transition focus:border-brand-400 focus:outline-none focus:ring-2 focus:ring-brand-100 disabled:cursor-not-allowed disabled:bg-slate-100',
          hasError && 'border-red-400 focus:border-red-500 focus:ring-red-100',
          className,
        )}
        {...props}
      >
        {children}
      </select>
      <span className="pointer-events-none absolute inset-y-0 right-3 flex items-center text-slate-400">
        <svg
          width="12"
          height="12"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
        >
          <path
            d="M6 9L12 15L18 9"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </span>
    </div>
  ),
)

Select.displayName = 'Select'
