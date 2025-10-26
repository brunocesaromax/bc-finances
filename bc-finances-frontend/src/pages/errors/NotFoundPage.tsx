import { Link } from 'react-router-dom'
import { Card } from '@/components/ui/Card'

export const NotFoundPage = () => (
  <div className="flex justify-center">
    <Card className="flex max-w-lg flex-col items-center text-center">
      <div className="flex h-14 w-14 items-center justify-center rounded-full bg-slate-100 text-slate-600">
        <svg
          width="28"
          height="28"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
        >
          <path
            d="M4 19H20M4 7H20V17C20 17.5304 19.7893 18.0391 19.4142 18.4142C19.0391 18.7893 18.5304 19 18 19H6C5.46957 19 4.96086 18.7893 4.58579 18.4142C4.21071 18.0391 4 17.5304 4 17V7ZM9 11V15M15 11V15M9 3V5M15 3V5"
            stroke="currentColor"
            strokeWidth="1.8"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </div>
      <h2 className="mt-6 text-2xl font-semibold text-slate-900">Página não encontrada</h2>
      <p className="mt-2 text-sm text-slate-500">
        O endereço acessado não existe ou foi movido. Utilize o menu principal para encontrar o conteúdo desejado.
      </p>

      <Link
        to="/transactions"
        className="mt-6 inline-flex items-center justify-center rounded-xl bg-brand-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-700"
      >
        Voltar para lançamentos
      </Link>
    </Card>
  </div>
)
