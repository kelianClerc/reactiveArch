package com.fabernovel.technologies.data

import com.fabernovel.technologies.core.ExampleRepository
import com.fabernovel.technologies.core.RandomUserReactiveError
import com.fabernovel.technologies.data.net.common.NetworkErrorHandler
import com.fabernovel.technologies.utils.Either
import com.fabernovel.technologies.utils.Right
import io.reactivex.Single
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StubExampleRepository @Inject internal constructor() : ExampleRepository {
    override val time: Single<Either<RandomUserReactiveError, DateTime>>
        get() = Single
            .just(Right(DateTime.now()))
            .delay(1L, TimeUnit.SECONDS)
            // Use this transformer to catch network errors
            .compose(NetworkErrorHandler())
}
