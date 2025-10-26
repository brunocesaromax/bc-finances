import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import type { AxiosError } from 'axios'
import { personService } from '@/services/personService'
import type { PersonSummary } from '@/types/person'
import { Button } from '@/components/ui/Button'
import { Badge } from '@/components/ui/Badge'
import { EmptyState } from '@/components/ui/EmptyState'
import { Spinner } from '@/components/ui/Spinner'
import { Pagination } from '@/components/ui/Pagination'
import { useAuth } from '@/hooks/useAuth'
import { PERMISSIONS } from '@/utils/permissions'
import { FormLabel } from '@/components/ui/FormLabel'
import { Input } from '@/components/ui/Input'
import { useDebouncedValue } from '@/hooks/useDebouncedValue'

const PAGE_SIZE = 8

type FetchState = {
  content: PersonSummary[]
  totalElements: number
}

export const PersonsListPage = () => {
  const { hasPermission } = useAuth()
  const [nameFilter, setNameFilter] = useState('')
  const [appliedName, setAppliedName] = useState('')
  const [page, setPage] = useState(0)
  const [isLoading, setIsLoading] = useState(true)
  const [fetchState, setFetchState] = useState<FetchState>({
    content: [],
    totalElements: 0,
  })
  const [refreshKey, setRefreshKey] = useState(0)

  const canCreate = hasPermission(PERMISSIONS.CREATE_PERSON)

  const debouncedName = useDebouncedValue(nameFilter, 400)

  const normalizedName = useMemo(() => {
    const trimmed = debouncedName.trim()
    return trimmed.length >= 3 ? trimmed : ''
  }, [debouncedName])

  useEffect(() => {
    setAppliedName((previous) => {
      if (previous === normalizedName) {
        return previous
      }

      setPage(0)
      return normalizedName
    })
  }, [normalizedName])

  useEffect(() => {
    const controller = new AbortController()
    const fetchData = async () => {
      setIsLoading(true)
      try {
        const response = await personService.search(
          {
            page,
            size: PAGE_SIZE,
            name: appliedName || undefined,
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
        toast.error('Não foi possível carregar as pessoas cadastradas.')
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
  }, [appliedName, page, refreshKey])

  const handleDelete = async (person: PersonSummary) => {
    const confirmed = window.confirm(
      `Deseja realmente excluir "${person.name}"? Esta ação não poderá ser desfeita.`,
    )

    if (!confirmed) {
      return
    }

    try {
      await personService.delete(person.id)
      toast.success('Pessoa excluída com sucesso!')
      setRefreshKey((prev) => prev + 1)
    } catch (error) {
      toast.error('Não foi possível excluir a pessoa.')
    }
  }

  const handleChangeStatus = async (person: PersonSummary) => {
    try {
      await personService.changeStatus(person.id, person.active)
      toast.success(
        person.active
          ? 'Pessoa inativada com sucesso!'
          : 'Pessoa ativada com sucesso!',
      )
      setRefreshKey((prev) => prev + 1)
    } catch (error) {
      toast.error('Não foi possível atualizar o status da pessoa.')
    }
  }

  const handleResetFilters = () => {
    setNameFilter('')
    setAppliedName('')
    setPage(0)
  }

  const total = fetchState.totalElements
  const persons = fetchState.content

  return (
    <div className="space-y-6">
      <header className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900">Pessoas</h1>
          <p className="text-sm text-slate-500">
            Gerencie responsáveis e contatos vinculados aos lançamentos.
          </p>
        </div>
        {canCreate ? (
          <Link
            to="/persons/new"
            className="inline-flex items-center justify-center rounded-xl bg-brand-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-700 hover:text-white"
          >
            Nova pessoa
          </Link>
        ) : null}
      </header>

      <form
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm md:grid-cols-[1fr_auto]"
        onSubmit={(event) => {
          event.preventDefault()
          setPage(0)
        }}
      >
        <div>
          <FormLabel htmlFor="name">Nome</FormLabel>
          <Input
            id="name"
            placeholder="Pesquisar por nome"
            value={nameFilter}
            onChange={(event) => setNameFilter(event.target.value)}
          />
        </div>
        <div className="flex items-end gap-2">
          <Button type="submit" className="w-full md:w-auto">
            Filtrar
          </Button>
          <Button
            type="button"
            variant="ghost"
            onClick={handleResetFilters}
            className="w-full md:w-auto"
          >
            Limpar
          </Button>
        </div>
      </form>

      {isLoading ? (
        <div className="flex justify-center py-12">
          <Spinner className="h-8 w-8" />
        </div>
      ) : persons.length === 0 ? (
        <EmptyState
          title="Nenhuma pessoa localizada"
          description="Cadastre novas pessoas para começar a registrar transações."
        />
      ) : (
        <div className="space-y-6">
          <div className="hidden overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm md:block">
            <table className="min-w-full divide-y divide-slate-200 text-sm">
              <thead className="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                <tr>
                  <th className="px-6 py-3">Nome</th>
                  <th className="px-6 py-3">Status</th>
                  <th className="px-6 py-3 text-right">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200 text-sm text-slate-700">
                {persons.map((person) => (
                  <tr key={person.id}>
                    <td className="px-6 py-4 font-medium text-slate-900">
                      {person.name}
                    </td>
                    <td className="px-6 py-4">
                      <Badge variant={person.active ? 'success' : 'danger'}>
                        {person.active ? 'Ativa' : 'Inativa'}
                      </Badge>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex justify-end gap-2">
                        <Link
                          to={`/persons/${person.id}`}
                          className="cursor-pointer rounded-full border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-600"
                        >
                          Editar
                        </Link>
                        <button
                          type="button"
                          className="cursor-pointer rounded-full border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-600"
                          onClick={() => handleChangeStatus(person)}
                        >
                          {person.active ? 'Inativar' : 'Ativar'}
                        </button>
                        <button
                          type="button"
                          className="cursor-pointer rounded-full border border-red-200 px-3 py-1 text-xs font-semibold text-red-600 transition hover:bg-red-50"
                          onClick={() => handleDelete(person)}
                        >
                          Remover
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="space-y-4 md:hidden">
            {persons.map((person) => (
              <div
                key={person.id}
                className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm"
              >
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <p className="text-base font-semibold text-slate-900">
                      {person.name}
                    </p>
                    <Badge variant={person.active ? 'success' : 'danger'}>
                      {person.active ? 'Ativa' : 'Inativa'}
                    </Badge>
                  </div>
                  <div className="flex gap-2">
                    <Link
                      to={`/persons/${person.id}`}
                      className="cursor-pointer rounded-full bg-brand-50 px-3 py-1 text-xs font-semibold text-brand-700"
                    >
                      Editar
                    </Link>
                    <button
                      type="button"
                      className="cursor-pointer rounded-full bg-red-100 px-3 py-1 text-xs font-semibold text-red-700"
                      onClick={() => handleDelete(person)}
                    >
                      Remover
                    </button>
                  </div>
                </div>
                <button
                  type="button"
                  className="mt-3 cursor-pointer text-xs font-semibold text-slate-500 transition hover:text-brand-600"
                  onClick={() => handleChangeStatus(person)}
                >
                  {person.active ? 'Inativar' : 'Ativar'}
                </button>
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
