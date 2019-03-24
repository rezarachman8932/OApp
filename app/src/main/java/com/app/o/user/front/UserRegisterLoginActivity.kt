package com.app.o.user.front

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.base.page.OAppActivity
import com.app.o.user.login.LoginActivity
import com.app.o.user.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_user_register_login.*

class UserRegisterLoginActivity : OAppActivity(), View.OnClickListener {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_login)
        supportActionBar?.hide()

        initGoogleSignIn()
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.button_sign_up -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            R.id.layout_login_text -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.button_google_sign_in -> {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    private fun initGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun initView() {
        button_google_sign_in.setOnClickListener(this)
        button_google_sign_in.setSize(SignInButton.SIZE_STANDARD)

        button_sign_up.setOnClickListener(this)
        layout_login_text.setOnClickListener(this)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // TODO Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            e.printStackTrace()
        }

    }

    companion object {
        private const val RC_SIGN_IN = 99
    }

}