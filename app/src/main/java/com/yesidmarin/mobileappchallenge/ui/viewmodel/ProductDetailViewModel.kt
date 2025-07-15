package com.yesidmarin.mobileappchallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesidmarin.mobileappchallenge.data.response.ProductDetail
import com.yesidmarin.mobileappchallenge.domain.ProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.filter
import kotlin.collections.orEmpty

@HiltViewModel
class ProductDetailViewModel@Inject constructor(private val productDetailUseCase: ProductDetailUseCase): ViewModel() {

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDetail?>(null)
    val productDetail: StateFlow<ProductDetail?> = _productDetail.asStateFlow()

    private val _colorPickerSelected = MutableStateFlow<ProductDetail.Pickers.Products?>(null)

    private val _colorNameSelect = MutableStateFlow<String?>(null)
    val colorNameSelect: StateFlow<String?> = _colorNameSelect.asStateFlow()

    private val _productId = MutableStateFlow<String?>(null)
    val productId: StateFlow<String?> = _productId.asStateFlow()

    private val _colorPicker = MutableStateFlow<ProductDetail.Pickers?>(null)
    val colorPicker: StateFlow<ProductDetail.Pickers?> = _colorPicker.asStateFlow()

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images.asStateFlow()

    private val _productName = MutableStateFlow<String?>(null)
    val productName: StateFlow<String?> = _productName.asStateFlow()

    fun getProductDetails(id: String?) {

        if (id.isNullOrEmpty()) {
            _errorState.value = "El ID del producto no se encontró"
            return
        }

        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) { productDetailUseCase(id) }

            result.onSuccess { product ->
                _productDetail.value = product
                _productId.value    = product.id
                processColorPicker(product, id)
            }.onFailure { e ->
                _errorState.value = e.message ?: "Ocurrió un error inesperado"
            }
        }
    }

    private fun processColorPicker(product: ProductDetail, id: String) {

        val colorPicker = product.pickers
            ?.firstOrNull { it.pickerId == "COLOR" }

        if (colorPicker == null) {
            setDefaultImagesAndName(product)
            return
        }


        _colorPicker.value = colorPicker
        _colorPickerSelected.value = colorPicker.products?.firstOrNull()

        val selectedPictureId = _colorPickerSelected.value?.pictureId
        val colorPictures = product.pictures
            ?.filter { pic ->
                pic.suggestedForPicker?.contains("COLOR") == true &&
                        pic.id == selectedPictureId
            }
            .orEmpty()

        val picturesToShow = if (colorPictures.isNotEmpty()) {
            colorPictures
        } else {
            product.pictures
                ?.let { pictures ->
                    if (pictures.size >= 2)
                        pictures.filter { it.id != selectedPictureId }
                    else
                        pictures
                }
                .orEmpty()
        }


        _images.value = picturesToShow.mapNotNull { it.url }
        _productName.value = if (colorPictures.isNotEmpty())
            _colorPickerSelected.value?.productName ?: product.name
        else
            product.name

        _colorNameSelect.value = colorPicker.products
            ?.firstOrNull { it.productId == id }
            ?.pickerLabel
            .orEmpty()
    }

    private fun setDefaultImagesAndName(product: ProductDetail) {
        _images.value      = product.pictures?.mapNotNull { it.url } ?: emptyList()
        _productName.value = product.name
    }
}