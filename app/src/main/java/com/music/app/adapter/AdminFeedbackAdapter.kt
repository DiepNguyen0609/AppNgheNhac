package com.music.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.music.app.adapter.AdminFeedbackAdapter.AdminFeedbackViewHolder
import com.music.app.databinding.ItemAdminFeedbackBinding
import com.music.app.model.Feedback

class AdminFeedbackAdapter(
        private val mListFeedback: List<Feedback>?
        ) : RecyclerView.Adapter<AdminFeedbackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminFeedbackViewHolder {
        val binding = ItemAdminFeedbackBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return AdminFeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminFeedbackViewHolder, position: Int) {
        val feedback = mListFeedback!![position]
        holder.mItemAdminFeedbackBinding.tvEmail.text = feedback.email
        holder.mItemAdminFeedbackBinding.tvFeedback.text = feedback.comment
    }

    override fun getItemCount(): Int {
        return mListFeedback?.size ?: 0
    }

    class AdminFeedbackViewHolder(
            val mItemAdminFeedbackBinding: ItemAdminFeedbackBinding
            ) : RecyclerView.ViewHolder(mItemAdminFeedbackBinding.root)
}