package fr.emse.lemeilleurcoinmobile.services

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServices {
    val userApiService : UserApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("http://10.0.2.2:8080/api/")
            .build()
            .create(UserApiService::class.java)
    }
}