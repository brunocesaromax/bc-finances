import clsx from 'clsx'
import type { LabelHTMLAttributes, ReactNode } from 'react'

type FormLabelProps = {
  requiredIndicator?: boolean
  children?: ReactNode
} & LabelHTMLAttributes<HTMLLabelElement>

export const FormLabel = ({
  className,
  children,
  requiredIndicator = false,
  ...props
}: FormLabelProps) => (
  <label
    className={clsx('mb-1 block text-sm font-semibold text-slate-700', className)}
    {...props}
  >
    <span className="inline-flex items-center gap-1">
      {children}
      {requiredIndicator ? (
        <span className="text-sm font-semibold text-red-500" aria-hidden="true">
          *
        </span>
      ) : null}
    </span>
  </label>
)
