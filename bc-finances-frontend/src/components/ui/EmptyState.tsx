import { Card } from '@/components/ui/Card'
import clsx from 'clsx'

type EmptyStateProps = {
  title: string
  description?: string
  className?: string
}

export const EmptyState = ({ title, description, className }: EmptyStateProps) => (
  <Card className={clsx('text-center', className)}>
    <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-brand-50 text-brand-600">
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
      >
        <path
          d="M9 10H15M9 14H12M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z"
          stroke="currentColor"
          strokeWidth="1.8"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    </div>
    <h3 className="mt-4 text-lg font-semibold text-slate-900">{title}</h3>
    {description ? (
      <p className="mt-2 text-sm text-slate-500">{description}</p>
    ) : null}
  </Card>
)
