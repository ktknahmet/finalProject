package com.ktknahmet.final_project.sevices
import com.ktknahmet.final_project.utils.AppConstants
import com.ktknahmet.final_project.utils.IRxSchedulers
import com.ktknahmet.final_project.sevices.RxService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    internal fun provideCoroutineApiService(@Named(AppConstants.COROUTINE_RETROFIT) restAdapter: Retrofit): ApiService {
        return restAdapter.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(AppConstants.COROUTINE_RETROFIT)
    internal fun provideCoroutineRestAdapter(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        @Suppress("DEPRECATION") val httpBuilder = OkHttpClient.Builder()
            .callTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
        return httpBuilder.build()
    }

    @Provides
    @Singleton
    internal fun provideRxApiService(@Named(AppConstants.RX_RETROFIT) restAdapter: Retrofit): RxService {
        return restAdapter.create(RxService::class.java)
    }

    @Provides
    @Singleton
    @Named(AppConstants.RX_RETROFIT)
    internal fun provideRxRestAdapter(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRxSchedulers(): IRxSchedulers {
        return object : IRxSchedulers {
            override fun main() = AndroidSchedulers.mainThread()
            override fun io() = Schedulers.io()
        }
    }
}
