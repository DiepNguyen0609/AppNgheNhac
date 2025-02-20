package com.music.app.activity

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.music.app.R
import com.music.app.constant.Constant
import com.music.app.constant.GlobalFunction.startActivity
import com.music.app.databinding.ActivitySignUpBinding
import com.music.app.model.User
import com.music.app.prefs.DataStoreManager
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.utils.StringUtil.isEmpty
import com.music.app.utils.StringUtil.isValidEmail

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initListener()
    }

    private fun initListener() {
        binding?.imgBack?.setOnClickListener { onBackPressed() }
        binding?.layoutSignIn?.setOnClickListener { finish() }
        binding?.btnSignUp?.setOnClickListener { onClickValidateSignUp() }
    }

    private fun onClickValidateSignUp() {
        val strEmail = binding?.edtEmail?.text.toString().trim()
        val strPassword = binding?.edtPassword?.text.toString().trim()

        if (strEmail.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show()
        } else if (strPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show()
        } else if (!isValidEmail(strEmail)) {
            Toast.makeText(this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show()
        } else if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
            Toast.makeText(this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show()
        } else {
            signUpUser(strEmail, strPassword)
        }
    }

    private fun signUpUser(email: String, password: String) {
        showProgressDialog(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showProgressDialog(false)
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val userObject = User(user.email, password)
                        DataStoreManager.user = userObject
                        goToMainActivity()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.msg_sign_up_error), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity() {
        startActivity(this@SignUpActivity, MainActivity::class.java)
        finishAffinity()
    }
}