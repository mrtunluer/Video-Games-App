package com.yks.videogamesapp.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yks.videogamesapp.db.LikedGamesDao
import com.yks.videogamesapp.db.LikedGamesDb
import com.yks.videogamesapp.service.ApiService
import com.yks.videogamesapp.utils.Constants.API_KEY
import com.yks.videogamesapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiKeyInterceptor(@Named("key")apiKey: String): Interceptor {
        return Interceptor.invoke {chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("key",apiKey)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

    @Singleton
    @Provides
    fun provideCallFactory(httpLoggingInterceptor: HttpLoggingInterceptor,
                           apiKeyInterceptor: Interceptor
    ): Call.Factory{
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        httpLoggingInterceptor: Call.Factory,
        gsonConverterFactory: GsonConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        @Named("base_url")baseUrl: String
    ): Retrofit{
        return Retrofit.Builder()
            .callFactory(httpLoggingInterceptor)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideRxjava3CallAdapter(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    @Named("key")
    fun provideApiKey(): String{
        return API_KEY
    }

    @Singleton
    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String{
        return BASE_URL
    }

    @Singleton
    @Provides
    fun provideGameService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLikedGamesDb(app: Application): LikedGamesDb {
        return Room.databaseBuilder(app, LikedGamesDb::class.java, LikedGamesDb.DB_NAME).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: LikedGamesDb): LikedGamesDao {
        return db.getLikedGamesDao()
    }

}