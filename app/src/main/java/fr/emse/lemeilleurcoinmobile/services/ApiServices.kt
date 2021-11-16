package fr.emse.lemeilleurcoinmobile.services

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServices {
    val userApiService : UserApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            //.baseUrl("http://10.0.2.2:8080/api/users/")
            .baseUrl("https://lemeilleurcoin-yasser-faleh.cleverapps.io/api/users/")
            .build()
            .create(UserApiService::class.java)
    }
    val viewApiService : ViewApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            //.baseUrl("http://10.0.2.2:8080/api/views/")
            .baseUrl("https://lemeilleurcoin-yasser-faleh.cleverapps.io/api/views/")
            .build()
            .create(ViewApiService::class.java)
    }
    val productApiService : ProductApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            //.baseUrl("http://10.0.2.2:8080/api/products/")
            .baseUrl("https://lemeilleurcoin-yasser-faleh.cleverapps.io/api/products/")
            .build()
            .create(ProductApiService::class.java)
    }
    val offerApiService : OfferApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            //.baseUrl("http://10.0.2.2:8080/api/offers/")
            .baseUrl("https://lemeilleurcoin-yasser-faleh.cleverapps.io/api/offers/")
            .build()
            .create(OfferApiService::class.java)
    }
}