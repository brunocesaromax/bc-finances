import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'
import clsx from 'clsx'
import { useAuth } from '@/hooks/useAuth'
import { Button } from '@/components/ui/Button'
import { PERMISSIONS } from '@/utils/permissions'

const navigation = [
  {
    label: 'Lançamentos',
    to: '/transactions',
    permission: PERMISSIONS.SEARCH_TRANSACTION,
  },
]

const NavItems = ({ onNavigate }: { onNavigate?: () => void }) => {
  const { hasPermission } = useAuth()

  return (
    <>
      {navigation
        .filter((item) => hasPermission(item.permission))
        .map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              clsx(
                'cursor-pointer rounded-full px-4 py-2 text-sm font-semibold transition',
                isActive
                  ? 'bg-brand-100 text-brand-700 shadow-sm'
                  : 'text-slate-600 hover:bg-slate-100 hover:text-brand-700',
              )
            }
            onClick={onNavigate}
          >
            {item.label}
          </NavLink>
        ))}
    </>
  )
}

export const AppShell = () => {
  const { user, logout } = useAuth()
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false)
  const [isLoggingOut, setIsLoggingOut] = useState(false)

  const handleLogout = async () => {
    setIsLoggingOut(true)
    try {
      await logout()
    } finally {
      setIsLoggingOut(false)
    }
  }

  return (
    <div className="min-h-screen bg-slate-50"> 
      <header className="border-b border-slate-200 bg-white/80 backdrop-blur">
        <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4">
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-3">
              <span className="flex h-10 w-10 items-center justify-center rounded-2xl bg-brand-600 text-base font-bold text-white shadow-lg">
                BC
              </span>
              <div className="leading-none">
                <p className="text-sm font-semibold text-slate-800">BC Finances</p>
                <p className="text-xs text-slate-500">Gestão financeira inteligente</p>
              </div>
            </div>

            <nav className="hidden items-center gap-1 md:flex">
              <NavItems />
            </nav>
          </div>

          <div className="flex items-center gap-3">
            <div className="hidden text-right md:block">
              <p className="text-sm font-semibold text-slate-800">
                {user?.name ?? 'Usuário'}
              </p>
              <p className="text-xs text-slate-500">{user?.email}</p>
            </div>

            <Button
              type="button"
              variant="ghost"
              className="hidden md:inline-flex"
              onClick={handleLogout}
              loading={isLoggingOut}
            >
              Sair
            </Button>

            <button
              type="button"
              className="flex h-10 w-10 items-center justify-center rounded-full border border-slate-200 bg-white text-slate-600 transition hover:bg-slate-100 md:hidden"
              onClick={() => setIsMobileMenuOpen((prev) => !prev)}
              aria-label="Abrir menu"
            >
              <svg
                width="20"
                height="20"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  d="M4 6H20M4 12H20M4 18H20"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                />
              </svg>
            </button>
          </div>
        </div>

        {isMobileMenuOpen ? (
          <div className="border-t border-slate-200 md:hidden">
            <div className="space-y-4 px-4 py-4">
              <div>
                <p className="text-sm font-semibold text-slate-800">
                  {user?.name ?? 'Usuário'}
                </p>
                <p className="text-xs text-slate-500">{user?.email}</p>
              </div>

              <nav className="flex flex-col gap-2">
                <NavItems onNavigate={() => setIsMobileMenuOpen(false)} />
              </nav>

              <Button
                type="button"
                variant="outline"
                onClick={handleLogout}
                loading={isLoggingOut}
                fullWidth
              >
                Encerrar sessão
              </Button>
            </div>
          </div>
        ) : null}
      </header>

      <main className="mx-auto w-full max-w-6xl px-4 py-6">
        <Outlet />
      </main>
    </div>
  )
}
