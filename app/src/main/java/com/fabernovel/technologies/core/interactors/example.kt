package com.fabernovel.technologies.core.interactors

import com.fabernovel.technologies.core.ExampleRepository
import com.fabernovel.technologies.core.RandomUserReactiveError
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import org.joda.time.DateTime
import org.reactivestreams.Publisher
import javax.inject.Inject

class GetDateTime @Inject internal constructor(
    private val repo: ExampleRepository
) : FlowableTransformer<GetDateTime.Request, GetDateTime.Response> {

    object Request
    sealed class Response {
        object InFlight : Response()
        data class Error(val error: RandomUserReactiveError) : Response()
        data class Success(val time: DateTime) : Response()
    }

    override fun apply(upstream: Flowable<Request>): Publisher<Response> = fromRepo(
        upstream, { repo.time }, { Response.Error(it) }, { Response.Success(it) }, Response.InFlight
    )
}
