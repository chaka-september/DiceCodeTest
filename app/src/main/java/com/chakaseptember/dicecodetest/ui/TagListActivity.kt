package com.chakaseptember.dicecodetest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chakaseptember.dicecodetest.R
import com.chakaseptember.dicecodetest.repository.TagRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_list.*

class TagListActivity : AppCompatActivity() {

    private val TAG = "TagListActivity"
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_list)

        linearLayoutManager = LinearLayoutManager(this)
        tagListRecyclerView.layoutManager = linearLayoutManager
        loadTags()
    }

    private fun loadTags() {
        disposables.add(TagRepo.getTags()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tagListProgressBar.visibility = View.GONE
                tagListRecyclerView.adapter = TagAdapter(it.tagList){
                    val intent = Intent(this, TagDetailsActivity::class.java)
                    intent.putExtra(EXTRA_TAG_STRING, it)
                    startActivity(intent)
                }
            }, {
                Log.e(TAG, "loadTags: ", it)
            }))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}

class TagAdapter(val tags: List<String>, val clickListener: (String) -> Unit) : RecyclerView.Adapter<TagViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tag,parent,false))
    }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tagItemTextView.text = tags[position]
        holder.tagCardView.setOnClickListener(){
            clickListener(tags[position])
        }
    }
}

class TagViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val tagItemTextView = v.findViewById<TextView>(R.id.tagItemTextView)
    val tagCardView = v

}

