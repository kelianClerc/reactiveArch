package com.fabernovel.technologies.di.common

import android.content.Context
import android.net.ConnectivityManager
import com.fabernovel.technologies.Settings
import com.fabernovel.technologies.data.net.common.ConnectivityInterceptor
import com.fabernovel.technologies.data.net.retrofit.RandomUserReactiveService
import com.fabernovel.technologies.data.net.retrofit.RestError
import com.fabernovel.technologies.utils.InterceptorLogger
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.BuildConfig
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ServiceModule(private val cacheDirectory: File, private val context: Context) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    internal fun provideService(retrofit: Retrofit): RandomUserReactiveService {
        return retrofit.create(RandomUserReactiveService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideConverter(retrofit: Retrofit): Converter<ResponseBody, RestError> =
        retrofit.responseBodyConverter(RestError::class.java, arrayOfNulls(0))

    @Provides
    @Singleton
    internal fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: Converter.Factory,
        adapterFactory: CallAdapter.Factory
    ): Retrofit {
        val baseUrl = sanitizeUrl(BASE_URL)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(adapterFactory)
            .build()
    }

    @Provides @Singleton
    internal fun provideAdapter(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    private fun sanitizeUrl(baseUrl: String): String {
        return if (baseUrl.endsWith("/")) {
            baseUrl
        } else baseUrl + "/"
    }

    @Provides @Singleton
    internal fun provideLoggingInterceptor(logger: HttpLoggingInterceptor.Logger): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(logger)
        if (BuildConfig.DEBUG && LOG_NETWORK) {
            interceptor.level = networkLogLevel
        }
        return interceptor
    }

    private val networkLogLevel: HttpLoggingInterceptor.Level
        get() = HttpLoggingInterceptor.Level.valueOf(LOG_NETWORK_LEVEL)

    @Provides
    @Singleton internal fun provideConnectivityManager(): ConnectivityManager {
        return connectivityManager
    }

    @Provides @Singleton
    internal fun provideClient(
        loggingInterceptor: HttpLoggingInterceptor,
        chuckInterceptor: ChuckInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        cache: Cache?
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(loggingInterceptor)
        if (BuildConfig.DEBUG && ENABLE_CHUCK) {
            builder.addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides @Singleton
    internal fun provideChuckInterceptor(): ChuckInterceptor = ChuckInterceptor(context)

    @Provides @Singleton
    internal fun provideLogger(logger: InterceptorLogger): HttpLoggingInterceptor.Logger = logger

    @Provides @Singleton
    internal fun provideJsonConverterFactory(gson: Gson): Converter.Factory =
        GsonConverterFactory.create(gson)

    @Provides
    internal fun provideGson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()


    @Provides @Singleton
    internal fun provideCache(@Named("cacheDir") cacheDirectory: File): Cache? =
        if (BuildConfig.DEBUG && !ENABLE_CACHE) null
        else Cache(cacheDirectory, CACHE_SIZE)

    @Provides @Named("cacheDir") @Singleton
    internal fun provideCacheDirectory(): File = cacheDirectory

    companion object {
        private val BASE_URL = Settings.network.base_url
        private val LOG_NETWORK = Settings.network.logging.enabled
        private val TIMEOUT = Settings.network.timeout_seconds
        private val LOG_NETWORK_LEVEL = Settings.network.logging.level
        private val CACHE_SIZE =
            Settings.network.cache.size_mb.toLong() * 1024L * 1024L // Mb in bytes
        private val ENABLE_CACHE = Settings.network.cache.enabled
        private val ENABLE_CHUCK = Settings.performance.chuck
    }
}
