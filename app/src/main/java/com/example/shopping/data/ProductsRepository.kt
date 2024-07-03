package com.example.shopping.data

import com.example.shopping.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository
{
    suspend fun getProductsList():Flow<Result<List<Product>>>

}