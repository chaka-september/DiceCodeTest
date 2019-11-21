package com.chakaseptember.dicecodetest.network.dto

import com.squareup.moshi.Json

data class TagListDto(
    val count: Int,
    val total: Int,
    @field:Json(name = "_embedded")
    val tags: List<String>,
    val _link: LinkDto)

data class LinkDto(
    val self: SelfDto
)

data class SelfDto(
    val href: String
)