package com.yesidmarin.mobileappchallenge.domain

import com.yesidmarin.mobileappchallenge.data.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        siteId: String,
        status: String,
        productIdentifier: String? = null,
        query: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ) = repository.searchProducts(
        siteId = siteId,
        status = status,
        productIdentifier = productIdentifier,
        query = query,
        limit = limit,
        offset = offset
    )
}