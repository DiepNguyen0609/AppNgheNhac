package com.music.app.fragment

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
import com.music.app.R
import com.music.app.activity.MainActivity
import com.music.app.activity.PlayMusicActivity
import com.music.app.adapter.SongAdapter
import com.music.app.constant.Constant
import com.music.app.constant.GlobalFunction
import com.music.app.databinding.FragmentFavoriteBinding
import com.music.app.listener.IOnClickSongItemListener
import com.music.app.model.Song
import com.music.app.service.MusicService
import com.music.app.service.MusicService.Companion.clearListSongPlaying

class FavoriteFragment : Fragment() {

    private var binding: FragmentFavoriteBinding? = null
    private var mListSong: MutableList<Song>? = null
    private var mSongAdapter: SongAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        initUi()
        initListener()
        loadListFavoriteSongs()
        return binding?.root
    }

    private fun initUi() {
        val linearLayoutManager = LinearLayoutManager(activity)
        binding?.rcvData?.layoutManager = linearLayoutManager
        mListSong = ArrayList()
        mSongAdapter = SongAdapter(mListSong, object : IOnClickSongItemListener {
            override fun onClickItemSong(song: Song) {
                goToSongDetail(song)
            }

            override fun onClickFavoriteSong(song: Song, favorite: Boolean) {
                GlobalFunction.onClickFavoriteSong(activity, song, favorite)
            }

            override fun onClickMoreOptions(song: Song) {
                GlobalFunction.handleClickMoreOptions(activity, song)
            }
        })
        binding?.rcvData?.adapter = mSongAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadListFavoriteSongs() {
        if (activity == null) return
        MyApplication[activity!!].songsDatabaseReference()
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        resetListData()
                        for (dataSnapshot in snapshot.children) {
                            val song = dataSnapshot.getValue(Song::class.java) ?: return
                            if (GlobalFunction.isFavoriteSong(song)) {
                                mListSong!!.add(0, song)
                            }
                        }
                        if (mSongAdapter != null) mSongAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        GlobalFunction.showToastMessage(activity, getString(R.string.msg_get_date_error))
                    }
                })
    }

    private fun resetListData() {
        if (mListSong == null) {
            mListSong = ArrayList()
        } else {
            mListSong!!.clear()
        }
    }

    private fun goToSongDetail(song: Song) {
        clearListSongPlaying()
        MusicService.mListSongPlaying!!.add(song)
        MusicService.isPlaying = false
        GlobalFunction.startMusicService(activity, Constant.PLAY, 0)
        GlobalFunction.startActivity(activity, PlayMusicActivity::class.java)
    }

    private fun initListener() {
        val activity = activity as MainActivity?
        if (activity?.activityMainBinding == null) {
            return
        }
        activity.activityMainBinding!!.header.layoutPlayAll.setOnClickListener {
            if (mListSong == null || mListSong!!.isEmpty()) return@setOnClickListener
            clearListSongPlaying()
            MusicService.mListSongPlaying!!.addAll(mListSong!!)
            MusicService.isPlaying = false
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0)
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity::class.java)
        }
    }
}