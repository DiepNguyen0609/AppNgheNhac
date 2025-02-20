package com.music.app.fragment.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.music.app.MyApplication
import com.music.app.adapter.AdminFeedbackAdapter
import com.music.app.databinding.FragmentAdminFeedbackBinding
import com.music.app.model.Feedback

class AdminFeedbackFragment : Fragment() {

    private var binding: FragmentAdminFeedbackBinding? = null
    private var mListFeedback: MutableList<Feedback>? = null
    private var mFeedbackAdapter: AdminFeedbackAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAdminFeedbackBinding.inflate(inflater, container, false)
        initView()
        loadListFeedback()
        return binding?.root
    }

    private fun initView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        binding?.rcvFeedback?.layoutManager = linearLayoutManager
        mListFeedback = ArrayList()
        mFeedbackAdapter = AdminFeedbackAdapter(mListFeedback)
        binding?.rcvFeedback?.adapter = mFeedbackAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadListFeedback() {
        if (activity == null) return
        MyApplication[activity!!].feedbackDatabaseReference()
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        clearListFeedback()
                        for (dataSnapshot in snapshot.children) {
                            val feedback = dataSnapshot.getValue(Feedback::class.java)
                            if (feedback != null) {
                                mListFeedback!!.add(0, feedback)
                            }
                        }
                        if (mFeedbackAdapter != null) mFeedbackAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun clearListFeedback() {
        if (mListFeedback != null) {
            mListFeedback!!.clear()
        } else {
            mListFeedback = ArrayList()
        }
    }
}