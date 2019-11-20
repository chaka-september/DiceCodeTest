package com.chakaseptember.dicecodetest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chakaseptember.dicecodetest.R
import com.chakaseptember.dicecodetest.model.TagModel
import com.chakaseptember.dicecodetest.repository.TagRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_details.*

const val EXTRA_TAG_STRING = "com.chakaseptember.dicecodetest.ui.EXTRA_TAG_STRING"

class TagDetailsActivity : AppCompatActivity() {
    lateinit var tag: String
    private val TAG = "TagDetailsActivity"
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_details)

        tag = intent.extras?.getString(EXTRA_TAG_STRING)!!
        title = tag
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        linearLayoutManager = LinearLayoutManager(this)
        tagDetailsRecyclerView.layoutManager = linearLayoutManager


        loadTagDetails(tag)
    }

    private fun loadTagDetails(tag: String) {
        disposables.add(TagRepo.getTag(tag).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tagDetailsRecyclerView.adapter = TagDetailsAdapter(it.tags)
                tagDetailsProgressBar.visibility = View.GONE
            }, {
                Log.e(TAG, "loadTags: ", it)
            }))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

class TagDetailsAdapter(val tagDetails: List<TagModel>) : RecyclerView.Adapter<TagDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagDetailsViewHolder {
        return TagDetailsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tag_details,parent,false))
    }

    override fun getItemCount() = tagDetails.size

    override fun onBindViewHolder(holder: TagDetailsViewHolder, position: Int) {
        val tagModel:TagModel = tagDetails[position]
        holder.valueTextView.text = tagModel.value
        holder.tagsTextView.text = tagModel.tags.toString()
        holder.authorTextView.text = tagModel.author
        holder.soureTextView.text = tagModel.sourceUrl
    }
}

class TagDetailsViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val valueTextView = v.findViewById<TextView>(R.id.valueTextView)
    val tagsTextView = v.findViewById<TextView>(R.id.tagsTextView)
    val authorTextView = v.findViewById<TextView>(R.id.authorTextView)
    val soureTextView = v.findViewById<TextView>(R.id.sourceTextView)



}
