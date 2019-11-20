package com.chakaseptember.dicecodetest.model

data class TagsModel(
    val tags:List<TagModel>
)

data class TagModel(
    val value:String,
    val tags:List<String>,
    val author:String,
    val sourceUrl:String
)

