package com.alz19.storyapp2.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.alz19.storyapp2.R
import com.alz19.storyapp2.databinding.ActivityWelcomeBinding
import com.alz19.storyapp2.view.login.LoginActivity
import com.alz19.storyapp2.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var activityWelcomeBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityWelcomeBinding.root)
        setupAction()
        setupView()
        playAnimation()
    }

    private fun playAnimation() {
        val binding = activityWelcomeBinding

        ObjectAnimator.ofFloat(activityWelcomeBinding.imageWelcome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(300)
        val signup = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.textWelcomeTitle, View.ALPHA, 1f).setDuration(300)
        val desc = ObjectAnimator.ofFloat(binding.textWelcomeDesc, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, desc, login, signup)
            start()
        }
    }

    private fun setupAction() {
        activityWelcomeBinding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        activityWelcomeBinding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        activityWelcomeBinding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun initComponent(){
        activityWelcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}