package com.chakaseptember.dicecodetest.ui.tagdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chakaseptember.dicecodetest.model.TagsModel
import com.chakaseptember.dicecodetest.repository.TagRepo
import io.reactivex.schedulers.Schedulers

class TagDetailsViewModel(val tag:String): ViewModel() {

    private val _tagsModel =  MutableLiveData<TagsModel>()
    val tagsModel: MutableLiveData<TagsModel>
        get() = _tagsModel

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val disposable = TagRepo.getTag(tag)
        .subscribeOn(Schedulers.io())
        .subscribe(
            {tagModel -> _tagsModel.postValue(tagModel)
                _showLoading.postValue(false)},
            {error -> Log.e("TagListViewModel", "error", error)})

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    class Factory(val tag: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TagDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TagDetailsViewModel(tag) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}