package com.chakaseptember.dicecodetest.network.dto

data class TagDto(
    val count: Int,
    val total: Int,
    val _embedded: TagsDto,
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
    val _embedded: MoreTagDetailsDto
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