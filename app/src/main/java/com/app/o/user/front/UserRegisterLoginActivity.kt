package com.app.o.user.front

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.api.login.account.LoginResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.home.HomeActivity
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

class UserRegisterLoginActivity : OAppActivity(), View.OnClickListener, OAppViewService<LoginResponse> {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var presenter: UserRegisterLoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_login)
        supportActionBar?.hide()

        initGoogleSignIn()
        initView()

        presenter = UserRegisterLoginPresenter(this, mCompositeDisposable)
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

    override fun showLoading() {
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)
    }

    override fun onDataResponse(data: LoginResponse) {
        if (isSuccess(data.status)) {
            saveUserState(data)

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            showSnackBar(root_register_login, getString(R.string.text_error_after_login))
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
            presenter.doLoginWithThirdParty(
                    account?.email,
                    account?.displayName,
                    "password",
                    "google")
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 99
    }

}