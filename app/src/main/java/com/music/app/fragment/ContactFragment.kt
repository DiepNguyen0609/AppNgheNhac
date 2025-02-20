package com.music.app.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.music.app.R
import com.music.app.adapter.ContactAdapter
import com.music.app.constant.AboutUsConfig
import com.music.app.constant.GlobalFunction
import com.music.app.databinding.FragmentContactBinding
import com.music.app.model.Contact

class ContactFragment : Fragment() {

    private var binding: FragmentContactBinding? = null
    private var mContactAdapter: ContactAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        initUi()
        return binding?.root
    }

    private fun initUi() {
        binding?.tvAboutUsTitle?.text = AboutUsConfig.ABOUT_US_TITLE
        binding?.tvAboutUsContent?.text = AboutUsConfig.ABOUT_US_CONTENT
        val layoutManager = GridLayoutManager(activity, 3)
        binding?.rcvData?.isNestedScrollingEnabled = false
        binding?.rcvData?.isFocusable = false
        binding?.rcvData?.layoutManager = layoutManager
        binding?.rcvData?.adapter = mContactAdapter
    }


    private fun loadListContact(): List<Contact> {
        val contactArrayList: MutableList<Contact> = ArrayList()
        contactArrayList.add(Contact(Contact.FACEBOOK, R.drawable.ic_facebook))
        contactArrayList.add(Contact(Contact.GMAIL, R.drawable.ic_gmail))
        return contactArrayList
    }

    override fun onDestroy() {
        super.onDestroy()
        mContactAdapter?.release()
    }
}