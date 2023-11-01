package com.educa.api.model

data class RatingResponse(
    val idAvaliacao: Int,
    val avaliacao: String,
    val usuario: User,
)
