package com.music.app.activity

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.music.app.R
import com.music.app.databinding.ActivityForgotPasswordBinding
import com.music.app.utils.StringUtil.isEmpty
import com.music.app.utils.StringUtil.isValidEmail

class ForgotPasswordActivity : BaseActivity() {

    private var binding: ActivityForgotPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initListener()
    }

    private fun initListener() {
        binding?.imgBack?.setOnClickListener { onBackPressed() }
        binding?.btnResetPassword?.setOnClickListener { onClickValidateResetPassword() }
    }

    private fun onClickValidateResetPassword() {
        val strEmail = binding?.edtEmail?.text.toString().trim { it <= ' ' }
        if (isEmpty(strEmail)) {
            Toast.makeText(this@ForgotPasswordActivity,
                    getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show()
        } else if (!isValidEmail(strEmail)) {
            Toast.makeText(this@ForgotPasswordActivity,
                    getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show()
        } else {
            resetPassword(strEmail)
        }
    }

    private fun resetPassword(email: String) {
        showProgressDialog(true)
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task: Task<Void?> ->
                    showProgressDialog(false)
                    if (task.isSuccessful) {
                        Toast.makeText(this@ForgotPasswordActivity,
                                getString(R.string.msg_reset_password_successfully),
                                Toast.LENGTH_SHORT).show()
                        binding?.edtEmail?.setText("")
                    }
                }
    }
}