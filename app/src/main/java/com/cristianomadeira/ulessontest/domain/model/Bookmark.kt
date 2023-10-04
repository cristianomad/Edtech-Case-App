package com.cristianomadeira.ulessontest.domain.model

data class Bookmark(
    val id: Int = 0,
    val lessonId: Int,
    val content: String,
    val position: Long
)
