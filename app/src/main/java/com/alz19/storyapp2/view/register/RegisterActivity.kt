package com.alz19.storyapp2.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.alz19.storyapp2.R
import com.alz19.storyapp2.databinding.ActivityRegisterBinding
import com.alz19.storyapp2.helper.factory.ViewModelFactory
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.view.login.LoginActivity
import com.alz19.storyapp2.view.welcome.WelcomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniComponent()
        setContentView(activityRegisterBinding.root)
        setupAction()
        setupView()
        playAnimation()


    }

    private fun playAnimation() {
        val binding = activityRegisterBinding

        ObjectAnimator.ofFloat(binding.imageRegisterIllustration, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.textRegisterTitle, View.ALPHA, 1f).setDuration(300)
        val titledesc = ObjectAnimator.ofFloat(binding.textRegisterSubtitle, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.textRegisterNameLabel, View.ALPHA, 1f).setDuration(300)
        val inputName = ObjectAnimator.ofFloat(binding.editRegisterNameLayout, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.textRegisterEmailLabel, View.ALPHA, 1f).setDuration(300)
        val inputEmail = ObjectAnimator.ofFloat(binding.editRegisterEmailLayout, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.textRegisterPasswordLabel, View.ALPHA, 1f).setDuration(300)
        val inputPassword = ObjectAnimator.ofFloat(binding.editRegisterPasswordLayout, View.ALPHA, 1f).setDuration(300)
        val passCheck = ObjectAnimator.ofFloat(binding.textRegisterPasswordConfirmLabel, View.ALPHA, 1f).setDuration(300)
        val inputPassCheck = ObjectAnimator.ofFloat(binding.editRegisterPasswordConfirmLayout, View.ALPHA, 1f).setDuration(300)
        val button = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(300)


        AnimatorSet().apply {
            playSequentially(title, titledesc, name, inputName, email, inputEmail, password, inputPassword,passCheck, inputPassCheck, button)
            start()
        }
    }

    private fun setupAction() {

        activityRegisterBinding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        activityRegisterBinding.editRegisterEmail.message.observe(this) {
            activityRegisterBinding.editRegisterEmailLayout.error = it
        }

        activityRegisterBinding.editRegisterPassword.message.observe(this) {
            activityRegisterBinding.editRegisterPasswordLayout.error = it
        }

        activityRegisterBinding.editRegisterPasswordConfirm.message.observe(this) {
            activityRegisterBinding.editRegisterPasswordConfirmLayout.error = it
        }

        registerViewModel.response.observe(this){result ->
            when(result){
                is Result.Failure -> showDialog(resources.getString(R.string.error), result.throwable)
                is Result.Loading -> showLoading(result.state)
                is Result.Success -> showDialog(resources.getString(R.string.success), result.data.message ?: resources.getString(R.string.success_create_user))
            }
        }


        activityRegisterBinding.buttonRegister.setOnClickListener {
            if (activityRegisterBinding.editRegisterEmail.text.isNullOrEmpty()) {
                activityRegisterBinding.editRegisterEmailLayout.error = resources.getString(R.string.empty_text)
            }
            if (activityRegisterBinding.editRegisterPassword.text.isNullOrEmpty()) {
                activityRegisterBinding.editRegisterPasswordLayout.error = resources.getString(R.string.empty_text)
            }
            if (activityRegisterBinding.editRegisterPasswordConfirm.text.isNullOrEmpty()) {
                activityRegisterBinding.editRegisterPasswordConfirmLayout.error =
                    resources.getString(R.string.empty_text)
            }
            if (activityRegisterBinding.editRegisterPassword.text.toString() != activityRegisterBinding.editRegisterPasswordConfirm.text.toString()){
                activityRegisterBinding.editRegisterPasswordConfirmLayout.error = resources.getString(R.string.password_not_matched)
            }

            if (activityRegisterBinding.editRegisterEmailLayout.error != null || activityRegisterBinding.editRegisterPasswordLayout.error != null || activityRegisterBinding.editRegisterPasswordConfirmLayout.error != null) {
                Toast.makeText(this, resources.getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerViewModel.registerUser(
                activityRegisterBinding.editRegisterName.text.toString(),
                activityRegisterBinding.editRegisterEmail.text.toString(),
                activityRegisterBinding.editRegisterPassword.text.toString(),
            )
        }

        activityRegisterBinding.textButtonRegisterLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showDialog(title : String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
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

    private fun showLoading (isLoading: Boolean){
        activityRegisterBinding.registerLayoutComponent.visibility = if (isLoading) View.GONE else View.VISIBLE
        activityRegisterBinding.progressRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun iniComponent() {
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        registerViewModel = obtainViewModel(this)
    }

    private fun obtainViewModel(context: Context): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[RegisterViewModel::class.java]
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