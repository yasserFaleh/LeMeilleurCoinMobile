package fr.emse.lemeilleurcoinmobile.services

import fr.emse.lemeilleurcoinmobile.dto.OfferDto
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.dto.UserDto
import fr.emse.lemeilleurcoinmobile.dto.ViewDto
import fr.emse.lemeilleurcoinmobile.model.Category
import retrofit2.Call
import retrofit2.http.*

interface OfferApiService {
    @GET("all")
    fun getAllOffersByCategoryByTitle(@Query("category") category: Category?, @Query("title") title: String): Call<List<OfferDto>>

    @POST("create")
    fun create(@Body offerDto: OfferDto): Call<OfferDto>

    @GET("own/{email}")
    fun getAllOffersByUser(@Path("email") email: String):Call<List<OfferDto>>

    @DELETE("{id}")
    fun delete(@Path("id")id :Long?,@Query("pass") password: String): Call<Boolean>
}