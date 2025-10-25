import { apiClient } from '@/services/apiClient'
import type {
  Person,
  PersonFilter,
  PersonSummary,
  PersonMinimal,
  State,
  City,
} from '@/types/person'
import type { PageableResponse } from '@/types/common'

export const personService = {
  async search(
    filter: PersonFilter,
  ): Promise<PageableResponse<PersonSummary>> {
    const params: Record<string, string> = {
      page: filter.page.toString(),
      size: filter.size.toString(),
    }

    if (filter.name) {
      params.name = filter.name
    }

    const { data } = await apiClient.get<PageableResponse<PersonSummary>>(
      '/persons',
      {
        params: {
          ...params,
          pagination: '',
        },
      },
    )

    return data
  },

  async findById(id: number): Promise<Person> {
    const { data } = await apiClient.get<Person>(`/persons/${id}`)
    return data
  },

  async save(person: Person): Promise<Person> {
    const { data } = await apiClient.post<Person>('/persons', person)
    return data
  },

  async update(person: Person): Promise<Person> {
    if (!person.id) {
      throw new Error('Person id is required to update')
    }
    const { data } = await apiClient.put<Person>(
      `/persons/${person.id}`,
      person,
    )
    return data
  },

  async delete(id: number): Promise<void> {
    await apiClient.delete(`/persons/${id}`)
  },

  async changeStatus(id: number, active: boolean): Promise<void> {
    await apiClient.put(`/persons/${id}/active`, !active, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
  },

  async findAll(): Promise<PersonMinimal[]> {
    const { data } = await apiClient.get<PersonMinimal[]>('/persons')
    return data
  },

  async listStates(): Promise<State[]> {
    const { data } = await apiClient.get<State[]>('/states')
    return data
  },

  async listCities(stateId: number): Promise<City[]> {
    const { data } = await apiClient.get<City[]>('/cities', {
      params: { stateId },
    })
    return data
  },
}
