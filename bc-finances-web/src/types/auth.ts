export type LoginResponse = {
  accessToken: string
  tokenType: string
  expiresIn: number
  userName: string
}

export type JwtPayload = {
  sub: string
  exp: number
  iat?: number
  name?: string
  authorities?: string[]
  [key: string]: unknown
}
