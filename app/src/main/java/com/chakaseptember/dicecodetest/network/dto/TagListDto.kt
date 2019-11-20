package com.chakaseptember.dicecodetest.network.dto

data class TagListDto(
    val count: Int,
    val total: Int,
    val _embedded: List<String>,
    val _link: LinkDto)

data class LinkDto(
    val self: SelfDto
)

data class SelfDto(
    val href: String
)