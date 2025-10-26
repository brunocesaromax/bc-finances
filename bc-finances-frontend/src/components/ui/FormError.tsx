import clsx from 'clsx'
import type { HTMLAttributes } from 'react'

export const FormError = ({ className, ...props }: HTMLAttributes<HTMLParagraphElement>) => (
  <p className={clsx('mt-1 text-xs font-medium text-red-600', className)} {...props} />
)
