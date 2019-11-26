package com.chakaseptember.dicecodetest.ui.tagdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chakaseptember.dicecodetest.R
import com.chakaseptember.dicecodetest.databinding.ActivityTagListBinding
import com.chakaseptember.dicecodetest.databinding.ItemTagDetailsBinding
import com.chakaseptember.dicecodetest.model.TagsModel
import kotlinx.android.synthetic.main.activity_tag_details.*

const val EXTRA_TAG_STRING = "com.chakaseptember.dicecodetest.ui.tagdetails.EXTRA_TAG_STRING"

class TagDetailsActivity : AppCompatActivity() {
    lateinit var tag: String
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var tagDetailsAdapter: TagDetailsAdapter

    private val viewModel: TagDetailsViewModel by lazy {
        ViewModelProviders.of(this, TagDetailsViewModel.Factory(tag)).get(TagDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_details)

        tag = intent.extras?.getString(EXTRA_TAG_STRING)!!

        setupActionBar()
        setupRecyclerView()
        setupBinding()
        setupObservers()
    }

    private fun setupActionBar() {
        title = tag
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        tagDetailsRecyclerView.layoutManager = linearLayoutManager

        tagDetailsAdapter = TagDetailsAdapter()
        tagDetailsRecyclerView.adapter = tagDetailsAdapter
    }

    private fun setupBinding() {
        var binding = ActivityTagListBinding.inflate(layoutInflater)
        binding.tagListRecyclerView
        binding.root.findViewById<RecyclerView>(R.id.tagListRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tagDetailsAdapter
        }
    }

    private fun setupObservers(){
        viewModel.tagsModel.observe(this, Observer { tagsModel ->
            tagsModel?.apply {
                tagDetailsAdapter.tagsModel = tagsModel
            }
        })

        viewModel.showLoading.observe(this, Observer { showLoading ->
            if(showLoading)tagDetailsProgressBar.visibility = View.VISIBLE
            else tagDetailsProgressBar.visibility = View.GONE
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

class TagDetailsAdapter() : RecyclerView.Adapter<TagDetailsViewHolder>() {

    var tagsModel: TagsModel = TagsModel(emptyList())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagDetailsViewHolder {

        val withDataBinding: ItemTagDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            TagDetailsViewHolder.LAYOUT,
            parent,
            false
        )
        return TagDetailsViewHolder(withDataBinding)
    }

    override fun getItemCount() = tagsModel.tags.size

    override fun onBindViewHolder(holder: TagDetailsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.tag = tagsModel.tags[position]
        }
    }
}


class TagDetailsViewHolder(val viewDataBinding: ItemTagDetailsBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tag_details
    }
}
