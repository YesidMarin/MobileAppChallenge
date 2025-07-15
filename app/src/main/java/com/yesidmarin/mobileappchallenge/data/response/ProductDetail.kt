package com.yesidmarin.mobileappchallenge.data.response

import com.google.gson.annotations.SerializedName

data class ProductDetail(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("pictures") val pictures: List<Picture>?,
    @SerializedName("pickers") val pickers: List<Pickers>?,
    @SerializedName("main_features") val mainFeatures: List<MainFeature>?,
    @SerializedName("attributes") val attributes: List<Attribute>?,
    @SerializedName("short_description") val shortDescription: ShortDescription?,
) {
    data class Picture(
        @SerializedName("id") val id: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("max_width") val maxWidth: Int?,
        @SerializedName("max_height") val maxHeight: Int?,
        @SerializedName("suggested_for_picker") val suggestedForPicker: List<String>?
    )

    data class Pickers(
        @SerializedName("picker_id") val pickerId: String?,
        @SerializedName("picker_name") val pickerName: String?,
        @SerializedName("products") val products: List<Products>?
    ) {
        data class Products(
            @SerializedName("product_id") val productId: String?,
            @SerializedName("picture_id") val pictureId: String?,
            @SerializedName("picker_label") val pickerLabel: String?,
            @SerializedName("thumbnail") val thumbnail: String?,
            @SerializedName("product_name") val productName: String?,
        )
    }

    data class MainFeature(
        @SerializedName("text") val text: String?
    )

    data class Attribute(
        @SerializedName("name") val name: String?,
        @SerializedName("value_name") val valueName: String?
    )

    data class ShortDescription(
        @SerializedName("content") val text: String?
    )
}