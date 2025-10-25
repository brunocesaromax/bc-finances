import { useEffect, useMemo, useState } from 'react'
import { useForm, type Resolver } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { Link, useNavigate, useParams } from 'react-router-dom'
import toast from 'react-hot-toast'
import { transactionService } from '@/services/transactionService'
import { categoryService } from '@/services/categoryService'
import { personService } from '@/services/personService'
import type { Category } from '@/types/category'
import type { PersonMinimal } from '@/types/person'
import type { Transaction } from '@/types/transaction'
import { Input } from '@/components/ui/Input'
import { FormLabel } from '@/components/ui/FormLabel'
import { FormError } from '@/components/ui/FormError'
import { Select } from '@/components/ui/Select'
import { Textarea } from '@/components/ui/Textarea'
import { Button } from '@/components/ui/Button'
import { Spinner } from '@/components/ui/Spinner'
import { formatCurrencyBRL, parseDateInputValue } from '@/utils/formatters'

const transactionSchema = z.object({
  description: z.string().min(5, 'Informe a descrição (mínimo 5 caracteres)'),
  value: z.coerce
    .number()
    .refine((val) => !Number.isNaN(val), 'Informe o valor')
    .refine((val) => val > 0, 'O valor deve ser maior que zero'),
  type: z.enum(['RECIPE', 'EXPENSE']),
  dueDay: z.string().min(1, 'Informe a data de vencimento'),
  payday: z.string().optional(),
  personId: z.string().min(1, 'Selecione a pessoa'),
  categoryId: z.string().min(1, 'Selecione a categoria'),
  observation: z.string().optional(),
  attachment: z.string().nullable().optional(),
  urlAttachment: z.string().nullable().optional(),
})

type TransactionFormValues = z.infer<typeof transactionSchema>

const DEFAULT_VALUES: TransactionFormValues = {
  description: '',
  value: 0,
  type: 'RECIPE',
  dueDay: '',
  payday: '',
  personId: '',
  categoryId: '',
  observation: '',
  attachment: null,
  urlAttachment: null,
}

export const TransactionFormPage = () => {
  const { id } = useParams<{ id: string }>()
  const isEditing = Boolean(id)
  const navigate = useNavigate()
  const [categories, setCategories] = useState<Category[]>([])
  const [persons, setPersons] = useState<PersonMinimal[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [uploadingAttachment, setUploadingAttachment] = useState(false)
  const [transactionId, setTransactionId] = useState<number | null>(null)

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm<TransactionFormValues>({
    resolver: zodResolver(transactionSchema) as Resolver<TransactionFormValues>,
    defaultValues: DEFAULT_VALUES,
  })

  const currentValue = watch('value')

  const loadResources = useMemo(
    () =>
      async () => {
        try {
          const [categoriesResponse, personsResponse] = await Promise.all([
            categoryService.findAll(),
            personService.findAll(),
          ])
          setCategories(categoriesResponse)
          setPersons(personsResponse)
        } catch (error) {
          toast.error('Não foi possível carregar categorias e pessoas.')
        }
      },
    [],
  )

  useEffect(() => {
    const initialize = async () => {
      await loadResources()

      if (isEditing && id) {
        try {
          const transaction = await transactionService.findById(Number(id))
          hydrateForm(transaction)
        } catch (error) {
          toast.error('Não foi possível carregar o lançamento.')
        }
      }

      setIsLoading(false)
    }

    initialize()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, isEditing, loadResources])

  const hydrateForm = (transaction: Transaction) => {
    setTransactionId(transaction.id ?? null)
    reset({
      description: transaction.description,
      value: transaction.value,
      type: transaction.type,
      dueDay: parseDateInputValue(transaction.dueDay) ?? '',
      payday: parseDateInputValue(transaction.payday) ?? '',
      personId: transaction.person.id?.toString() ?? '',
      categoryId: transaction.category.id?.toString() ?? '',
      observation: transaction.observation ?? '',
      attachment: transaction.attachment ?? null,
      urlAttachment: transaction.urlAttachment ?? null,
    })
  }

  const onSubmit = handleSubmit(async (values) => {
    setIsSubmitting(true)

    const payload: Transaction = {
      id: transactionId ?? undefined,
      description: values.description,
      value: Number(values.value),
      type: values.type,
      dueDay: values.dueDay,
      payday: values.payday || null,
      observation: values.observation ?? '',
      person: {
        id: Number(values.personId),
      },
      category: {
        id: Number(values.categoryId),
      },
      attachment: values.attachment ?? null,
      urlAttachment: values.urlAttachment ?? null,
    }

    try {
      if (isEditing) {
        await transactionService.update(payload)
        toast.success('Lançamento atualizado com sucesso!')
        navigate('/transactions')
      } else {
        const saved = await transactionService.save(payload)
        toast.success('Lançamento criado com sucesso!')
        navigate(`/transactions/${saved.id}`)
      }
    } catch (error) {
      toast.error('Não foi possível salvar o lançamento.')
    } finally {
      setIsSubmitting(false)
    }
  })

  const handleAttachmentUpload = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files?.[0]
    if (!file) {
      return
    }

    setUploadingAttachment(true)

    try {
      const response = await transactionService.uploadAttachment(file)
      setValue('attachment', response.name)
      setValue('urlAttachment', response.url)
      toast.success('Anexo enviado com sucesso!')
    } catch (error) {
      toast.error('Não foi possível enviar o anexo.')
    } finally {
      setUploadingAttachment(false)
      event.target.value = ''
    }
  }

  const handleRemoveAttachment = () => {
    setValue('attachment', null)
    setValue('urlAttachment', null)
  }

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
          {isEditing ? 'Editar lançamento' : 'Novo lançamento'}
        </h1>
        <p className="text-sm text-slate-500">
          Controle os fluxos de caixa e acompanhe anexos relacionados a cada transação.
        </p>
      </header>

      <form className="space-y-6" onSubmit={onSubmit} noValidate>
        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">Informações básicas</h2>
          <div className="mt-4 grid gap-4 md:grid-cols-2">
            <div className="md:col-span-2">
              <FormLabel htmlFor="description">Descrição</FormLabel>
              <Input
                id="description"
                hasError={Boolean(errors.description)}
                {...register('description')}
              />
              {errors.description ? (
                <FormError>{errors.description.message}</FormError>
              ) : null}
            </div>

            <div>
              <FormLabel htmlFor="value">Valor</FormLabel>
              <Input
                id="value"
                type="number"
                step="0.01"
                min="0"
                hasError={Boolean(errors.value)}
                {...register('value', { valueAsNumber: true })}
              />
              {errors.value ? <FormError>{errors.value.message}</FormError> : null}
              <p className="mt-1 text-xs text-slate-400">
                Valor atual: {formatCurrencyBRL(Number(currentValue) || 0)}
              </p>
            </div>

            <div>
              <FormLabel htmlFor="type">Tipo</FormLabel>
              <Select id="type" {...register('type')}>
                <option value="RECIPE">Receita</option>
                <option value="EXPENSE">Despesa</option>
              </Select>
            </div>

            <div>
              <FormLabel htmlFor="dueDay">Data de vencimento</FormLabel>
              <Input
                id="dueDay"
                type="date"
                hasError={Boolean(errors.dueDay)}
                {...register('dueDay')}
              />
              {errors.dueDay ? <FormError>{errors.dueDay.message}</FormError> : null}
            </div>
            <div>
              <FormLabel htmlFor="payday">Data de pagamento</FormLabel>
              <Input id="payday" type="date" {...register('payday')} />
            </div>

            <div>
              <FormLabel htmlFor="personId">Pessoa</FormLabel>
              <Select
                id="personId"
                hasError={Boolean(errors.personId)}
                {...register('personId')}
              >
                <option value="">Selecione...</option>
                {persons.map((person) => (
                  <option key={person.id} value={person.id}>
                    {person.name}
                  </option>
                ))}
              </Select>
              {errors.personId ? <FormError>{errors.personId.message}</FormError> : null}
            </div>

            <div>
              <FormLabel htmlFor="categoryId">Categoria</FormLabel>
              <Select
                id="categoryId"
                hasError={Boolean(errors.categoryId)}
                {...register('categoryId')}
              >
                <option value="">Selecione...</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </Select>
              {errors.categoryId ? (
                <FormError>{errors.categoryId.message}</FormError>
              ) : null}
            </div>

            <div className="md:col-span-2">
              <FormLabel htmlFor="observation">Observações</FormLabel>
              <Textarea id="observation" rows={3} {...register('observation')} />
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">Anexo</h2>
          <p className="mt-1 text-sm text-slate-500">
            Inclua comprovantes ou notas fiscais relacionados ao lançamento.
          </p>

          <div className="mt-4 flex flex-col gap-3 md:flex-row md:items-center">
            <label className="inline-flex cursor-pointer items-center gap-2 rounded-xl border border-dashed border-brand-300 bg-brand-50 px-4 py-2 text-sm font-semibold text-brand-700 shadow-sm transition hover:bg-brand-100">
              <input
                type="file"
                accept="image/*,application/pdf"
                className="sr-only"
                onChange={handleAttachmentUpload}
              />
              {uploadingAttachment ? 'Enviando...' : 'Selecionar arquivo'}
            </label>

            {watch('attachment') ? (
              <div className="flex flex-1 flex-col gap-1 text-sm text-slate-600 md:flex-row md:items-center md:justify-between">
                <div>
                  <p className="font-semibold text-slate-800">
                    {watch('attachment')}
                  </p>
                  {watch('urlAttachment') ? (
                    <a
                      href={watch('urlAttachment') ?? undefined}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-xs text-brand-600 hover:underline"
                    >
                      Abrir arquivo
                    </a>
                  ) : null}
                </div>
                <button
                  type="button"
                  className="text-xs font-semibold text-red-600"
                  onClick={handleRemoveAttachment}
                >
                  Remover
                </button>
              </div>
            ) : (
              <p className="text-xs text-slate-400">
                Nenhum anexo selecionado. Formatos aceitos: PDF, JPG, PNG.
              </p>
            )}
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Link
            to="/transactions"
            className="inline-flex items-center justify-center rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-600 transition hover:bg-slate-100"
          >
            Cancelar
          </Link>
          <Button type="submit" loading={isSubmitting} disabled={isSubmitting}>
            {isEditing ? 'Salvar alterações' : 'Criar lançamento'}
          </Button>
        </div>
      </form>
    </div>
  )
}
