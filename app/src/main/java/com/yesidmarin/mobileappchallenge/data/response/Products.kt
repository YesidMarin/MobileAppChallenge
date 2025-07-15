package com.yesidmarin.mobileappchallenge.data.response

import com.google.gson.annotations.SerializedName

data class Products(
    @SerializedName("paging") val paging: Paging,
    @SerializedName("results") val results: List<Results>
) {
    data class Paging(
        @SerializedName("total") val total: Int,
        @SerializedName("offset") val offset: Int,
        @SerializedName("limit") val limit: Int
    )

    data class Results(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("pictures") val pictures: List<Pictures>,
    ) {
        data class Pictures(
            @SerializedName("id") val id: String,
            @SerializedName("url") val url: String,
        )
    }
}