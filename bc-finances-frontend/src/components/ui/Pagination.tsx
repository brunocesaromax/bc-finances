import clsx from 'clsx'

type PaginationProps = {
  page: number
  size: number
  total: number
  onPageChange: (page: number) => void
}

const getPageList = (current: number, totalPages: number) => {
  const pages: number[] = []
  const maxVisible = 5
  const half = Math.floor(maxVisible / 2)

  let start = Math.max(current - half, 0)
  let end = Math.min(start + maxVisible - 1, totalPages - 1)

  if (end - start + 1 < maxVisible) {
    start = Math.max(end - maxVisible + 1, 0)
  }

  for (let i = start; i <= end; i += 1) {
    pages.push(i)
  }

  return pages
}

export const Pagination = ({ page, size, total, onPageChange }: PaginationProps) => {
  const totalPages = Math.max(Math.ceil(total / size), 1)
  const isFirstPage = page <= 0
  const isLastPage = page >= totalPages - 1
  const pages = getPageList(page, totalPages)

  if (totalPages <= 1) {
    return null
  }

  return (
    <nav className="flex items-center justify-between gap-4 text-sm">
      <button
        type="button"
        className={clsx(
          'inline-flex items-center gap-1 rounded-full px-3 py-1.5 font-medium text-slate-600 transition hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-50',
        )}
        onClick={() => onPageChange(page - 1)}
        disabled={isFirstPage}
      >
        <span aria-hidden="true">&lt;</span>
        <span>Anterior</span>
      </button>

      <ul className="flex items-center gap-2">
        {pages.map((pageNumber) => {
          const isActive = pageNumber === page

          return (
            <li key={pageNumber}>
              <button
                type="button"
                className={clsx(
                  'h-9 w-9 rounded-full text-sm font-semibold transition',
                  isActive
                    ? 'bg-brand-600 text-white shadow-sm'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200',
                )}
                onClick={() => onPageChange(pageNumber)}
              >
                {pageNumber + 1}
              </button>
            </li>
          )
        })}
      </ul>

      <button
        type="button"
        className={clsx(
          'inline-flex items-center gap-1 rounded-full px-3 py-1.5 font-medium text-slate-600 transition hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-50',
        )}
        onClick={() => onPageChange(page + 1)}
        disabled={isLastPage}
      >
        <span>Próxima</span>
        <span aria-hidden="true">&gt;</span>
      </button>
    </nav>
  )
}
