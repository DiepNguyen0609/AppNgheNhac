package com.music.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.music.app.MyApplication
import com.music.app.R
import com.music.app.activity.MainActivity
import com.music.app.constant.GlobalFunction
import com.music.app.databinding.FragmentFeedbackBinding
import com.music.app.model.Feedback
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.utils.StringUtil.isEmpty

class FeedbackFragment : Fragment() {

    private var binding: FragmentFeedbackBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        binding?.edtEmail?.setText(user!!.email)
        binding?.tvSendFeedback?.setOnClickListener { onClickSendFeedback() }
        return binding?.root
    }

    private fun onClickSendFeedback() {
        if (activity == null) {
            return
        }
        val activity = activity as MainActivity?
        val strName = binding?.edtName?.text.toString()
        val strPhone = binding?.edtPhone?.text.toString()
        val strEmail = binding?.edtEmail?.text.toString()
        val strComment = binding?.edtComment?.text.toString()
        if (isEmpty(strName)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.name_require))
        } else if (isEmpty(strComment)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.comment_require))
        } else {
            activity!!.showProgressDialog(true)
            val feedback = Feedback(strName, strPhone, strEmail, strComment)
            MyApplication[getActivity()!!].feedbackDatabaseReference()
                    ?.child(System.currentTimeMillis().toString())
                    ?.setValue(feedback) { _: DatabaseError?, _: DatabaseReference? ->
                        activity.showProgressDialog(false)
                        sendFeedbackSuccess()
                    }
        }
    }

    private fun sendFeedbackSuccess() {
        GlobalFunction.hideSoftKeyboard(activity)
        GlobalFunction.showToastMessage(activity, getString(R.string.msg_send_feedback_success))
        binding?.edtName?.setText("")
        binding?.edtPhone?.setText("")
        binding?.edtComment?.setText("")
    }
}