package com.music.app.listener

import com.music.app.model.Artist

interface IOnAdminManagerArtistListener {
    fun onClickUpdateArtist(artist: Artist)
    fun onClickDeleteArtist(artist: Artist)
    fun onClickDetailArtist(artist: Artist)
}