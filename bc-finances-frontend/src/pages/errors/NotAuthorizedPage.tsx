import { Link } from 'react-router-dom'
import { Card } from '@/components/ui/Card'

export const NotAuthorizedPage = () => (
  <div className="flex justify-center">
    <Card className="flex max-w-lg flex-col items-center text-center">
      <div className="flex h-14 w-14 items-center justify-center rounded-full bg-red-50 text-red-600">
        <svg
          width="28"
          height="28"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
        >
          <path
            d="M12 9V12M12 15H12.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z"
            stroke="currentColor"
            strokeWidth="1.8"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </div>
      <h2 className="mt-6 text-2xl font-semibold text-slate-900">Acesso negado</h2>
      <p className="mt-2 text-sm text-slate-500">
        Você não possui permissão para acessar esta funcionalidade. Entre em contato com um administrador para solicitar acesso.
      </p>

      <Link
        to="/transactions"
        className="mt-6 inline-flex items-center justify-center rounded-xl bg-brand-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-700"
      >
        Voltar aos lançamentos
      </Link>
    </Card>
  </div>
)
