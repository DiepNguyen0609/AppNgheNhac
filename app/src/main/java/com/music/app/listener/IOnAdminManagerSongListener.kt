package com.music.app.listener

import com.music.app.model.Song

interface IOnAdminManagerSongListener {
    fun onClickUpdateSong(song: Song)
    fun onClickDeleteSong(song: Song)
    fun onClickDetailSong(song: Song)
}