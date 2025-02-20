package com.music.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.music.app.adapter.BannerSongAdapter.BannerSongViewHolder
import com.music.app.databinding.ItemBannerSongBinding
import com.music.app.listener.IOnClickSongItemListener
import com.music.app.model.Song
import com.music.app.utils.GlideUtils.loadUrlBanner

class BannerSongAdapter(
        private val mListSongs: List<Song>?,
        private val iOnClickSongItemListener: IOnClickSongItemListener
        ) : RecyclerView.Adapter<BannerSongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerSongViewHolder {
        val itemBannerSongBinding = ItemBannerSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerSongViewHolder(itemBannerSongBinding)
    }

    override fun onBindViewHolder(holder: BannerSongViewHolder, position: Int) {
        val song = mListSongs!![position]
        loadUrlBanner(song.image, holder.mItemBannerSongBinding.imageBanner)
        holder.mItemBannerSongBinding.layoutItem.setOnClickListener { iOnClickSongItemListener.onClickItemSong(song) }
    }

    override fun getItemCount(): Int {
        return mListSongs?.size ?: 0
    }

    class BannerSongViewHolder(
            val mItemBannerSongBinding: ItemBannerSongBinding
            ) : RecyclerView.ViewHolder(mItemBannerSongBinding.root)
}