import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import { transactionService } from '@/services/transactionService'
import type { TransactionSummary } from '@/types/transaction'
import { Input } from '@/components/ui/Input'
import { FormLabel } from '@/components/ui/FormLabel'
import { Button } from '@/components/ui/Button'
import { Badge } from '@/components/ui/Badge'
import { EmptyState } from '@/components/ui/EmptyState'
import { Spinner } from '@/components/ui/Spinner'
import { Pagination } from '@/components/ui/Pagination'
import { formatCurrencyBRL, formatDate } from '@/utils/formatters'
import { useAuth } from '@/hooks/useAuth'
import { PERMISSIONS } from '@/utils/permissions'

const PAGE_SIZE = 6

type FilterState = {
  description: string
  dueDayStart: string
  dueDayEnd: string
}

type FetchState = {
  content: TransactionSummary[]
  totalElements: number
}

export const TransactionsListPage = () => {
  const { hasPermission } = useAuth()
  const [filters, setFilters] = useState<FilterState>({
    description: '',
    dueDayStart: '',
    dueDayEnd: '',
  })
  const [page, setPage] = useState(0)
  const [isLoading, setIsLoading] = useState(true)
  const [fetchState, setFetchState] = useState<FetchState>({
    content: [],
    totalElements: 0,
  })

  const canCreate = hasPermission(PERMISSIONS.CREATE_TRANSACTION)
  const canDelete = hasPermission(PERMISSIONS.REMOVE_TRANSACTION)

  const loadTransactions = useMemo(
    () =>
      async (pageIndex: number, filtersState: FilterState) => {
        setIsLoading(true)
        try {
          const response = await transactionService.search({
            page: pageIndex,
            size: PAGE_SIZE,
            description: filtersState.description || undefined,
            dueDayStart: filtersState.dueDayStart || undefined,
            dueDayEnd: filtersState.dueDayEnd || undefined,
          })

          setFetchState({
            content: response.content,
            totalElements: response.totalElements,
          })
        } catch (error) {
          toast.error('Não foi possível carregar os lançamentos.')
        } finally {
          setIsLoading(false)
        }
      },
    [],
  )

  useEffect(() => {
    loadTransactions(page, filters)
  }, [filters, loadTransactions, page])

  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setFilters((prev) => ({ ...prev, [name]: value }))
  }

  const handleFilterSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setPage(0)
    loadTransactions(0, filters)
  }

  const handleDelete = async (transaction: TransactionSummary) => {
    const confirmed = window.confirm(
      `Excluir o lançamento "${transaction.description}"?`,
    )

    if (!confirmed) {
      return
    }

    try {
      await transactionService.delete(transaction.id)
      toast.success('Lançamento removido com sucesso!')
      loadTransactions(page, filters)
    } catch (error) {
      toast.error('Não foi possível remover o lançamento.')
    }
  }

  const transactions = fetchState.content
  const total = fetchState.totalElements

  return (
    <div className="space-y-6">
      <header className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900">
            Lançamentos
          </h1>
          <p className="text-sm text-slate-500">
            Acompanhe receitas, despesas e anexos em uma visão centralizada.
          </p>
        </div>
        {canCreate ? (
          <Link
            to="/transactions/new"
            className="inline-flex items-center justify-center rounded-xl bg-brand-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-700"
          >
            Novo lançamento
          </Link>
        ) : null}
      </header>

      <form
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm md:grid-cols-4"
        onSubmit={handleFilterSubmit}
      >
        <div className="md:col-span-2">
          <FormLabel htmlFor="description">Descrição</FormLabel>
          <Input
            id="description"
            name="description"
            placeholder="Buscar por descrição"
            value={filters.description}
            onChange={handleFilterChange}
          />
        </div>
        <div>
          <FormLabel htmlFor="dueDayStart">Vencimento inicial</FormLabel>
          <Input
            id="dueDayStart"
            name="dueDayStart"
            type="date"
            value={filters.dueDayStart}
            onChange={handleFilterChange}
          />
        </div>
        <div>
          <FormLabel htmlFor="dueDayEnd">Vencimento final</FormLabel>
          <Input
            id="dueDayEnd"
            name="dueDayEnd"
            type="date"
            value={filters.dueDayEnd}
            onChange={handleFilterChange}
          />
        </div>
        <div className="md:col-span-4 flex justify-end">
          <Button type="submit" className="w-full md:w-auto">
            Aplicar filtros
          </Button>
        </div>
      </form>

      {isLoading ? (
        <div className="flex justify-center py-12">
          <Spinner className="h-8 w-8" />
        </div>
      ) : transactions.length === 0 ? (
        <EmptyState
          title="Nenhum lançamento encontrado"
          description="Ajuste os filtros ou cadastre um novo lançamento para visualizá-lo aqui."
        />
      ) : (
        <div className="space-y-6">
          <div className="hidden overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm lg:block">
            <table className="min-w-full divide-y divide-slate-200 text-sm">
              <thead className="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                <tr>
                  <th className="px-6 py-3">Pessoa</th>
                  <th className="px-6 py-3">Descrição</th>
                  <th className="px-6 py-3">Vencimento</th>
                  <th className="px-6 py-3">Pagamento</th>
                  <th className="px-6 py-3">Valor</th>
                  <th className="px-6 py-3">Tipo</th>
                  <th className="px-6 py-3 text-right">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200 text-sm text-slate-700">
                {transactions.map((transaction) => (
                  <tr key={transaction.id}>
                    <td className="px-6 py-4 font-semibold text-slate-900">
                      {transaction.personName}
                    </td>
                    <td className="px-6 py-4">{transaction.description}</td>
                    <td className="px-6 py-4">{formatDate(transaction.dueDay)}</td>
                    <td className="px-6 py-4">{formatDate(transaction.payday)}</td>
                    <td className="px-6 py-4 font-semibold">
                      <span
                        className={transaction.type === 'EXPENSE' ? 'text-red-600' : 'text-emerald-600'}
                      >
                        {formatCurrencyBRL(transaction.value)}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <Badge variant={transaction.type === 'EXPENSE' ? 'danger' : 'success'}>
                        {transaction.type === 'EXPENSE' ? 'Despesa' : 'Receita'}
                      </Badge>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex justify-end gap-2">
                        <Link
                          to={`/transactions/${transaction.id}`}
                          className="rounded-full border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-600"
                        >
                          Editar
                        </Link>
                        {canDelete ? (
                          <button
                            type="button"
                            className="rounded-full border border-red-200 px-3 py-1 text-xs font-semibold text-red-600 transition hover:bg-red-50"
                            onClick={() => handleDelete(transaction)}
                          >
                            Excluir
                          </button>
                        ) : null}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="space-y-4 lg:hidden">
            {transactions.map((transaction) => (
              <div
                key={transaction.id}
                className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm"
              >
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <p className="text-sm font-semibold text-slate-900">
                      {transaction.description}
                    </p>
                    <p className="text-xs text-slate-500">
                      {transaction.personName}
                    </p>
                  </div>
                  <Badge variant={transaction.type === 'EXPENSE' ? 'danger' : 'success'}>
                    {transaction.type === 'EXPENSE' ? 'Despesa' : 'Receita'}
                  </Badge>
                </div>
                <div className="mt-3 grid grid-cols-2 gap-3 text-xs text-slate-600">
                  <div>
                    <p className="font-semibold text-slate-500">Vencimento</p>
                    <p>{formatDate(transaction.dueDay)}</p>
                  </div>
                  <div>
                    <p className="font-semibold text-slate-500">Pagamento</p>
                    <p>{formatDate(transaction.payday)}</p>
                  </div>
                  <div className="col-span-2 text-sm font-semibold text-emerald-600">
                    {formatCurrencyBRL(transaction.value)}
                  </div>
                </div>
                <div className="mt-4 flex flex-wrap gap-2 text-xs font-semibold">
                  <Link
                    to={`/transactions/${transaction.id}`}
                    className="rounded-full bg-brand-50 px-3 py-1 text-brand-700"
                  >
                    Editar
                  </Link>
                  {canDelete ? (
                    <button
                      type="button"
                      className="rounded-full bg-red-100 px-3 py-1 text-red-700"
                      onClick={() => handleDelete(transaction)}
                    >
                      Excluir
                    </button>
                  ) : null}
                </div>
              </div>
            ))}
          </div>

          <Pagination
            page={page}
            size={PAGE_SIZE}
            total={total}
            onPageChange={(nextPage) => {
              setPage(nextPage)
              loadTransactions(nextPage, filters)
            }}
          />
        </div>
      )}
    </div>
  )
}
