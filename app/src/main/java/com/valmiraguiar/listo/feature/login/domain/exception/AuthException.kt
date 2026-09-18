package com.valmiraguiar.listo.feature.login.domain.exception

class InvalidCredentialsException(cause: Throwable? = null) : Exception(cause)
class AuthNetworkException(cause: Throwable? = null) : Exception(cause)
class EmailAlreadyInUseException(cause: Throwable? = null) : Exception(cause)
class WeakPasswordException(cause: Throwable? = null) : Exception(cause)
