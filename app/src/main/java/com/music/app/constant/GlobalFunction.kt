package com.music.app.constant

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.music.app.MyApplication
import com.music.app.R
import com.music.app.activity.MainActivity
import com.music.app.activity.PlayMusicActivity
import com.music.app.databinding.LayoutBottomSheetOptionBinding
import com.music.app.model.Song
import com.music.app.model.UserInfor
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.service.MusicReceiver
import com.music.app.service.MusicService
import com.music.app.service.MusicService.Companion.clearListSongPlaying
import com.music.app.service.MusicService.Companion.deleteSongFromPlaylist
import com.music.app.service.MusicService.Companion.isSongExist
import com.music.app.service.MusicService.Companion.isSongPlaying
import com.music.app.utils.GlideUtils.loadUrl
import com.music.app.utils.StringUtil.isEmpty
import java.text.Normalizer
import java.util.regex.Pattern

object GlobalFunction {

    fun startActivity(context: Context?, clz: Class<*>?) {
        val intent = Intent(context, clz)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

    @JvmStatic
    fun startActivity(context: Context?, clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, clz)
        intent.putExtras(bundle!!)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity?) {
        try {
            val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
        }
    }

    @JvmStatic
    fun onClickOpenGmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", AboutUsConfig.GMAIL, null))
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }


    fun showToastMessage(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getTextSearch(input: String?): String {
        val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

    @JvmStatic
    fun startMusicService(ctx: Context?, action: Int, songPosition: Int) {
        val musicService = Intent(ctx, MusicService::class.java)
        musicService.putExtra(Constant.MUSIC_ACTION, action)
        musicService.putExtra(Constant.SONG_POSITION, songPosition)
        ctx?.startService(musicService)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun openMusicReceiver(ctx: Context, action: Int): PendingIntent {
        val intent = Intent(ctx, MusicReceiver::class.java)
        intent.putExtra(Constant.MUSIC_ACTION, action)
        val pendingFlag: Int = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        return PendingIntent.getBroadcast(ctx.applicationContext, action, intent, pendingFlag)
    }

    @JvmStatic
    fun isFavoriteSong(song: Song): Boolean {
        if (song.favorite == null || song.favorite!!.isEmpty()) return false
        val listUsersFavorite: List<UserInfor> = ArrayList(song.favorite!!.values)
        if (listUsersFavorite.isEmpty()) return false
        for (userInfo in listUsersFavorite) {
            if (user!!.email == userInfo.emailUser) {
                return true
            }
        }
        return false
    }

    private fun getUserFavoriteSong(song: Song): UserInfor? {
        var userInfo: UserInfor? = null
        if (song.favorite == null || song.favorite!!.isEmpty()) return null
        val listUsersFavorite: List<UserInfor> = ArrayList(song.favorite!!.values)
        if (listUsersFavorite.isEmpty()) return null
        for (userObject in listUsersFavorite) {
            if (user!!.email == userObject.emailUser) {
                userInfo = userObject
                break
            }
        }
        return userInfo
    }

    fun onClickFavoriteSong(context: Context?, song: Song, isFavorite: Boolean) {
        if (context == null) return
        if (isFavorite) {
            val userEmail = user!!.email
            val userInfo = UserInfor(System.currentTimeMillis(), userEmail)
            MyApplication[context].songsDatabaseReference()
                    ?.child(song.id.toString())
                    ?.child("favorite")
                    ?.child(userInfo.id.toString())
                    ?.setValue(userInfo)
        } else {
            val userInfo = getUserFavoriteSong(song)
            if (userInfo != null) {
                MyApplication[context].songsDatabaseReference()
                        ?.child(song.id.toString())
                        ?.child("favorite")
                        ?.child(userInfo.id.toString())
                        ?.removeValue()
            }
        }
    }

    @SuppressLint("InflateParams")
    fun handleClickMoreOptions(context: Activity?, song: Song?) {
        if (context == null || song == null) return
        val binding = LayoutBottomSheetOptionBinding
                .inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        loadUrl(song.image, binding.imgSong)
        binding.tvSongName.text = song.title
        binding.tvArtist.text = song.artist

        if (isSongExist(song.id)) {
            binding.layoutRemovePlaylist.visibility = View.VISIBLE
            binding.layoutPriority.visibility = View.VISIBLE
            binding.layoutAddPlaylist.visibility = View.GONE
        } else {
            binding.layoutRemovePlaylist.visibility = View.GONE
            binding.layoutPriority.visibility = View.GONE
            binding.layoutAddPlaylist.visibility = View.VISIBLE
        }

        binding.layoutDownload.setOnClickListener {
            val mainActivity = context as MainActivity
            mainActivity.downloadSong(song)
            bottomSheetDialog.hide()
        }

        binding.layoutPriority.setOnClickListener {
            if (isSongPlaying(song.id)) {
                showToastMessage(context, context.getString(R.string.msg_song_playing))
            } else {
                for (songEntity in MusicService.mListSongPlaying!!) {
                    songEntity.isPriority = songEntity.id == song.id
                }
                showToastMessage(context, context.getString(R.string.msg_setting_priority_successfully))
            }
            bottomSheetDialog.hide()
        }

        binding.layoutAddPlaylist.setOnClickListener {
            if (MusicService.mListSongPlaying == null || MusicService.mListSongPlaying!!.isEmpty()) {
                clearListSongPlaying()
                MusicService.mListSongPlaying!!.add(song)
                MusicService.isPlaying = false
                startMusicService(context, Constant.PLAY, 0)
                startActivity(context, PlayMusicActivity::class.java)
            } else {
                MusicService.mListSongPlaying!!.add(song)
                showToastMessage(context, context.getString(R.string.msg_add_song_playlist_success))
            }
            bottomSheetDialog.hide()
        }

        binding.layoutRemovePlaylist.setOnClickListener {
            if (isSongPlaying(song.id)) {
                showToastMessage(context, context.getString(R.string.msg_cannot_delete_song))
            } else {
                deleteSongFromPlaylist(song.id)
                showToastMessage(context, context.getString(R.string.msg_delete_song_from_playlist_success))
            }
            bottomSheetDialog.hide()
        }

        bottomSheetDialog.show()
    }

    @JvmStatic
    fun startDownloadFile(activity: Activity?, song: Song?) {
        if (activity == null || song == null || isEmpty(song.url)) return
        val request = DownloadManager.Request(Uri.parse(song.url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(activity.getString(R.string.title_download))
        request.setDescription(activity.getString(R.string.message_download))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val fileName = song.title + ".mp3"
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}