package com.fabernovel.technologies.core

sealed class RandomUserReactiveError : Exception()

object NetworkError : RandomUserReactiveError()
object OfflineError : RandomUserReactiveError()
object ServerError : RandomUserReactiveError()
object UnexpectedError : RandomUserReactiveError()

// Add other errors here.
