package com.chakaseptember.dicecodetest.ui.taglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chakaseptember.dicecodetest.model.TagListModel
import com.chakaseptember.dicecodetest.repository.TagRepo
import io.reactivex.schedulers.Schedulers

class TagListViewModel : ViewModel() {

    private val _tagListModel =  MutableLiveData<TagListModel>()
    val tagListModel:LiveData<TagListModel>
        get() = _tagListModel

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val disposable = TagRepo.getTags()
        .subscribeOn(Schedulers.io())
        .subscribe(
            {tagListModel -> _tagListModel.postValue(tagListModel)
                _showLoading.postValue(false)
                Log.d("success", "success")},
            {error -> Log.e("TagListViewModel", "error", error)})

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TagListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TagListViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}