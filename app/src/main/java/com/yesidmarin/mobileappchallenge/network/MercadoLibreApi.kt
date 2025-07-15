package com.yesidmarin.mobileappchallenge.network

import com.yesidmarin.mobileappchallenge.data.response.ProductDetail
import com.yesidmarin.mobileappchallenge.data.response.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreApi {

    @GET("products/search")
    suspend fun searchProducts(
        @Query("site_id") siteId: String,
        @Query("status") status: String,
        @Query("product_identifier") productIdentifier: String?,
        @Query("q") query: String?,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<Products>

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") id: String): Response<ProductDetail>
}