package com.music.app.activity

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.music.app.R
import com.music.app.databinding.ActivityAdminChangePasswordBinding
import com.music.app.prefs.DataStoreManager
import com.music.app.prefs.DataStoreManager.Companion.user
import com.music.app.utils.StringUtil.isEmpty

class AdminChangePasswordActivity : BaseActivity() {

    private var binding: ActivityAdminChangePasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initListener()
    }

    private fun initListener() {
        binding?.imgBack?.setOnClickListener { onBackPressed() }
        binding?.btnChangePassword?.setOnClickListener { onClickValidateChangePassword() }
    }

    private fun onClickValidateChangePassword() {
        val strOldPassword = binding?.edtOldPassword?.text.toString().trim { it <= ' ' }
        val strNewPassword = binding?.edtNewPassword?.text.toString().trim { it <= ' ' }
        val strConfirmPassword = binding?.edtConfirmPassword?.text.toString().trim { it <= ' ' }
        if (isEmpty(strOldPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show()
        } else if (isEmpty(strNewPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show()
        } else if (isEmpty(strConfirmPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show()
        } else if (user?.password != strOldPassword) {
            Toast.makeText(this,
                    getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show()
        } else if (strNewPassword != strConfirmPassword) {
            Toast.makeText(this,
                    getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show()
        } else if (strOldPassword == strNewPassword) {
            Toast.makeText(this,
                    getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show()
        } else {
            changePassword(strNewPassword)
        }
    }

    private fun changePassword(newPassword: String) {
        showProgressDialog(true)
        val user = FirebaseAuth.getInstance().currentUser ?: return
        user.updatePassword(newPassword)
                .addOnCompleteListener { task: Task<Void?> ->
                    showProgressDialog(false)
                    if (task.isSuccessful) {
                        Toast.makeText(this,
                                getString(R.string.msg_change_password_successfully),
                                Toast.LENGTH_SHORT).show()
                        val userLogin = DataStoreManager.user
                        userLogin!!.password = newPassword
                        DataStoreManager.user = userLogin
                        binding?.edtOldPassword?.setText("")
                        binding?.edtNewPassword?.setText("")
                        binding?.edtConfirmPassword?.setText("")
                    }
                }
    }
}