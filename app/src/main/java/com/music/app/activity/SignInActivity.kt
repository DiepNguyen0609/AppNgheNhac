package com.music.app.activity

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.music.app.R
import com.music.app.constant.Constant
import com.music.app.constant.GlobalFunction.startActivity
import com.music.app.databinding.ActivitySignInBinding
import com.music.app.model.User
import com.music.app.prefs.DataStoreManager
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.utils.StringUtil.isEmpty
import com.music.app.utils.StringUtil.isValidEmail

class SignInActivity : BaseActivity() {

    private var binding: ActivitySignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initListener()
    }

    private fun initListener() {
        binding?.layoutSignUp?.setOnClickListener {
            startActivity(this@SignInActivity, SignUpActivity::class.java)
        }
        binding?.btnSignIn?.setOnClickListener { onClickValidateSignIn() }
        binding?.tvForgotPassword?.setOnClickListener { onClickForgotPassword() }
    }

    private fun onClickForgotPassword() {
        startActivity(this, ForgotPasswordActivity::class.java)
    }

    private fun onClickValidateSignIn() {
        val strEmail = binding?.edtEmail?.text.toString().trim()
        val strPassword = binding?.edtPassword?.text.toString().trim()

        when {
            strEmail.isEmpty() -> {
                Toast.makeText(this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show()
            }
            strPassword.isEmpty() -> {
                Toast.makeText(this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show()
            }
            !isValidEmail(strEmail) -> {
                Toast.makeText(this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show()
            }
            else -> {
                signInUser(strEmail, strPassword)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        showProgressDialog(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showProgressDialog(false)
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        val userObject = User(it.email, password)
                        userObject.isAdmin = it.email?.contains(Constant.ADMIN_EMAIL_FORMAT) == true
                        DataStoreManager.user = userObject
                        goToMainActivity(userObject.isAdmin)
                    }
                } else {
                    Toast.makeText(this, getString(R.string.msg_sign_in_error), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity(isAdmin: Boolean) {
        val targetActivity = if (isAdmin) AdminMainActivity::class.java else MainActivity::class.java
        startActivity(this@SignInActivity, targetActivity)
        finishAffinity()
    }
}