import { useEffect, useState } from 'react'
import { Controller, useForm, type Resolver } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { Link, useNavigate, useParams } from 'react-router-dom'
import toast from 'react-hot-toast'
import { transactionService } from '@/services/transactionService'
import { categoryService } from '@/services/categoryService'
import { tagService } from '@/services/tagService'
import type { Category } from '@/types/category'
import type {
  TransactionDetail,
  TransactionPayload,
  Attachment,
} from '@/types/transaction'
import type { TransactionType } from '@/types/finance'
import type { Tag } from '@/types/tag'
import { FormLabel } from '@/components/ui/FormLabel'
import { FormError } from '@/components/ui/FormError'
import { Select } from '@/components/ui/Select'
import { Textarea } from '@/components/ui/Textarea'
import { Button } from '@/components/ui/Button'
import { Spinner } from '@/components/ui/Spinner'
import { DatePicker } from '@/components/ui/DatePicker'
import { CurrencyInput } from '@/components/ui/CurrencyInput'
import { Input } from '@/components/ui/Input'
import { Badge } from '@/components/ui/Badge'
import { parseDateInputValue } from '@/utils/formatters'

const TAG_MAX_LENGTH = 80

const currencySchema = z
  .any()
  .transform((value) => {
    if (typeof value === 'number') {
      return value
    }

    if (typeof value === 'string' && value.trim().length > 0) {
      return Number(value.replace(',', '.'))
    }

    return NaN
  })
  .refine((value) => !Number.isNaN(value), 'Informe o valor do lançamento')
  .refine((value) => value > 0, 'O valor deve ser maior que zero')

const attachmentSchema = z.object({
  name: z.string().min(1),
  originalName: z.string().optional().nullable(),
  contentType: z.string().optional().nullable(),
  size: z.number().optional().nullable(),
  url: z.string().optional().nullable(),
})

const transactionSchema = z.object({
  description: z.string().min(5, 'Informe a descrição (mínimo 5 caracteres)'),
  value: currencySchema,
  type: z.enum(['RECIPE', 'EXPENSE']),
  dueDay: z.string().min(1, 'Informe a data de vencimento'),
  payday: z.string().optional().nullable(),
  categoryId: z.string().min(1, 'Selecione a categoria'),
  observation: z.string().optional().nullable(),
  tags: z.array(z.string().min(1).max(TAG_MAX_LENGTH)).optional().default([]),
  attachments: z.array(attachmentSchema).optional().default([]),
})

type TransactionFormValues = z.infer<typeof transactionSchema>

const DEFAULT_VALUES: TransactionFormValues = {
  description: '',
  value: undefined as unknown as number,
  type: 'RECIPE',
  dueDay: '',
  payday: '',
  categoryId: '',
  observation: '',
  tags: [],
  attachments: [],
}

export const TransactionFormPage = () => {
  const { id } = useParams<{ id: string }>()
  const isEditing = Boolean(id)
  const navigate = useNavigate()
  const [categoriesByType, setCategoriesByType] = useState<
    Record<TransactionType, Category[]>
  >({
    RECIPE: [],
    EXPENSE: [],
  })
  const [isLoadingCategories, setIsLoadingCategories] = useState(false)
  const [availableTags, setAvailableTags] = useState<Tag[]>([])
  const [tagInput, setTagInput] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [uploadingAttachment, setUploadingAttachment] = useState(false)
  const [transactionId, setTransactionId] = useState<number | null>(null)

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    watch,
    control,
    formState: { errors },
  } = useForm<TransactionFormValues>({
    resolver: zodResolver(transactionSchema) as Resolver<TransactionFormValues>,
    defaultValues: DEFAULT_VALUES,
  })

  const watchedType = watch('type')
  const watchedTags = watch('tags') ?? []
  const watchedAttachments = watch('attachments') ?? []
  const watchedCategoryId = watch('categoryId')
  const currentCategories = categoriesByType[watchedType] ?? []

  const loadTags = async () => {
    try {
      const tagsResponse = await tagService.findAll()
      setAvailableTags(tagsResponse)
    } catch (error) {
      console.error('Não foi possível carregar tags.', error)
      toast.error('Não foi possível carregar tags.')
    }
  }

  const loadCategories = async (typeToLoad: TransactionType) => {
    setIsLoadingCategories(true)
    try {
      const categoriesResponse = await categoryService.findAll(typeToLoad)
      setCategoriesByType((previous) => ({
        ...previous,
        [typeToLoad]: categoriesResponse,
      }))
      return categoriesResponse
    } catch (error) {
      console.error('Não foi possível carregar categorias.', error)
      toast.error('Não foi possível carregar categorias.')
      return []
    } finally {
      setIsLoadingCategories(false)
    }
  }

  const hydrateForm = async (transaction: TransactionDetail) => {
    setTransactionId(transaction.id ?? null)
    await loadCategories(transaction.type)

    reset({
      description: transaction.description,
      value: transaction.value,
      type: transaction.type,
      dueDay: parseDateInputValue(transaction.dueDay) ?? '',
      payday: parseDateInputValue(transaction.payday) ?? '',
      categoryId: transaction.category.id?.toString() ?? '',
      observation: transaction.observation ?? '',
      tags: transaction.tags ?? [],
      attachments: transaction.attachments ?? [],
    })
  }

  useEffect(() => {
    const initialize = async () => {
      try {
        await Promise.all([loadTags(), loadCategories(watchedType)])

        if (isEditing && id) {
          const transaction = await transactionService.findById(Number(id))
          await hydrateForm(transaction)
        }
      } catch (error) {
        console.error('Não foi possível inicializar o formulário.', error)
        toast.error('Não foi possível carregar dados para o formulário.')
      }

      setIsLoading(false)
    }

    initialize()
  }, [id])

  useEffect(() => {
    const ensureCategoriesForType = async () => {
      const categories = categoriesByType[watchedType]
      if (!categories || categories.length === 0) {
        await loadCategories(watchedType)
      }

      if (
        watchedCategoryId &&
        !(categoriesByType[watchedType] ?? []).some(
          (category) => String(category.id) === watchedCategoryId,
        )
      ) {
        setValue('categoryId', '')
      }
    }

    ensureCategoriesForType()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [watchedType, categoriesByType, watchedCategoryId])

  const handleTypeChange = async (nextType: TransactionType) => {
    setValue('type', nextType)
    await loadCategories(nextType)
    setValue('categoryId', '')
  }

  const onSubmit = handleSubmit(async (values) => {
    setIsSubmitting(true)

    const attachmentsPayload: Attachment[] = (values.attachments ?? []).map(
      (attachment) => ({
        name: attachment.name,
        originalName: attachment.originalName ?? attachment.name,
        contentType: attachment.contentType,
        size: attachment.size,
      }),
    )

    const payload: TransactionPayload = {
      description: values.description,
      value: Number(values.value),
      type: values.type,
      dueDay: values.dueDay,
      payday: values.payday ? values.payday : null,
      observation: values.observation ?? '',
      categoryId: Number(values.categoryId),
      tags: values.tags ?? [],
      attachments: attachmentsPayload,
    }

    try {
      if (isEditing && transactionId) {
        await transactionService.update({
          id: transactionId,
          ...payload,
        })
        toast.success('Lançamento atualizado com sucesso!')
        navigate('/transactions')
      } else {
        const saved = await transactionService.save(payload)
        toast.success('Lançamento criado com sucesso!')
        navigate(`/transactions/${saved.id}`)
      }
    } catch (error) {
      console.error('Não foi possível salvar o lançamento.', error)
      toast.error('Não foi possível salvar o lançamento.')
    } finally {
      setIsSubmitting(false)
    }
  })

  const handleAttachmentUpload = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const files = event.target.files ? Array.from(event.target.files) : []
    if (!files.length) {
      return
    }

    setUploadingAttachment(true)

    try {
      const uploads = await transactionService.uploadAttachments(files)
      const current = watch('attachments') ?? []

      const merged = [...current]
      uploads.forEach((upload) => {
        if (!merged.some((attachment) => attachment.name === upload.name)) {
          merged.push(upload)
        }
      })

      setValue('attachments', merged, { shouldValidate: true })
      toast.success('Anexo enviado com sucesso!')
    } catch (error) {
      console.error('Não foi possível enviar o anexo.', error)
      toast.error('Não foi possível enviar o anexo.')
    } finally {
      setUploadingAttachment(false)
      event.target.value = ''
    }
  }

  const handleRemoveAttachment = (name: string) => {
    const nextAttachments = (watch('attachments') ?? []).filter(
      (attachment) => attachment.name !== name,
    )
    setValue('attachments', nextAttachments, { shouldValidate: true })
  }

  const handleTagAddition = () => {
    const normalized = tagInput.trim().slice(0, TAG_MAX_LENGTH)
    if (!normalized) {
      return
    }

    const alreadyAdded = watchedTags.some(
      (tag) => tag.toLowerCase() === normalized.toLowerCase(),
    )
    if (alreadyAdded) {
      setTagInput('')
      return
    }

    setValue('tags', [...watchedTags, normalized], { shouldValidate: true })
    setTagInput('')
  }

  const handleRemoveTag = (tagToRemove: string) => {
    setValue(
      'tags',
      watchedTags.filter(
        (tag) => tag.toLowerCase() !== tagToRemove.toLowerCase(),
      ),
      { shouldValidate: true },
    )
  }

  const tagSuggestions = availableTags.filter((tag) => {
    const normalizedQuery = tagInput.trim().toLowerCase()
    const alreadySelected = watchedTags.some(
      (selected) => selected.toLowerCase() === tag.name.toLowerCase(),
    )
    return (
      !alreadySelected &&
      (normalizedQuery === '' ||
        tag.name.toLowerCase().includes(normalizedQuery))
    )
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
          {isEditing ? 'Editar lançamento' : 'Novo lançamento'}
        </h1>
        <p className="text-sm text-slate-500">
          Controle os fluxos de caixa, tags e anexos relacionados a cada transação.
        </p>
      </header>

      <form className="space-y-6" onSubmit={onSubmit} noValidate>
        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">
            Informações básicas
          </h2>
          <div className="mt-4 grid gap-4 md:grid-cols-2">
            <div className="md:col-span-2">
              <FormLabel htmlFor="description" requiredIndicator>
                Descrição
              </FormLabel>
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
              <FormLabel htmlFor="value" requiredIndicator>
                Valor
              </FormLabel>
              <Controller
                control={control}
                name="value"
                render={({ field }) => (
                  <CurrencyInput
                    id="value"
                    value={
                      typeof field.value === 'number' && !Number.isNaN(field.value)
                        ? field.value
                        : null
                    }
                    hasError={Boolean(errors.value)}
                    onChange={(nextValue) => field.onChange(nextValue ?? undefined)}
                    onBlur={field.onBlur}
                    required
                  />
                )}
              />
              {errors.value ? <FormError>{errors.value.message}</FormError> : null}
            </div>

            <div>
              <FormLabel htmlFor="type" requiredIndicator>
                Tipo
              </FormLabel>
              <Select
                id="type"
                {...register('type', {
                  onChange: (event) =>
                    handleTypeChange(event.target.value as TransactionType),
                })}
              >
                <option value="RECIPE">Receita</option>
                <option value="EXPENSE">Despesa</option>
              </Select>
            </div>

            <div>
              <FormLabel htmlFor="dueDay" requiredIndicator>
                Data de vencimento
              </FormLabel>
              <Controller
                control={control}
                name="dueDay"
                render={({ field }) => (
                  <DatePicker
                    id="dueDay"
                    value={field.value || null}
                    onChange={(value) => field.onChange(value ?? '')}
                    onBlur={field.onBlur}
                    hasError={Boolean(errors.dueDay)}
                    required
                    className="w-full"
                  />
                )}
              />
              {errors.dueDay ? <FormError>{errors.dueDay.message}</FormError> : null}
            </div>

            <div>
              <FormLabel htmlFor="payday">Data de pagamento</FormLabel>
              <Controller
                control={control}
                name="payday"
                render={({ field }) => (
                  <DatePicker
                    id="payday"
                    value={field.value || null}
                    onChange={(value) => field.onChange(value ?? '')}
                    onBlur={field.onBlur}
                    className="w-full"
                  />
                )}
              />
            </div>

            <div>
              <FormLabel htmlFor="categoryId" requiredIndicator>
                Categoria
              </FormLabel>
              <Select
                id="categoryId"
                hasError={Boolean(errors.categoryId)}
                disabled={isLoadingCategories}
                {...register('categoryId')}
              >
                <option value="">Selecione...</option>
                {currentCategories.map((category) => (
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

            <div className="md:col-span-2">
              <FormLabel htmlFor="tags">Tags</FormLabel>
              <div className="flex flex-col gap-3 rounded-2xl border border-slate-200 bg-slate-50 p-4">
                <div className="flex flex-col gap-2 md:flex-row">
                  <Input
                    id="tags"
                    placeholder="Digite e pressione Enter para adicionar"
                    value={tagInput}
                    onChange={(event) => setTagInput(event.target.value)}
                    onKeyDown={(event) => {
                      if (event.key === 'Enter' || event.key === ',') {
                        event.preventDefault()
                        handleTagAddition()
                      }
                    }}
                  />
                  <Button
                    type="button"
                    variant="outline"
                    onClick={handleTagAddition}
                    disabled={!tagInput.trim()}
                    className="md:w-40"
                  >
                    Adicionar tag
                  </Button>
                </div>

                {watchedTags.length > 0 ? (
                  <div className="flex flex-wrap gap-2">
                    {watchedTags.map((tag) => (
                      <Badge key={tag} className="flex items-center gap-2">
                        <span>{tag}</span>
                        <button
                          type="button"
                          className="text-slate-500 transition hover:text-slate-700"
                          onClick={() => handleRemoveTag(tag)}
                          aria-label={`Remover tag ${tag}`}
                        >
                          &times;
                        </button>
                      </Badge>
                    ))}
                  </div>
                ) : (
                  <p className="text-xs text-slate-500">
                    Use tags para agrupar lançamentos (ex.: aluguel, assinatura, saúde).
                  </p>
                )}

                {tagSuggestions.length > 0 ? (
                  <div className="flex flex-wrap gap-2">
                    {tagSuggestions.slice(0, 6).map((tag) => (
                      <button
                        type="button"
                        key={tag.id}
                        className="rounded-full border border-slate-200 bg-white px-3 py-1 text-xs font-semibold text-slate-600 transition hover:border-brand-300 hover:text-brand-700"
                        onClick={() => {
                          setValue('tags', [...watchedTags, tag.name], {
                            shouldValidate: true,
                          })
                          setTagInput('')
                        }}
                      >
                        {tag.name}
                      </button>
                    ))}
                  </div>
                ) : null}
              </div>
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">Anexos</h2>
          <p className="mt-1 text-sm text-slate-500">
            Inclua comprovantes, notas fiscais ou imagens relacionados ao lançamento.
          </p>

          <div className="mt-4 flex flex-col gap-3 md:flex-row md:items-center">
            <label className="inline-flex cursor-pointer items-center gap-2 rounded-xl border border-dashed border-brand-300 bg-brand-50 px-4 py-2 text-sm font-semibold text-brand-700 shadow-sm transition hover:bg-brand-100">
              <input
                type="file"
                accept="image/*,application/pdf,text/plain"
                multiple
                className="sr-only"
                onChange={handleAttachmentUpload}
              />
              {uploadingAttachment ? 'Enviando...' : 'Selecionar arquivos'}
            </label>

            {watchedAttachments.length > 0 ? (
              <div className="flex-1 space-y-3">
                {watchedAttachments.map((attachment) => (
                  <div
                    key={attachment.name}
                    className="flex flex-col gap-1 rounded-xl border border-slate-100 bg-slate-50 px-3 py-2 text-sm text-slate-700 md:flex-row md:items-center md:justify-between"
                  >
                    <div className="space-y-0.5">
                      <p className="font-semibold text-slate-800">
                        {attachment.originalName ?? attachment.name}
                      </p>
                      <p className="text-xs text-slate-500">
                        {attachment.contentType ?? 'Arquivo'}
                      </p>
                      {attachment.url ? (
                        <a
                          href={attachment.url}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-xs font-semibold text-brand-700 hover:underline"
                        >
                          Abrir arquivo
                        </a>
                      ) : null}
                    </div>
                    <button
                      type="button"
                      className="text-xs font-semibold text-red-600 transition hover:text-red-700"
                      onClick={() => handleRemoveAttachment(attachment.name)}
                    >
                      Remover
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-xs text-slate-400">
                Nenhum anexo selecionado. Formatos aceitos: PDF, imagens e texto.
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
