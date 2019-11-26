package com.chakaseptember.dicecodetest.ui.taglist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.chakaseptember.dicecodetest.databinding.ItemTagBinding
import com.chakaseptember.dicecodetest.model.TagListModel
import com.chakaseptember.dicecodetest.ui.tagdetails.EXTRA_TAG_STRING
import com.chakaseptember.dicecodetest.ui.tagdetails.TagDetailsActivity
import kotlinx.android.synthetic.main.activity_tag_list.*

class TagListActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var tagAdapter: TagAdapter

    private val viewModel: TagListViewModel by lazy {
        ViewModelProviders.of(this, TagListViewModel.Factory()).get(TagListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_list)

        setupRecyclerView()
        setupBinding()
        setupObservers()
    }

    private fun setupRecyclerView(){
        linearLayoutManager = LinearLayoutManager(this)
        tagListRecyclerView.layoutManager = linearLayoutManager

        tagAdapter = TagAdapter {
            val intent = Intent(this, TagDetailsActivity::class.java)
            intent.putExtra(EXTRA_TAG_STRING, it)
            startActivity(intent)
        }

        tagListRecyclerView.adapter=tagAdapter
    }

    private fun setupBinding(){
        var binding = ActivityTagListBinding.inflate(layoutInflater)
        binding.tagListRecyclerView
        binding.root.findViewById<RecyclerView>(R.id.tagListRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tagAdapter
        }
    }

    private fun setupObservers(){
        viewModel.tagListModel.observe(this, Observer { tagListModel ->
            tagListModel?.apply {
                tagAdapter.tagListModel = tagListModel
            }
        })

        viewModel.showLoading.observe(this, Observer { showLoading ->
            if(showLoading)tagListProgressBar.visibility = View.VISIBLE
            else tagListProgressBar.visibility = View.GONE
        })
    }
}

class TagAdapter(val clickListener: (String) -> Unit) : RecyclerView.Adapter<TagViewHolder>() {

    var tagListModel:TagListModel = TagListModel(emptyList())
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {

        val withDataBinding: ItemTagBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            TagViewHolder.LAYOUT,
            parent,
            false
        )
        return TagViewHolder(withDataBinding)
    }

    override fun getItemCount() = tagListModel.tagList.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.tag = tagListModel.tagList[position]
        }
        holder.itemView.setOnClickListener{clickListener(tagListModel.tagList[position].tag)}
    }
}


class TagViewHolder(val viewDataBinding: ItemTagBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tag
    }
}

