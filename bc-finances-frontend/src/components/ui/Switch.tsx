import { forwardRef } from 'react'
import type { InputHTMLAttributes } from 'react'
import clsx from 'clsx'

type SwitchProps = InputHTMLAttributes<HTMLInputElement>

export const Switch = forwardRef<HTMLInputElement, SwitchProps>(
  ({ className, disabled, ...props }, ref) => (
    <label className={clsx('inline-flex cursor-pointer items-center gap-3', className)}>
      <input
        type="checkbox"
        className="peer sr-only"
        disabled={disabled}
        ref={ref}
        {...props}
      />
      <span
        aria-hidden="true"
        className={clsx(
          'relative inline-flex h-6 w-11 items-center rounded-full bg-slate-300 transition-colors peer-checked:bg-brand-500',
          disabled && 'opacity-60',
        )}
      >
        <span
          className={clsx(
            'inline-block h-5 w-5 translate-x-1 transform rounded-full bg-white transition-all shadow-sm peer-checked:translate-x-5',
          )}
        />
      </span>
    </label>
  ),
)

Switch.displayName = 'Switch'
