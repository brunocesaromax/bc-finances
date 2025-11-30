import clsx from 'clsx'
import type { HTMLAttributes } from 'react'

type BadgeVariant = 'default' | 'success' | 'danger' | 'info' | 'primary'

type BadgeProps = {
  variant?: BadgeVariant
} & HTMLAttributes<HTMLSpanElement>

const variants: Record<BadgeVariant, string> = {
  default: 'bg-slate-100 text-slate-700',
  success: 'bg-emerald-100 text-emerald-700',
  danger: 'bg-red-100 text-red-700',
  info: 'bg-brand-100 text-brand-700',
  primary: 'bg-brand-600 text-white shadow-sm',
}

export const Badge = ({ variant = 'default', className, ...props }: BadgeProps) => (
  <span
    className={clsx(
      'inline-flex items-center rounded-full px-3 py-1 text-xs font-medium',
      variants[variant],
      className,
    )}
    {...props}
  />
)
