package com.music.app.listener

interface IOnClickSongPlayingItemListener {
    fun onClickItemSongPlaying(position: Int)
    fun onClickRemoveFromPlaylist(position: Int)
}