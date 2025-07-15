package com.yesidmarin.mobileappchallenge.helpers

import com.yesidmarin.mobileappchallenge.data.response.ProductDetail
import kotlin.random.Random


fun dummyPictureDetail(
    id: String = "pic${Random.nextInt(1000)}",
    url: String = "https://picsum.photos/500?id=$id"
) = ProductDetail.Picture(id, url, 500, 500, listOf("COLOR"))

fun dummyPickerProduct(
    prodId: String = "ML${Random.nextInt(10_000)}",
    picId: String = "pic$prodId"
) = ProductDetail.Pickers.Products(
    productId   = prodId,
    pictureId   = picId,
    pickerLabel = "Rojo",
    thumbnail   = "https://picsum.photos/100?id=$picId",
    productName = "Producto $prodId"
)

fun dummyPicker(
    id: String = "COLOR",
    name: String = "Color",
    products: List<ProductDetail.Pickers.Products> = listOf(dummyPickerProduct())
) = ProductDetail.Pickers(id, name, products)

fun dummyProductDetail(
    id: String = "ML${Random.nextInt(10_000)}",
    pictures: List<ProductDetail.Picture> = listOf(dummyPictureDetail()),
    pickers: List<ProductDetail.Pickers> = listOf(dummyPicker()),
    mainFeatures: List<ProductDetail.MainFeature> = listOf(
        ProductDetail.MainFeature("Pantalla OLED 6.4 ''")
    ),
    attributes: List<ProductDetail.Attribute> = listOf(
        ProductDetail.Attribute("Marca", "Genérica")
    ),
    shortDescription: ProductDetail.ShortDescription =
        ProductDetail.ShortDescription("Un producto de prueba")
) = ProductDetail(
    id = id,
    name = "Producto $id",
    pictures = pictures,
    pickers = pickers,
    mainFeatures = mainFeatures,
    attributes = attributes,
    shortDescription = shortDescription
)