package com.fabernovel.technologies.core

import com.fabernovel.technologies.utils.Either
import io.reactivex.Single
import org.joda.time.DateTime

interface ExampleRepository {
    val time: Single<Either<RandomUserReactiveError, DateTime>>
}

interface MainRepository {
    val users: Single<Either<RandomUserReactiveError, List<User>>>
}
