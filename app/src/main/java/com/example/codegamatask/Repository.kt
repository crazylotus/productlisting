package com.example.codegamatask

import com.example.codegamatask.datamodal.ProductList
import retrofit2.Response

class Repository constructor(private val apiSerive: ApiSerive) {

    suspend fun getCategoriesList() = apiSerive.getCategories()

    suspend fun getProducts( product : String) : Response<ProductList>  {
        return apiSerive.getProductList(product)
    }

}