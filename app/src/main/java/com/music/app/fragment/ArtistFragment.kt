package com.music.app.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.music.app.MyApplication
import com.music.app.R
import com.music.app.activity.MainActivity
import com.music.app.adapter.ArtistVerticalAdapter
import com.music.app.constant.Constant
import com.music.app.constant.GlobalFunction
import com.music.app.databinding.FragmentArtistBinding
import com.music.app.listener.IOnClickArtistItemListener
import com.music.app.model.Artist

class ArtistFragment : Fragment() {

    private var binding: FragmentArtistBinding? = null
    private var mListArtist: MutableList<Artist>? = null
    private var mArtistVerticalAdapter: ArtistVerticalAdapter? = null
    @JvmField
    var mIsFromMenuLeft = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        loadDataIntent()
        initUi()
        loadListAllArtist()
        return binding?.root
    }

    private fun loadDataIntent() {
        val bundle = arguments ?: return
        mIsFromMenuLeft = bundle.getBoolean(Constant.IS_FROM_MENU_LEFT)
    }

    private fun initUi() {
        if (activity == null) return
        val gridLayoutManager = GridLayoutManager(activity, 2)
        binding?.rcvData?.layoutManager = gridLayoutManager
        mListArtist = ArrayList()
        mArtistVerticalAdapter = ArtistVerticalAdapter(mListArtist, object : IOnClickArtistItemListener {
            override fun onClickItemArtist(artist: Artist) {
                val mainActivity = activity as MainActivity?
                mainActivity?.clickOpenSongsByArtist(artist)
            }
        })
        binding?.rcvData?.adapter = mArtistVerticalAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadListAllArtist() {
        if (activity == null) return
        MyApplication[activity!!].artistDatabaseReference()
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (mListArtist == null) {
                            mListArtist = ArrayList()
                        } else {
                            mListArtist!!.clear()
                        }
                        for (dataSnapshot in snapshot.children) {
                            val artist = dataSnapshot.getValue(Artist::class.java) ?: return
                            mListArtist!!.add(0, artist)
                        }
                        if (mArtistVerticalAdapter != null) mArtistVerticalAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        GlobalFunction.showToastMessage(activity, getString(R.string.msg_get_date_error))
                    }
                })
    }

    companion object {
        @JvmStatic
        fun newInstance(isFromMenuLeft: Boolean): ArtistFragment {
            val fragment = ArtistFragment()
            val bundle = Bundle()
            bundle.putBoolean(Constant.IS_FROM_MENU_LEFT, isFromMenuLeft)
            fragment.arguments = bundle
            return fragment
        }
    }
}