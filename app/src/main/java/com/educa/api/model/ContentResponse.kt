package com.educa.api.model

data class ContentResponseArray(
    val content: List<ContentResponse>
)

data class ContentResponse(
    val idConteudo: Int,
    val titulo: String,
    val tempoEstimado: String,
    val habilidade: Ability,
    val texto: String,
    val urlVideo: String,
    val usuario: User,
    val dataCriacao: String,
    val avaliacoes: List<RatingResponse>
)
