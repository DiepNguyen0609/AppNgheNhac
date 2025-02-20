package com.music.app.listener

import com.music.app.model.Song

interface IOnClickSongItemListener {
    fun onClickItemSong(song: Song)
    fun onClickFavoriteSong(song: Song, favorite: Boolean)
    fun onClickMoreOptions(song: Song)
}