package com.chakaseptember.dicecodetest.repository

import com.chakaseptember.dicecodetest.model.TagListModel
import com.chakaseptember.dicecodetest.model.TagsModel
import com.chakaseptember.dicecodetest.network.TronaldDumpNetwork
import io.reactivex.Single

object TagRepo {

    fun getTags(): Single<TagListModel> {
        return TronaldDumpNetwork.getTags()
    }

    fun getTag(tag:String): Single<TagsModel> {
        return TronaldDumpNetwork.getTag(tag)
    }
}