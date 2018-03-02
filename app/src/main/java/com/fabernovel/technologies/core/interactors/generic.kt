package com.fabernovel.technologies.core.interactors

import com.fabernovel.technologies.core.RandomUserReactiveError
import com.fabernovel.technologies.utils.Either
import com.fabernovel.technologies.utils.Left
import com.fabernovel.technologies.utils.Right
import io.reactivex.Flowable
import io.reactivex.Single

fun <Request, Response, Data, RepoData : Single<Either<RandomUserReactiveError, Data>>>
    fromRepo(
    upstream: Flowable<Request>,
    getter: (req: Request) -> RepoData,
    error: (it: RandomUserReactiveError) -> Response,
    success: (it: Data) -> Response,
    initial: Response
): Flowable<Response> {
    return upstream.flatMap { getter(it)
        .toFlowable()
        .map {
            when (it) {
                is Left -> error(it.value)
                is Right -> success(it.value)
            }
        }.startWith(initial)
    }
}
