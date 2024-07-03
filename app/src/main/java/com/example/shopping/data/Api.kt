package com.example.shopping.data
import com.example.shopping.data.model.Products
import retrofit2.http.GET


public interface Api
{

    @GET("products")
    suspend fun getProductsList(): Products
    companion object{
        const val BASE_URL = "https://dummyjson.com/"
    }
}