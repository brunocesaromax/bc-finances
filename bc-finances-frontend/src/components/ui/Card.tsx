import clsx from 'clsx'
import type { HTMLAttributes } from 'react'

export const Card = ({ className, ...props }: HTMLAttributes<HTMLDivElement>) => (
  <div
    className={clsx(
      'rounded-2xl border border-slate-100 bg-white p-6 shadow-[var(--shadow-card)] shadow-slate-950/5',
      className,
    )}
    {...props}
  />
)
