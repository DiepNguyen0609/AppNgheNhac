package com.music.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.music.app.constant.Constant
import com.music.app.constant.GlobalFunction

class MusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.extras!!.getInt(Constant.MUSIC_ACTION)
        GlobalFunction.startMusicService(context, action, MusicService.mSongPosition)
    }
}