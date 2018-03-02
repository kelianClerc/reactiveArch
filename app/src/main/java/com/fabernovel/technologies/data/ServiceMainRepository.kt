package com.fabernovel.technologies.data

import com.fabernovel.technologies.core.MainRepository
import com.fabernovel.technologies.core.RandomUserReactiveError
import com.fabernovel.technologies.core.UnexpectedError
import com.fabernovel.technologies.core.User
import com.fabernovel.technologies.data.net.common.NetworkErrorHandler
import com.fabernovel.technologies.data.net.retrofit.RandomUserReactiveService
import com.fabernovel.technologies.data.net.retrofit.mapUser
import com.fabernovel.technologies.utils.Either
import com.fabernovel.technologies.utils.Left
import com.fabernovel.technologies.utils.Right
import io.reactivex.Single


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceMainRepository @Inject internal constructor(
    private val service: RandomUserReactiveService
) : MainRepository {
    override val users: Single<Either<RandomUserReactiveError, List<User>>>
        get() = service.getUsers().map {
            if (it.isSuccessful) {
                Right(mapUser(it.body()))
            } else {
                Left(UnexpectedError as RandomUserReactiveError)
            }
        }.compose(NetworkErrorHandler())

}
