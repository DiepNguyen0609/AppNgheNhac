package com.music.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.music.app.adapter.ArtistVerticalAdapter.ArtistVerticalViewHolder
import com.music.app.databinding.ItemArtistVerticalBinding
import com.music.app.listener.IOnClickArtistItemListener
import com.music.app.model.Artist
import com.music.app.utils.GlideUtils.loadUrl

class ArtistVerticalAdapter(
        private val mListArtist: List<Artist>?,
        private val iOnClickArtistItemListener: IOnClickArtistItemListener
        ) : RecyclerView.Adapter<ArtistVerticalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistVerticalViewHolder {
        val itemArtistVerticalBinding = ItemArtistVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistVerticalViewHolder(itemArtistVerticalBinding)
    }

    override fun onBindViewHolder(holder: ArtistVerticalViewHolder, position: Int) {
        val artist = mListArtist!![position]
        loadUrl(artist.image, holder.mItemArtistVerticalBinding.imgArtist)
        holder.mItemArtistVerticalBinding.tvArtist.text = artist.name
        holder.mItemArtistVerticalBinding.layoutItem.setOnClickListener { iOnClickArtistItemListener.onClickItemArtist(artist) }
    }

    override fun getItemCount(): Int {
        return mListArtist?.size ?: 0
    }

    class ArtistVerticalViewHolder(
            val mItemArtistVerticalBinding: ItemArtistVerticalBinding
            ) : RecyclerView.ViewHolder(mItemArtistVerticalBinding.root)
}