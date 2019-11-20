package com.chakaseptember.dicecodetest.network

import com.chakaseptember.dicecodetest.model.TagListModel
import com.chakaseptember.dicecodetest.model.TagModel
import com.chakaseptember.dicecodetest.model.TagsModel
import com.chakaseptember.dicecodetest.network.dto.*
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface TronaldDumpEndPoint {

    @GET("/tag")
    fun getTags(): Single<TagListDto>

    @GET("/tag/{tag}")
    fun getTag(@Path("tag") tag:String): Single<TagDto>

}

object TronaldDumpNetwork{

    private const val endPoint = "https://api.tronalddump.io//"

    private val tronaldDumpEndPoint = Retrofit.Builder()
        .baseUrl(endPoint)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TronaldDumpEndPoint::class.java)

    fun getTags():Single<TagListModel> {
        return tronaldDumpEndPoint.getTags()
            .map { tagListDto: TagListDto -> TagListModel(tagListDto._embedded)   }
    }

    fun getTag(tag:String): Single<TagsModel> {
        return tronaldDumpEndPoint.getTag(tag)
            .map { tagDto: TagDto ->
                val tags = mutableListOf<TagModel>()
                for (tagDetails: TagDetailsDto in tagDto._embedded.tags){
                    val authors = StringBuilder()
                    for(author:AuthorDto in tagDetails._embedded.author){
                        authors.appendln(author.name)
                    }
                    val sources = StringBuilder()
                    for(source:SourceDto in tagDetails._embedded.source){
                        sources.appendln(source.url)
                    }
                    tags.add(TagModel(tagDetails.value,
                        tagDetails.tags,
                        authors.toString(),
                        sources.toString()))
                }
                TagsModel(tags)
            }
    }
}