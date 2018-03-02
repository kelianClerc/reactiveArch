package com.fabernovel.technologies.data.net.common

import android.util.MalformedJsonException
import com.fabernovel.technologies.core.*
import com.fabernovel.technologies.utils.Either
import com.fabernovel.technologies.utils.Left
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import java.io.EOFException
import java.io.IOException
import java.net.SocketException

class NetworkErrorHandler<T> : SingleTransformer<Either<RandomUserReactiveError, T>, Either<RandomUserReactiveError, T>> {
    override fun apply(upstream: Single<Either<RandomUserReactiveError, T>>): SingleSource<Either<RandomUserReactiveError, T>> {
        return upstream.onErrorResumeNext {
            val error = when (it) {
                is SocketException -> Left(NetworkError)
                is MalformedJsonException -> Left(ServerError)
                is JsonSyntaxException -> Left(ServerError)
                is EOFException -> Left(ServerError)
                is OfflineIOException -> Left(OfflineError)
                is IOException -> Left(UnexpectedError)
                else -> Left(UnexpectedError)
            }
            Single.just(error)
        }
    }
}
