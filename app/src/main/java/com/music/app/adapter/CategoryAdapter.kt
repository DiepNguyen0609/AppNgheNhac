package com.music.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.music.app.adapter.CategoryAdapter.CategoryViewHolder
import com.music.app.databinding.ItemCategoryBinding
import com.music.app.listener.IOnClickCategoryItemListener
import com.music.app.model.Category
import com.music.app.utils.GlideUtils.loadUrl

class CategoryAdapter(
        private val mListCategory: List<Category>?,
        private val iOnClickCategoryItemListener: IOnClickCategoryItemListener
        ) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemCategoryBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = mListCategory!![position]
        loadUrl(category.image, holder.mItemCategoryBinding.imgCategory)
        holder.mItemCategoryBinding.tvCategory.text = category.name
        holder.mItemCategoryBinding.layoutItem.setOnClickListener { iOnClickCategoryItemListener.onClickItemCategory(category) }
    }

    override fun getItemCount(): Int {
        return mListCategory?.size ?: 0
    }

    class CategoryViewHolder(
            val mItemCategoryBinding: ItemCategoryBinding
            ) : RecyclerView.ViewHolder(mItemCategoryBinding.root)
}