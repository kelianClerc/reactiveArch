package com.fabernovel.technologies.data.net.common

import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ConnectivityInterceptor @Inject internal constructor(
    private val connectivityManager: ConnectivityManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (isConnected) {
            return chain.proceed(chain.request())
        }
        throw OfflineIOException()
    }

    private val isConnected: Boolean
    get() {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}

