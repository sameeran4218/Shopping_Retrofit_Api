package com.example.shopping.data

import com.example.shopping.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.HttpRetryException

class ProductsRepositoryImp(private val api: Api):ProductsRepository
{
    override suspend fun getProductsList(): Flow<Result<List<Product>>>
    {
       return flow{
           val productsFromApi=try {
               api.getProductsList()
           }
           catch (e:Exception)
           {
               emit(Result.Error(message = e.message.toString()))
               return@flow
           }
           emit(Result.Success(productsFromApi.products))
       }
    }

}