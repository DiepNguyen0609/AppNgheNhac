package com.music.app.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.music.app.R
import com.music.app.adapter.MusicViewPagerAdapter
import com.music.app.constant.Constant
import com.music.app.constant.GlobalFunction.startDownloadFile
import com.music.app.constant.GlobalFunction.startMusicService
import com.music.app.databinding.ActivityPlayMusicBinding
import com.music.app.model.Song
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.service.MusicService

class PlayMusicActivity : BaseActivity() {

    private var mSong: Song? = null
    private var binding: ActivityPlayMusicBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayMusicBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initToolbar()
        initUI()
    }

    private fun initToolbar() {
        binding?.toolbar?.imgLeft?.setImageResource(R.drawable.ic_back_white)
        binding?.toolbar?.tvTitle?.setText(R.string.music_player)
        binding?.toolbar?.layoutPlayAll?.visibility = View.GONE
        binding?.toolbar?.imgLeft?.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        if (user!!.isAdmin) {
            startMusicService(this@PlayMusicActivity,
                    Constant.CANCEL_NOTIFICATION, MusicService.mSongPosition)
        }
        super.onBackPressed()
    }

    private fun initUI() {
        val musicViewPagerAdapter = MusicViewPagerAdapter(this)
        binding?.viewpager2?.adapter = musicViewPagerAdapter
        binding?.indicator3?.setViewPager(binding?.viewpager2)
        binding?.viewpager2?.currentItem = 1
    }

    fun downloadSong(song: Song?) {
        mSong = song
        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_PERMISSION_CODE)
            } else {
                startDownloadFile(this, mSong)
            }
        } else {
            startDownloadFile(this, mSong)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloadFile(this, mSong)
            } else {
                Toast.makeText(this, getString(R.string.msg_permission_denied),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 10
    }
}