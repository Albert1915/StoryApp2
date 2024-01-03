package com.alz19.storyapp2.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alz19.storyapp2.R
import com.alz19.storyapp2.databinding.ActivityLoginBinding
import com.alz19.storyapp2.helper.factory.ViewModelFactory
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.view.main.MainActivity
import com.alz19.storyapp2.view.register.RegisterActivity
import com.alz19.storyapp2.view.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityLoginBinding.root)
        setupAction()
        setupView()
        playAnimation()


    }

    private fun playAnimation() {
        val binding = activityLoginBinding

        ObjectAnimator.ofFloat(binding.imageLoginIllustration, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val titleMessage = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val inputEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val inputPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val loginButton = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, titleMessage, email, inputEmail, password, inputPassword, loginButton)
            start()
        }
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

    private fun setupAction() {

        activityLoginBinding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        activityLoginBinding.editLoginEmail.message.observe(this) {
            activityLoginBinding.emailEditTextLayout.error = it
        }

        activityLoginBinding.editLoginPassword.message.observe(this) {
            activityLoginBinding.passwordEditTextLayout.error = it
        }

        activityLoginBinding.textButtonLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }



        loginViewModel.token.observe(this) {
            if (!it.isNullOrEmpty()) {
                Log.i("LoginActivity", it)
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.user.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(result.state)
                is Result.Failure -> showErrorDialog(result.throwable)
                is Result.Success -> {
                    loginViewModel.saveSession(result.data.loginResult?.token.toString())
                }
            }
        }

        activityLoginBinding.buttonLogin.setOnClickListener {
            if (activityLoginBinding.editLoginEmail.text.isNullOrEmpty()){
                activityLoginBinding.emailEditTextLayout.error = resources.getString(R.string.empty_text)
            }
            if (activityLoginBinding.editLoginPassword.text.isNullOrEmpty()){
                activityLoginBinding.passwordEditTextLayout.error = resources.getString(R.string.empty_text)
            }

            if (activityLoginBinding.emailEditTextLayout.error != null || activityLoginBinding.passwordEditTextLayout.error != null) {
                Toast.makeText(this, resources.getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginViewModel.loginUser(
                activityLoginBinding.editLoginEmail.text.toString(),
                activityLoginBinding.editLoginPassword.text.toString()
            )
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("Oke") { _, _ ->
                val intent = Intent(context, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityLoginBinding.progressLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        activityLoginBinding.loginLayoutComponent.visibility =  if (isLoading) View.GONE else View.VISIBLE
    }

    private fun obtainViewModel(context: Context): LoginViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun initComponent() {
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel = obtainViewModel(this)
    }


}