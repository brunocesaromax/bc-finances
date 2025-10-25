import {
  createContext,
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
  type ReactNode,
} from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { jwtDecode } from 'jwt-decode'
import { authService, type LoginPayload } from '@/services/authService'
import type { LoginResponse, JwtPayload } from '@/types/auth'
import { authStorage } from '@/storage/authStorage'
import { setUnauthorizedHandler } from '@/services/apiClient'

type AuthUser = {
  email: string
  name: string
  permissions: string[]
  expiresAt: number
}

type AuthContextValue = {
  user: AuthUser | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (payload: LoginPayload) => Promise<LoginResponse>
  logout: (options?: { redirect?: boolean; silent?: boolean }) => Promise<void>
  hasPermission: (permission: string) => boolean
  hasAnyPermission: (permissions: string[]) => boolean
}

const AuthContext = createContext<AuthContextValue>({
  user: null,
  token: null,
  isAuthenticated: false,
  isLoading: true,
  async login() {
    throw new Error('AuthProvider is missing')
  },
  async logout() {
    throw new Error('AuthProvider is missing')
  },
  hasPermission: () => false,
  hasAnyPermission: () => false,
})

const tokenToUser = (token: string): AuthUser | null => {
  try {
    const payload = jwtDecode<JwtPayload>(token)
    const permissions = Array.isArray(payload.authorities)
      ? payload.authorities.filter((perm): perm is string => typeof perm === 'string')
      : []

    const expiresAt =
      typeof payload.exp === 'number' ? payload.exp * 1000 : Date.now()

    if (expiresAt <= Date.now()) {
      return null
    }

    const email =
      typeof payload.sub === 'string' && payload.sub.length > 0
        ? payload.sub
        : ''

    if (!email) {
      return null
    }

    const name =
      (typeof payload.name === 'string' && payload.name.length > 0
        ? payload.name
        : '') || email

    return {
      email,
      name,
      permissions,
      expiresAt,
    }
  } catch (error) {
    return null
  }
}

type AuthProviderProps = {
  children: ReactNode
}

const AuthProvider = ({ children }: AuthProviderProps) => {
  const navigate = useNavigate()
  const location = useLocation()
  const [user, setUser] = useState<AuthUser | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const isLoggingOutRef = useRef(false)

  useEffect(() => {
    const storedToken = authStorage.getToken()

    if (storedToken) {
      const decoded = tokenToUser(storedToken)

      if (decoded) {
        setToken(storedToken)
        setUser(decoded)
      } else {
        authStorage.clearToken()
      }
    }

    setIsLoading(false)
  }, [])

  const logout = useCallback(
    async (options?: { redirect?: boolean; silent?: boolean }) => {
      if (isLoggingOutRef.current) {
        return
      }

      isLoggingOutRef.current = true

      try {
        if (!options?.silent && authStorage.getToken()) {
          await authService.logout()
        } else if (options?.silent) {
          await authService
            .logout()
            .catch(() => {
              /* ignore */
            })
        }
      } catch (error) {
        if (!options?.silent) {
          console.error('Erro ao finalizar sessão', error)
        }
      } finally {
        authStorage.clearToken()
        setToken(null)
        setUser(null)
        isLoggingOutRef.current = false

        if (options?.redirect !== false) {
          const redirectTo =
            location.pathname === '/login' ? '/login' : '/login'
          navigate(redirectTo, { replace: true })
        }
      }
    },
    [location.pathname, navigate],
  )

  useEffect(() => {
    setUnauthorizedHandler(() => {
      logout({ silent: true })
    })

    return () => {
      setUnauthorizedHandler(null)
    }
  }, [logout])

  const login = useCallback(
    async (payload: LoginPayload) => {
      const response = await authService.login(payload)

      if (!response.accessToken) {
        throw new Error('Token inválido retornado pela API')
      }

      const decoded = tokenToUser(response.accessToken)

      if (!decoded) {
        authStorage.clearToken()
        throw new Error('Não foi possível validar o token de acesso')
      }

      const displayName = response.userName || decoded.name || decoded.email

      const hydratedUser: AuthUser = {
        ...decoded,
        name: displayName,
      }

      authStorage.setToken(response.accessToken)
      setToken(response.accessToken)
      setUser(hydratedUser)

      return {
        ...response,
        userName: displayName,
      }
    },
    [],
  )

  const hasPermission = useCallback(
    (permission: string) => {
      if (!user) {
        return false
      }

      return user.permissions.includes(permission)
    },
    [user],
  )

  const hasAnyPermission = useCallback(
    (permissions: string[]) => permissions.some((permission) => hasPermission(permission)),
    [hasPermission],
  )

  const contextValue = useMemo<AuthContextValue>(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(user && token),
      isLoading,
      login,
      logout,
      hasPermission,
      hasAnyPermission,
    }),
    [hasAnyPermission, hasPermission, isLoading, login, logout, token, user],
  )

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  )
}

export { AuthProvider, AuthContext }
