package com.chakaseptember.dicecodetest.network.dto

import com.squareup.moshi.Json

data class TagDto(
    val count: Int,
    val total: Int,
    @field:Json(name = "_embedded")
    val tagDetailsList: TagsDto,
    val _links: LinkDto
)

data class TagsDto(
    val tags: List<TagDetailsDto>
)

data class TagDetailsDto(
    val appeared_at: String,
    val created_at: String,
    val quote_id: String,
    val tags: List<String>,
    val value: String,
    @field:Json(name = "_embedded")
    val moreTagDetails: MoreTagDetailsDto
)

data class MoreTagDetailsDto(
    val author:List<AuthorDto>,
    val source:List<SourceDto>
)

data class AuthorDto(
    val name:String
)

data class SourceDto (
    val url:String
)