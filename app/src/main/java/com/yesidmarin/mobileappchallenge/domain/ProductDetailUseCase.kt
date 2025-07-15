package com.yesidmarin.mobileappchallenge.domain

import com.yesidmarin.mobileappchallenge.data.ProductRepository
import javax.inject.Inject

class ProductDetailUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(id: String) = repository.getProductDetails(id)
}