package com.yesidmarin.mobileappchallenge.helpers

import com.yesidmarin.mobileappchallenge.data.response.Products
import kotlin.random.Random

fun dummyPicture(
    id: String = Random.nextInt(1000).toString(),
    url: String = "https://picsum.photos/200?id=$id"
) = Products.Results.Pictures(id, url)

fun dummyResult(
    id: String = "ML-${Random.nextInt(10000)}",
    name: String = "Producto $id",
    pictures: List<Products.Results.Pictures> = listOf(dummyPicture())
) = Products.Results(id, name, pictures)

fun dummyProducts(
    total: Int = 1,
    offset: Int = 0,
    limit: Int = 10,
    items: List<Products.Results> = List(total) { dummyResult() }
) = Products(
    paging  = Products.Paging(total, offset, limit),
    results = items
)