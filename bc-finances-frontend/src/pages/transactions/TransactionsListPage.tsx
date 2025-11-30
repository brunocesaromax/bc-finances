import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import type { AxiosError } from 'axios'
import { transactionService } from '@/services/transactionService'
import { categoryService } from '@/services/categoryService'
import { tagService } from '@/services/tagService'
import type { TransactionSummary } from '@/types/transaction'
import type { TransactionType } from '@/types/finance'
import type { Category } from '@/types/category'
import type { Tag } from '@/types/tag'
import { Button } from '@/components/ui/Button'
import { Badge } from '@/components/ui/Badge'
import { EmptyState } from '@/components/ui/EmptyState'
import { Spinner } from '@/components/ui/Spinner'
import { Pagination } from '@/components/ui/Pagination'
import { formatCurrencyBRL, formatDate } from '@/utils/formatters'
import { useAuth } from '@/hooks/useAuth'
import { PERMISSIONS } from '@/utils/permissions'
import { FormLabel } from '@/components/ui/FormLabel'
import { Input } from '@/components/ui/Input'
import { DatePicker } from '@/components/ui/DatePicker'
import { Select } from '@/components/ui/Select'

const PAGE_SIZE = 10

type FilterFormState = {
  description: string
  dueDayStart: string | null
  dueDayEnd: string | null
  type: TransactionType | ''
  categoryId: string
  tags: string[]
}

type FetchState = {
  content: TransactionSummary[]
  totalElements: number
}

const truncateText = (value: string, maxChars: number) =>
  value.length > maxChars ? `${value.slice(0, maxChars).trimEnd()}…` : value

export const TransactionsListPage = () => {
  const { hasPermission } = useAuth()
  const [filterForm, setFilterForm] = useState<FilterFormState>({
    description: '',
    dueDayStart: null,
    dueDayEnd: null,
    type: '',
    categoryId: '',
    tags: [],
  })
  const [appliedFilters, setAppliedFilters] = useState<FilterFormState>({
    description: '',
    dueDayStart: null,
    dueDayEnd: null,
    type: '',
    categoryId: '',
    tags: [],
  })
  const [page, setPage] = useState(0)
  const [isLoading, setIsLoading] = useState(true)
  const [fetchState, setFetchState] = useState<FetchState>({
    content: [],
    totalElements: 0,
  })
  const [refreshKey, setRefreshKey] = useState(0)
  const [categories, setCategories] = useState<Category[]>([])
  const [availableTags, setAvailableTags] = useState<Tag[]>([])
  const [isLoadingTags, setIsLoadingTags] = useState(false)

  const canCreate = hasPermission(PERMISSIONS.CREATE_TRANSACTION)
  const canDelete = hasPermission(PERMISSIONS.REMOVE_TRANSACTION)

  useEffect(() => {
    if (!filterForm.type) {
      setCategories([])
      return
    }

    const fetchCategories = async () => {
      try {
        const response = await categoryService.findAll(
          filterForm.type || undefined,
        )
        setCategories(response)
      } catch (error) {
        console.error('Não foi possível carregar categorias.', error)
        toast.error('Não foi possível carregar categorias.')
      }
    }

    fetchCategories()
  }, [filterForm.type])

  useEffect(() => {
    const fetchTags = async () => {
      setIsLoadingTags(true)
      try {
        const response = await tagService.findAll()
        setAvailableTags(response)
      } catch (error) {
        console.error('Não foi possível carregar tags.', error)
        toast.error('Não foi possível carregar tags.')
      } finally {
        setIsLoadingTags(false)
      }
    }

    fetchTags()
  }, [])

  useEffect(() => {
    const controller = new AbortController()
    const fetchData = async () => {
      setIsLoading(true)
      try {
        const response = await transactionService.search(
          {
            page,
            size: PAGE_SIZE,
            description: appliedFilters.description || undefined,
            dueDayStart: appliedFilters.dueDayStart ?? undefined,
            dueDayEnd: appliedFilters.dueDayEnd ?? undefined,
            type: appliedFilters.type || undefined,
            categoryId: appliedFilters.categoryId
              ? Number(appliedFilters.categoryId)
              : undefined,
            tags:
              appliedFilters.tags && appliedFilters.tags.length > 0
                ? appliedFilters.tags
                : undefined,
          },
          { signal: controller.signal },
        )

        setFetchState({
          content: response.content,
          totalElements: response.totalElements,
        })
      } catch (error) {
        const axiosError = error as AxiosError
        if (axiosError?.code === 'ERR_CANCELED') {
          return
        }
        console.error('Não foi possível carregar os lançamentos.', error)
        toast.error('Não foi possível carregar os lançamentos.')
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false)
        }
      }
    }

    fetchData()

    return () => {
      controller.abort()
    }
  }, [
    appliedFilters.description,
    appliedFilters.dueDayEnd,
    appliedFilters.dueDayStart,
    appliedFilters.type,
    appliedFilters.categoryId,
    appliedFilters.tags,
    page,
    refreshKey,
  ])

  const handleDescriptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target
    setFilterForm((prev) => ({ ...prev, description: value }))
  }

  const handleTypeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const nextType = event.target.value as TransactionType | ''
    setFilterForm((prev) => ({
      ...prev,
      type: nextType,
      categoryId: '',
    }))
  }

  const handleCategoryChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setFilterForm((prev) => ({
      ...prev,
      categoryId: event.target.value,
    }))
  }

  const handleTagToggle = (tagName: string) => {
    setFilterForm((prev) => {
      const isSelected = prev.tags.includes(tagName)
      return {
        ...prev,
        tags: isSelected
          ? prev.tags.filter((tag) => tag !== tagName)
          : [...prev.tags, tagName],
      }
    })
  }

  const handleFilterSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const normalizedDescription = filterForm.description.trim()
    const descriptionFilter =
      normalizedDescription.length > 0 ? normalizedDescription : ''
    setPage(0)
    setAppliedFilters((prev) => ({
      ...prev,
      description: descriptionFilter,
      dueDayStart: filterForm.dueDayStart,
      dueDayEnd: filterForm.dueDayEnd,
      type: filterForm.type,
      categoryId: filterForm.categoryId,
      tags: [...filterForm.tags],
    }))
  }

  const handleResetFilters = () => {
    setFilterForm({
      description: '',
      dueDayStart: null,
      dueDayEnd: null,
      type: '',
      categoryId: '',
      tags: [],
    })
    setAppliedFilters({
      description: '',
      dueDayStart: null,
      dueDayEnd: null,
      type: '',
      categoryId: '',
      tags: [],
    })
    setPage(0)
    setCategories([])
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
      setRefreshKey((prev) => prev + 1)
    } catch (error) {
      console.error('Não foi possível remover o lançamento.', error)
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
            className="inline-flex items-center justify-center rounded-xl bg-brand-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-700 hover:text-white"
          >
            Novo lançamento
          </Link>
        ) : null}
      </header>

      <form
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm md:grid-cols-6"
        onSubmit={handleFilterSubmit}
      >
        <div className="md:col-span-2">
          <FormLabel htmlFor="description">Descrição</FormLabel>
          <Input
            id="description"
            name="description"
            placeholder="Buscar por descrição"
            value={filterForm.description}
            onChange={handleDescriptionChange}
          />
        </div>
        <div>
          <FormLabel htmlFor="type">Tipo</FormLabel>
          <Select
            id="type"
            value={filterForm.type}
            onChange={handleTypeChange}
          >
            <option value="">Todos</option>
            <option value="RECIPE">Receita</option>
            <option value="EXPENSE">Despesa</option>
          </Select>
        </div>
        <div>
          <FormLabel htmlFor="category">Categoria</FormLabel>
          <Select
            id="category"
            value={filterForm.categoryId}
            onChange={handleCategoryChange}
            disabled={!filterForm.type}
          >
            <option value="">
              {filterForm.type ? 'Todas' : 'Selecione o tipo'}
            </option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </Select>
        </div>
        <div className="md:col-span-2">
          <FormLabel htmlFor="tags-filter">Tags</FormLabel>
          <div
            id="tags-filter"
            className="flex min-h-[42px] flex-wrap items-center gap-2 rounded-2xl border border-slate-200 bg-slate-50 p-2"
          >
            {isLoadingTags ? (
              <span className="text-xs text-slate-500">Carregando tags...</span>
            ) : availableTags.length === 0 ? (
              <span className="text-xs text-slate-500">Nenhuma tag disponível</span>
            ) : (
              availableTags.map((tag) => {
                const isSelected = filterForm.tags.includes(tag.name)
                return (
                  <button
                    key={tag.id}
                    type="button"
                    aria-pressed={isSelected}
                    className={`rounded-full border px-3 py-1 text-xs font-semibold transition ${
                      isSelected
                        ? 'border-brand-300 bg-brand-50 text-brand-700 shadow-sm'
                        : 'border-slate-200 bg-white text-slate-600 hover:border-brand-200 hover:text-brand-700'
                    }`}
                    onClick={() => handleTagToggle(tag.name)}
                  >
                    {tag.name}
                  </button>
                )
              })
            )}
          </div>
        </div>
        <div>
          <FormLabel htmlFor="dueDayStart">Vencimento inicial</FormLabel>
          <DatePicker
            id="dueDayStart"
            value={filterForm.dueDayStart}
            onChange={(value) =>
              setFilterForm((prev) => ({ ...prev, dueDayStart: value }))
            }
            className="max-w-full"
          />
        </div>
        <div>
          <FormLabel htmlFor="dueDayEnd">Vencimento final</FormLabel>
          <DatePicker
            id="dueDayEnd"
            value={filterForm.dueDayEnd}
            onChange={(value) =>
              setFilterForm((prev) => ({ ...prev, dueDayEnd: value }))
            }
            className="max-w-full"
          />
        </div>
        <div className="md:col-span-6 flex flex-col justify-end gap-2 md:flex-row">
          <Button type="submit" className="w-full md:w-auto">
            Aplicar filtros
          </Button>
          <Button
            type="button"
            variant="ghost"
            className="w-full md:w-auto"
            onClick={handleResetFilters}
          >
            Limpar
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
                  <th className="px-6 py-3">Descrição</th>
                  <th className="px-6 py-3">Categoria</th>
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
                    <td className="px-6 py-4">
                      <span
                        className="line-clamp-1"
                        title={transaction.description}
                      >
                        {truncateText(transaction.description, 25)}
                      </span>
                      <div className="mt-1 flex flex-wrap gap-1">
                        {transaction.tags?.map((tag) => (
                          <Badge key={tag} variant="primary">
                            {tag}
                          </Badge>
                        ))}
                        {transaction.hasAttachments ? (
                          <Badge variant="info">Anexos</Badge>
                        ) : null}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-800">
                      {transaction.categoryName ?? '—'}
                    </td>
                    <td className="px-6 py-4">{formatDate(transaction.dueDay)}</td>
                    <td className="px-6 py-4">{formatDate(transaction.payday)}</td>
                    <td className="px-6 py-4 font-semibold">
                      <span
                        className={
                          transaction.type === 'EXPENSE'
                            ? 'text-red-600'
                            : 'text-emerald-600'
                        }
                      >
                        {formatCurrencyBRL(transaction.value)}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <Badge
                        variant={
                          transaction.type === 'EXPENSE' ? 'danger' : 'success'
                        }
                      >
                        {transaction.type === 'EXPENSE' ? 'Despesa' : 'Receita'}
                      </Badge>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex justify-end gap-2">
                        <Link
                          to={`/transactions/${transaction.id}`}
                          className="cursor-pointer rounded-full border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-600"
                        >
                          Editar
                        </Link>
                        {canDelete ? (
                          <button
                            type="button"
                            className="cursor-pointer rounded-full border border-red-200 px-3 py-1 text-xs font-semibold text-red-600 transition hover:bg-red-50"
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
                      <span title={transaction.description}>
                        {truncateText(transaction.description, 25)}
                      </span>
                    </p>
                    <p className="text-xs text-slate-500">
                      {transaction.categoryName ?? 'Sem categoria'}
                    </p>
                    <div className="mt-2 flex flex-wrap gap-1">
                      {transaction.tags?.map((tag) => (
                        <Badge key={tag} variant="primary">
                          {tag}
                        </Badge>
                      ))}
                      {transaction.hasAttachments ? (
                        <Badge variant="info">Anexos</Badge>
                      ) : null}
                    </div>
                  </div>
                  <Badge
                    variant={
                      transaction.type === 'EXPENSE' ? 'danger' : 'success'
                    }
                  >
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
                  <div
                    className={`col-span-2 text-sm font-semibold ${
                      transaction.type === 'EXPENSE'
                        ? 'text-red-600'
                        : 'text-emerald-600'
                    }`}
                  >
                    {formatCurrencyBRL(transaction.value)}
                  </div>
                </div>
                <div className="mt-4 flex flex-wrap gap-2 text-xs font-semibold">
                  <Link
                    to={`/transactions/${transaction.id}`}
                    className="cursor-pointer rounded-full bg-brand-50 px-3 py-1 text-brand-700"
                  >
                    Editar
                  </Link>
                  {canDelete ? (
                    <button
                      type="button"
                      className="cursor-pointer rounded-full bg-red-100 px-3 py-1 text-red-700"
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
            }}
          />
        </div>
      )}
    </div>
  )
}
