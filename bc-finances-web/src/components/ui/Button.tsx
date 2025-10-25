import { forwardRef } from 'react'
import type { ButtonHTMLAttributes, ReactNode } from 'react'
import clsx from 'clsx'
import { Spinner } from '@/components/ui/Spinner'

type ButtonVariant = 'primary' | 'secondary' | 'ghost' | 'danger' | 'outline'

type ButtonProps = {
  variant?: ButtonVariant
  fullWidth?: boolean
  icon?: ReactNode
  loading?: boolean
} & ButtonHTMLAttributes<HTMLButtonElement>

const variantStyles: Record<ButtonVariant, string> = {
  primary:
    'bg-brand-600 text-white shadow-sm hover:bg-brand-700 focus-visible:outline-brand-500',
  secondary:
    'bg-white text-slate-900 ring-1 ring-slate-200 shadow-sm hover:bg-slate-50 focus-visible:outline-brand-500',
  outline:
    'bg-transparent text-brand-600 ring-1 ring-brand-200 hover:bg-brand-50 focus-visible:outline-brand-500',
  ghost:
    'bg-transparent text-slate-700 hover:bg-slate-100 focus-visible:outline-brand-500',
  danger:
    'bg-red-600 text-white shadow-sm hover:bg-red-700 focus-visible:outline-red-500',
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      variant = 'primary',
      className,
      fullWidth,
      disabled,
      icon,
      loading,
      children,
      ...props
    },
    ref,
  ) => (
    <button
      ref={ref}
      className={clsx(
        'inline-flex items-center justify-center gap-2 rounded-xl px-4 py-2 text-sm font-semibold transition-all focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 disabled:cursor-not-allowed disabled:opacity-60',
        variantStyles[variant],
        fullWidth && 'w-full',
        className,
      )}
      disabled={disabled || loading}
      {...props}
    >
      {loading ? (
        <Spinner className="h-4 w-4" />
      ) : (
        icon && <span className="text-base">{icon}</span>
      )}
      <span>{children}</span>
    </button>
  ),
)

Button.displayName = 'Button'
