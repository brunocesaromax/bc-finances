import clsx from 'clsx'
import type { LabelHTMLAttributes } from 'react'

export const FormLabel = ({ className, ...props }: LabelHTMLAttributes<HTMLLabelElement>) => (
  <label
    className={clsx('mb-1 block text-sm font-semibold text-slate-700', className)}
    {...props}
  />
)
