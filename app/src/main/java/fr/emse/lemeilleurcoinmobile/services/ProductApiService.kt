package fr.emse.lemeilleurcoinmobile.services

import fr.emse.lemeilleurcoinmobile.dto.OfferDto
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.dto.UserDto
import fr.emse.lemeilleurcoinmobile.dto.ViewDto
import fr.emse.lemeilleurcoinmobile.model.Category
import retrofit2.Call
import retrofit2.http.*

interface ProductApiService {

    @GET("all")
    fun getAllProductsByCategoryByTitle(@Query("category") category: Category?, @Query("title") title: String): Call<List<ProductDto>>

    @POST("create")
    fun create(@Body productDto: ProductDto): Call<ProductDto>

    @GET("own/{email}")
    fun getAllProductsByUser(@Path("email") email: String):Call<List<ProductDto>>

    @DELETE("{id}")
    fun delete( @Path("id")id :Long?,@Query("pass") password: String): Call<Boolean>

    @GET("product/{id}")
    fun get(@Path("id") id: Long): Call<ProductDto>

    @POST("modify")
    fun modify(@Query("pass") password: String, @Body productDto: ProductDto):Call<ProductDto>
}
