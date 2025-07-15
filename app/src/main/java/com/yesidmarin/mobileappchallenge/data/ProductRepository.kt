package com.yesidmarin.mobileappchallenge.data

import com.yesidmarin.mobileappchallenge.data.response.ProductDetail
import com.yesidmarin.mobileappchallenge.data.response.Products
import com.yesidmarin.mobileappchallenge.network.MercadoLibreApi
import com.yesidmarin.mobileappchallenge.network.safeApiCall
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val api: MercadoLibreApi
) {
    suspend fun searchProducts(
        siteId: String,
        status: String,
        productIdentifier: String? = null,
        query: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): Result<Products> = safeApiCall { api.searchProducts(
            siteId = siteId,
            status = status,
            productIdentifier = productIdentifier,
            query = query,
            limit = limit,
            offset = offset
        )
    }

    suspend fun getProductDetails(id: String): Result<ProductDetail> = safeApiCall {
        api.getProductDetails(id)
    }
}