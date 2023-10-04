package com.cristianomadeira.ulessontest.domain.model

data class Chapter(
    val id: Int,
    val enumeration: Int,
    val title: String,
    val lessons: List<Lesson>
)
