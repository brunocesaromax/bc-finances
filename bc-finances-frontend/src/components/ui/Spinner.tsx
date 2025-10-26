import clsx from 'clsx'

type SpinnerProps = {
  className?: string
}

export const Spinner = ({ className }: SpinnerProps) => (
  <span
    className={clsx(
      'inline-block animate-spin rounded-full border-2 border-slate-300 border-t-brand-500',
      className,
    )}
  />
)
