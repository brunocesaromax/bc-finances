import { useEffect, useMemo, useRef, useState } from 'react'
import { useFieldArray, useForm } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { Link, useNavigate, useParams } from 'react-router-dom'
import toast from 'react-hot-toast'
import { personService } from '@/services/personService'
import type { City, Person, State } from '@/types/person'
import { Input } from '@/components/ui/Input'
import { FormLabel } from '@/components/ui/FormLabel'
import { FormError } from '@/components/ui/FormError'
import { Button } from '@/components/ui/Button'
import { Select } from '@/components/ui/Select'
import { Spinner } from '@/components/ui/Spinner'

const contactSchema = z.object({
  id: z.number().optional(),
  name: z.string().min(1, 'Informe o nome do contato'),
  email: z.string().email('E-mail inválido'),
  phone: z.string().min(1, 'Informe o telefone'),
})

const personSchema = z.object({
  name: z.string().min(5, 'Informe o nome completo (mínimo 5 caracteres)'),
  street: z.string().min(1, 'Informe o logradouro'),
  number: z.string().optional(),
  complement: z.string().optional(),
  neighborhood: z.string().min(1, 'Informe o bairro'),
  zipCode: z.string().min(1, 'Informe o CEP'),
  stateId: z.string().min(1, 'Selecione o estado'),
  cityId: z.string().min(1, 'Selecione a cidade'),
  contacts: z
    .array(contactSchema)
    .min(1, 'Adicione pelo menos um contato para a pessoa'),
})

type PersonFormValues = z.infer<typeof personSchema>

const DEFAULT_VALUES: PersonFormValues = {
  name: '',
  street: '',
  number: '',
  complement: '',
  neighborhood: '',
  zipCode: '',
  stateId: '',
  cityId: '',
  contacts: [],
}

export const PersonFormPage = () => {
  const { id } = useParams<{ id: string }>()
  const isEditing = Boolean(id)
  const navigate = useNavigate()
  const [states, setStates] = useState<State[]>([])
  const [cities, setCities] = useState<City[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [personId, setPersonId] = useState<number | null>(null)
  const skipLoadCities = useRef(false)

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    control,
    formState: { errors },
  } = useForm<PersonFormValues>({
    resolver: zodResolver(personSchema),
    defaultValues: DEFAULT_VALUES,
  })

  const { fields, append, remove, replace } = useFieldArray({
    control,
    name: 'contacts',
  })

  const selectedStateId = watch('stateId')
  const createBlankContact = () => ({
    name: '',
    email: '',
    phone: '',
  })

  const fetchStates = useMemo(
    () =>
      async () => {
        try {
          const response = await personService.listStates()
          setStates(response)
        } catch (error) {
          toast.error('Não foi possível carregar os estados.')
        }
      },
    [],
  )

  const loadCities = useMemo(
    () =>
      async (stateId: number, currentCityId?: number) => {
        try {
          const response = await personService.listCities(stateId)
          setCities(response)

          if (!currentCityId) {
            setValue('cityId', '')
          }
        } catch (error) {
          toast.error('Não foi possível carregar as cidades para o estado selecionado.')
        }
      },
    [setValue],
  )

  useEffect(() => {
    fetchStates()
  }, [fetchStates])

  useEffect(() => {
    const loadPerson = async (personIdentifier: number) => {
      try {
        const person = await personService.findById(personIdentifier)
        hydrateForm(person)
      } catch (error) {
        toast.error('Não foi possível carregar os dados da pessoa.')
      } finally {
        setIsLoading(false)
      }
    }

    if (isEditing && id) {
      loadPerson(Number(id))
    } else {
      setIsLoading(false)
      replace([createBlankContact()])
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, isEditing])

  useEffect(() => {
    if (skipLoadCities.current) {
      skipLoadCities.current = false
      return
    }

    if (selectedStateId) {
      loadCities(Number(selectedStateId))
    } else {
      setCities([])
      setValue('cityId', '')
    }
  }, [loadCities, selectedStateId, setValue])

  const hydrateForm = (person: Person) => {
    setPersonId(person.id ?? null)
    setValue('name', person.name)
    setValue('street', person.address.street)
    setValue('number', person.address.number ?? '')
    setValue('complement', person.address.complement ?? '')
    setValue('neighborhood', person.address.neighborhood)
    setValue('zipCode', person.address.zipCode)

    const stateId = person.address.city?.state?.id
    const cityId = person.address.city?.id

    if (stateId) {
      skipLoadCities.current = true
      setValue('stateId', stateId.toString())
      loadCities(stateId, cityId ?? undefined).then(() => {
        if (cityId) {
          setValue('cityId', cityId.toString())
        }
      })
    }

    if (person.contacts?.length) {
      replace(person.contacts)
    } else {
      replace([createBlankContact()])
    }
  }

  const onSubmit = handleSubmit(async (values) => {
    if (!values.stateId || !values.cityId) {
      return
    }

    setIsSubmitting(true)

    const payload: Person = {
      id: personId ?? undefined,
      name: values.name,
      active: true,
      address: {
        street: values.street,
        number: values.number ?? '',
        complement: values.complement ?? '',
        neighborhood: values.neighborhood,
        zipCode: values.zipCode,
        city: {
          id: Number(values.cityId),
          name: '',
          state: { id: Number(values.stateId), name: '' },
        },
      },
      contacts: values.contacts.map((contact) => ({
        id: contact.id,
        name: contact.name,
        email: contact.email,
        phone: contact.phone,
      })),
    }

    try {
      if (isEditing) {
        await personService.update(payload)
        toast.success('Pessoa atualizada com sucesso!')
      } else {
        const saved = await personService.save(payload)
        toast.success('Pessoa cadastrada com sucesso!')
        navigate(`/persons/${saved.id}`)
        return
      }

      navigate('/persons')
    } catch (error) {
      toast.error('Não foi possível salvar os dados da pessoa.')
    } finally {
      setIsSubmitting(false)
    }
  })

  if (isLoading) {
    return (
      <div className="flex justify-center py-12">
        <Spinner className="h-8 w-8" />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <header className="flex flex-col gap-2">
        <h1 className="text-2xl font-semibold text-slate-900">
          {isEditing ? 'Editar pessoa' : 'Nova pessoa'}
        </h1>
        <p className="text-sm text-slate-500">
          Organize informações cadastrais e contatos principais.
        </p>
      </header>

      <form className="space-y-6" onSubmit={onSubmit} noValidate>
        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">Dados pessoais</h2>
          <div className="mt-4 grid gap-4 md:grid-cols-2">
            <div className="md:col-span-2">
              <FormLabel htmlFor="name">Nome</FormLabel>
              <Input id="name" hasError={Boolean(errors.name)} {...register('name')} />
              {errors.name ? <FormError>{errors.name.message}</FormError> : null}
            </div>

            <div>
              <FormLabel htmlFor="street">Logradouro</FormLabel>
              <Input
                id="street"
                hasError={Boolean(errors.street)}
                {...register('street')}
              />
              {errors.street ? <FormError>{errors.street.message}</FormError> : null}
            </div>
            <div>
              <FormLabel htmlFor="number">Número</FormLabel>
              <Input id="number" {...register('number')} />
            </div>
            <div>
              <FormLabel htmlFor="complement">Complemento</FormLabel>
              <Input id="complement" {...register('complement')} />
            </div>
            <div>
              <FormLabel htmlFor="neighborhood">Bairro</FormLabel>
              <Input
                id="neighborhood"
                hasError={Boolean(errors.neighborhood)}
                {...register('neighborhood')}
              />
              {errors.neighborhood ? (
                <FormError>{errors.neighborhood.message}</FormError>
              ) : null}
            </div>
            <div>
              <FormLabel htmlFor="zipCode">CEP</FormLabel>
              <Input
                id="zipCode"
                placeholder="00000-000"
                hasError={Boolean(errors.zipCode)}
                {...register('zipCode')}
              />
              {errors.zipCode ? <FormError>{errors.zipCode.message}</FormError> : null}
            </div>
            <div>
              <FormLabel htmlFor="stateId">Estado</FormLabel>
              <Select
                id="stateId"
                hasError={Boolean(errors.stateId)}
                value={selectedStateId}
                {...register('stateId')}
              >
                <option value="">Selecione...</option>
                {states.map((state) => (
                  <option key={state.id} value={state.id}>
                    {state.name}
                  </option>
                ))}
              </Select>
              {errors.stateId ? <FormError>{errors.stateId.message}</FormError> : null}
            </div>
            <div>
              <FormLabel htmlFor="cityId">Cidade</FormLabel>
              <Select
                id="cityId"
                hasError={Boolean(errors.cityId)}
                {...register('cityId')}
              >
                <option value="">Selecione...</option>
                {cities.map((city) => (
                  <option key={city.id} value={city.id}>
                    {city.name}
                  </option>
                ))}
              </Select>
              {errors.cityId ? <FormError>{errors.cityId.message}</FormError> : null}
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-lg font-semibold text-slate-900">Contatos</h2>
              <p className="text-sm text-slate-500">
                Adicione canais de comunicação relevantes para este cadastro.
              </p>
            </div>
            <Button
              type="button"
              variant="outline"
              onClick={() => append(createBlankContact())}
            >
              Novo contato
            </Button>
          </div>

          {errors.contacts?.root ? (
            <FormError className="mt-3">
              {errors.contacts.root.message}
            </FormError>
          ) : null}

          <div className="mt-6 space-y-4">
            {fields.map((field, index) => (
              <div
                key={field.id}
                className="rounded-2xl border border-slate-200 bg-slate-50 p-4"
              >
                <div className="flex items-center justify-between">
                  <p className="text-sm font-semibold text-slate-700">
                    Contato {index + 1}
                  </p>
                  {fields.length > 1 ? (
                    <button
                      type="button"
                      className="text-xs font-semibold text-red-600"
                      onClick={() => remove(index)}
                    >
                      Remover
                    </button>
                  ) : null}
                </div>

                <div className="mt-4 grid gap-3 md:grid-cols-3">
                  <div className="md:col-span-1">
                    <FormLabel htmlFor={`contacts.${index}.name`}>
                      Nome
                    </FormLabel>
                    <Input
                      id={`contacts.${index}.name`}
                      hasError={Boolean(errors.contacts?.[index]?.name)}
                      {...register(`contacts.${index}.name` as const)}
                    />
                    {errors.contacts?.[index]?.name ? (
                      <FormError>
                        {errors.contacts[index]?.name?.message}
                      </FormError>
                    ) : null}
                  </div>
                  <div className="md:col-span-1">
                    <FormLabel htmlFor={`contacts.${index}.email`}>
                      E-mail
                    </FormLabel>
                    <Input
                      id={`contacts.${index}.email`}
                      hasError={Boolean(errors.contacts?.[index]?.email)}
                      {...register(`contacts.${index}.email` as const)}
                    />
                    {errors.contacts?.[index]?.email ? (
                      <FormError>
                        {errors.contacts[index]?.email?.message}
                      </FormError>
                    ) : null}
                  </div>
                  <div className="md:col-span-1">
                    <FormLabel htmlFor={`contacts.${index}.phone`}>
                      Telefone
                    </FormLabel>
                    <Input
                      id={`contacts.${index}.phone`}
                      placeholder="(00) 00000-0000"
                      hasError={Boolean(errors.contacts?.[index]?.phone)}
                      {...register(`contacts.${index}.phone` as const)}
                    />
                    {errors.contacts?.[index]?.phone ? (
                      <FormError>
                        {errors.contacts[index]?.phone?.message}
                      </FormError>
                    ) : null}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Link
            to="/persons"
            className="inline-flex items-center justify-center rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-600 transition hover:bg-slate-100"
          >
            Cancelar
          </Link>
          <Button type="submit" loading={isSubmitting} disabled={isSubmitting}>
            Salvar pessoa
          </Button>
        </div>
      </form>
    </div>
  )
}
