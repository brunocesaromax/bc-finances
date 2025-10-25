import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import { personService } from '@/services/personService'
import type { PersonSummary } from '@/types/person'
import { Input } from '@/components/ui/Input'
import { FormLabel } from '@/components/ui/FormLabel'
import { Button } from '@/components/ui/Button'
import { Badge } from '@/components/ui/Badge'
import { EmptyState } from '@/components/ui/EmptyState'
import { Spinner } from '@/components/ui/Spinner'
import { Pagination } from '@/components/ui/Pagination'
import { useAuth } from '@/hooks/useAuth'
import { PERMISSIONS } from '@/utils/permissions'

const PAGE_SIZE = 8

type FetchState = {
  content: PersonSummary[]
  totalElements: number
}

export const PersonsListPage = () => {
  const { hasPermission } = useAuth()
  const [filterName, setFilterName] = useState('')
  const [page, setPage] = useState(0)
  const [isLoading, setIsLoading] = useState(true)
  const [fetchState, setFetchState] = useState<FetchState>({
    content: [],
    totalElements: 0,
  })

  const canCreate = hasPermission(PERMISSIONS.CREATE_PERSON)

  const loadPersons = useMemo(
    () =>
      async (pageIndex: number, nameFilter: string) => {
        setIsLoading(true)
        try {
          const response = await personService.search({
            page: pageIndex,
            size: PAGE_SIZE,
            name: nameFilter || undefined,
          })

          setFetchState({
            content: response.content,
            totalElements: response.totalElements,
          })
        } catch (error) {
          toast.error('Não foi possível carregar as pessoas cadastradas.')
        } finally {
          setIsLoading(false)
        }
      },
    [],
  )

  useEffect(() => {
    loadPersons(page, filterName)
  }, [filterName, loadPersons, page])

  const handleSearch = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setPage(0)
    loadPersons(0, filterName)
  }

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
      loadPersons(page, filterName)
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
      loadPersons(page, filterName)
    } catch (error) {
      toast.error('Não foi possível atualizar o status da pessoa.')
    }
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
            className="inline-flex items-center justify-center rounded-xl bg-brand-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-700"
          >
            Nova pessoa
          </Link>
        ) : null}
      </header>

      <form
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm md:grid-cols-[1fr_auto]"
        onSubmit={handleSearch}
      >
        <div>
          <FormLabel htmlFor="name">Nome</FormLabel>
          <Input
            id="name"
            placeholder="Pesquisar por nome"
            value={filterName}
            onChange={(event) => setFilterName(event.target.value)}
          />
        </div>
        <div className="flex items-end">
          <Button type="submit" className="w-full md:w-auto">
            Filtrar
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
                  <th className="px-6 py-3">Cidade</th>
                  <th className="px-6 py-3">Estado</th>
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
                    <td className="px-6 py-4">{person.city || '—'}</td>
                    <td className="px-6 py-4">{person.state || '—'}</td>
                    <td className="px-6 py-4">
                      <Badge variant={person.active ? 'success' : 'danger'}>
                        {person.active ? 'Ativa' : 'Inativa'}
                      </Badge>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex justify-end gap-2">
                        <Link
                          to={`/persons/${person.id}`}
                          className="rounded-full border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-600"
                        >
                          Editar
                        </Link>
                        <button
                          type="button"
                          className="rounded-full border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-600"
                          onClick={() => handleChangeStatus(person)}
                        >
                          {person.active ? 'Inativar' : 'Ativar'}
                        </button>
                        <button
                          type="button"
                          className="rounded-full border border-red-200 px-3 py-1 text-xs font-semibold text-red-600 transition hover:bg-red-50"
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
                <div className="flex items-start justify-between">
                  <div>
                    <p className="text-base font-semibold text-slate-900">
                      {person.name}
                    </p>
                    <p className="text-xs text-slate-500">
                      {person.city || 'Cidade não informada'}
                    </p>
                  </div>
                  <Badge variant={person.active ? 'success' : 'danger'}>
                    {person.active ? 'Ativa' : 'Inativa'}
                  </Badge>
                </div>

                <div className="mt-4 flex flex-wrap gap-2 text-xs font-medium">
                  <Link
                    to={`/persons/${person.id}`}
                    className="rounded-full bg-brand-50 px-3 py-1 text-brand-700"
                  >
                    Editar
                  </Link>
                  <button
                    type="button"
                    className="rounded-full bg-slate-100 px-3 py-1 text-slate-700"
                    onClick={() => handleChangeStatus(person)}
                  >
                    {person.active ? 'Inativar' : 'Ativar'}
                  </button>
                  <button
                    type="button"
                    className="rounded-full bg-red-100 px-3 py-1 text-red-700"
                    onClick={() => handleDelete(person)}
                  >
                    Remover
                  </button>
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
              loadPersons(nextPage, filterName)
            }}
          />
        </div>
      )}
    </div>
  )
}
