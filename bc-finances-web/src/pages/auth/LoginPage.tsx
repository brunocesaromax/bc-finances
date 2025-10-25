import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate, useLocation, Navigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { useAuth } from '@/hooks/useAuth'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { FormLabel } from '@/components/ui/FormLabel'
import { FormError } from '@/components/ui/FormError'

const loginSchema = z.object({
  email: z
    .string()
    .min(1, 'Informe o e-mail')
    .email('Informe um e-mail válido'),
  password: z.string().min(1, 'Informe a senha'),
})

type LoginFormData = z.infer<typeof loginSchema>

export const LoginPage = () => {
  const { login, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [isSubmitting, setIsSubmitting] = useState(false)
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  })

  if (isAuthenticated) {
    return <Navigate to="/transactions" replace />
  }

  const onSubmit = handleSubmit(async (values) => {
    setIsSubmitting(true)
    try {
      const response = await login(values)
      toast.success(`Bem-vindo(a), ${response.userName}!`)
      const redirectUrl =
        (location.state as { from?: string } | undefined)?.from ||
        '/transactions'
      navigate(redirectUrl, { replace: true })
    } catch (error: unknown) {
      const message =
        (error as { response?: { status?: number } }).response?.status === 401
          ? 'Usuário ou senha inválidos'
          : 'Não foi possível acessar. Tente novamente.'
      toast.error(message)
    } finally {
      setIsSubmitting(false)
    }
  })

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4 py-12">
      <div className="w-full max-w-md">
        <div className="mb-8 text-center">
          <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-brand-600 text-lg font-bold text-white shadow-lg">
            BC
          </div>
          <h1 className="mt-6 text-2xl font-semibold text-slate-900">
            Bem-vindo ao BC Finances
          </h1>
          <p className="mt-2 text-sm text-slate-500">
            Faça login para acompanhar lançamentos, pessoas e categorias.
          </p>
        </div>

        <form
          className="space-y-5 rounded-2xl border border-slate-200 bg-white p-8 shadow-[var(--shadow-card)]"
          onSubmit={onSubmit}
          noValidate
        >
          <div>
            <FormLabel htmlFor="email">E-mail</FormLabel>
            <Input
              id="email"
              type="email"
              placeholder="admin@algamoney.com"
              hasError={Boolean(errors.email)}
              autoComplete="email"
              {...register('email')}
            />
            {errors.email ? <FormError>{errors.email.message}</FormError> : null}
          </div>

          <div>
            <FormLabel htmlFor="password">Senha</FormLabel>
            <Input
              id="password"
              type="password"
              placeholder="Informe sua senha"
              hasError={Boolean(errors.password)}
              autoComplete="current-password"
              {...register('password')}
            />
            {errors.password ? (
              <FormError>{errors.password.message}</FormError>
            ) : null}
          </div>

          <Button
            type="submit"
            className="w-full"
            loading={isSubmitting}
            disabled={isSubmitting}
          >
            Entrar
          </Button>

          <p className="text-center text-xs text-slate-400">
            Use admin@algamoney.com / admin para um login rápido em ambientes de teste.
          </p>
        </form>
      </div>
    </div>
  )
}
