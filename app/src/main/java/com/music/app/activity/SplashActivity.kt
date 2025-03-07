package com.music.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.music.app.constant.AboutUsConfig
import com.music.app.constant.GlobalFunction.startActivity
import com.music.app.databinding.ActivitySplashBinding
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.utils.StringUtil.isEmpty

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initUi()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ goToActivity() }, 2000)
    }

    private fun initUi() {
        binding?.tvAboutUsTitle?.text = AboutUsConfig.ABOUT_US_TITLE
        binding?.tvAboutUsSlogan?.text = AboutUsConfig.ABOUT_US_SLOGAN
    }

    private fun goToActivity() {
        if (user != null && !isEmpty(user!!.email)) {
            if (user!!.isAdmin) {
                startActivity(this, AdminMainActivity::class.java)
            } else {
                startActivity(this, MainActivity::class.java)
            }
        } else {
            startActivity(this, SignInActivity::class.java)
        }
        finish()
    }
}