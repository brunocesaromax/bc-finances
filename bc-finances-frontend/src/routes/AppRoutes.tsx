import { Navigate, Outlet, Route, Routes, useLocation } from 'react-router-dom'
import { LoginPage } from '@/pages/auth/LoginPage'
import { AppShell } from '@/components/layout/AppShell'
import { TransactionsListPage } from '@/pages/transactions/TransactionsListPage'
import { TransactionFormPage } from '@/pages/transactions/TransactionFormPage'
import { PersonsListPage } from '@/pages/persons/PersonsListPage'
import { PersonFormPage } from '@/pages/persons/PersonFormPage'
import { NotAuthorizedPage } from '@/pages/errors/NotAuthorizedPage'
import { NotFoundPage } from '@/pages/errors/NotFoundPage'
import { useAuth } from '@/hooks/useAuth'
import { PERMISSIONS } from '@/utils/permissions'
import { Spinner } from '@/components/ui/Spinner'

const RequireAuth = () => {
  const { isAuthenticated, isLoading } = useAuth()
  const location = useLocation()

  if (isLoading) {
    return (
      <div className="flex justify-center py-12">
        <Spinner className="h-8 w-8" />
      </div>
    )
  }

  if (!isAuthenticated) {
    return (
      <Navigate
        to="/login"
        replace
        state={{ from: location.pathname + location.search }}
      />
    )
  }

  return <Outlet />
}

const RequirePermission = ({ permissions }: { permissions: string[] }) => {
  const { hasAnyPermission } = useAuth()

  if (!hasAnyPermission(permissions)) {
    return <Navigate to="/not-authorized" replace />
  }

  return <Outlet />
}

export const AppRoutes = () => (
  <Routes>
    <Route path="/login" element={<LoginPage />} />

    <Route element={<RequireAuth />}>
      <Route element={<AppShell />}>
        <Route index element={<Navigate to="/transactions" replace />} />

        <Route
          element={
            <RequirePermission
              permissions={[PERMISSIONS.SEARCH_TRANSACTION]}
            />
          }
        >
          <Route path="transactions" element={<TransactionsListPage />} />
          <Route path="transactions/:id" element={<TransactionFormPage />} />
        </Route>

        <Route
          element={
            <RequirePermission permissions={[PERMISSIONS.CREATE_TRANSACTION]} />
          }
        >
          <Route path="transactions/new" element={<TransactionFormPage />} />
        </Route>

        <Route
          element={
            <RequirePermission permissions={[PERMISSIONS.SEARCH_PERSON]} />
          }
        >
          <Route path="persons" element={<PersonsListPage />} />
          <Route path="persons/:id" element={<PersonFormPage />} />
        </Route>

        <Route
          element={
            <RequirePermission permissions={[PERMISSIONS.CREATE_PERSON]} />
          }
        >
          <Route path="persons/new" element={<PersonFormPage />} />
        </Route>

        <Route path="not-authorized" element={<NotAuthorizedPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Route>

    <Route path="*" element={<Navigate to="/login" replace />} />
  </Routes>
)
