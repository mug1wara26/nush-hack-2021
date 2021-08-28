package com.example.nush_hack21.model

data class SerpapiResponse(
    val shopping_results : List<Result>
) {
    data class Result(
        val position: Int,
        val title: String,
        val link: String,
        val price: String,
        val extractedPrice: String,
        val thumbnail: String,
    )
}