package com.yesidmarin.mobileappchallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesidmarin.mobileappchallenge.data.response.Products
import com.yesidmarin.mobileappchallenge.domain.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(private val searchProductsUseCase: SearchProductsUseCase): ViewModel() {

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _productsResult = MutableStateFlow<Products?>(null)
    val productsResult: StateFlow<Products?> = _productsResult.asStateFlow()

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent.asStateFlow()

    private val _offset = MutableStateFlow(0)
    val offset: StateFlow<Int> = _offset.asStateFlow()

    private val _limit = MutableStateFlow(10)
    val limit: StateFlow<Int> = _limit.asStateFlow()

    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    fun setProductsResult(products: Products) {
        _productsResult.value = products
    }

    fun searchProducts(query: String, siteId: String, status: String, offset: Int, limit: Int = 10, productIdentifier: Boolean) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { searchProductsUseCase(
                siteId = siteId,
                status = status,
                query = if (!productIdentifier) query else null,
                productIdentifier = if (productIdentifier) query else null,
                limit = limit,
                offset = offset
            )
            }

            result.onSuccess { data ->
                processResult(query, data)
            }.onFailure { exception ->
                _errorState.value = exception.message ?: "Ocurrió un error inesperado"
            }

            _isLoadingMore.value = false
        }
    }

    private fun processResult(query: String, products: Products) {
        if (products.results.isEmpty()) {
            _errorState.value = "No se encontraron productos para la búsqueda: $query"
            return
        }
        _productsResult.update { current ->
            val merged = (current?.results.orEmpty() + products.results)
            current?.copy(
                results = merged,
                paging  = products.paging,
            ) ?: products.copy(results = merged)
        }

        _total.value = products.paging.total
    }

    fun loadMoreProducts(query: String, siteId: String, status: String, productIdentifier: Boolean) {
        if (_isLoadingMore.value) return
        _offset.value += _limit.value
        _isLoadingMore.value = true
        searchProducts(query, siteId, status, _offset.value, productIdentifier = productIdentifier)
    }

    fun navigateTo(destination: String) {
        _navigationEvent.value = destination
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}