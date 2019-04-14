package com.app.o.user.front

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.api.login.account.LoginResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.home.HomeActivity
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.login.LoginActivity
import com.app.o.user.register.RegisterActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_user_register_login.*
import java.util.*

class UserRegisterLoginActivity : OAppActivity(), View.OnClickListener, OAppViewService<LoginResponse> {

    private lateinit var presenter: UserRegisterLoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_login)
        supportActionBar?.hide()

        initSignInGoogle()
        initFacebookSignIn()
        initView()

        presenter = UserRegisterLoginPresenter(this, mCompositeDisposable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInGoogleResult(task)
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
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
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleAPIClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    override fun showLoading() {
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            if (!OAppUserUtil.getThirdPartyLoginType().isNullOrEmpty()) {
                setLoginTypeFromThirdParty(null)
            }
        }
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

    private fun initFacebookSignIn() {
        mCallbackManager = CallbackManager.Factory.create()

        button_facebook_login.setReadPermissions(Arrays.asList("public_profile", "email"))
        button_facebook_login.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleSignInFacebookResult(loginResult.accessToken)
            }

            override fun onCancel() {}

            override fun onError(exception: FacebookException) {}
        })
    }

    private fun initView() {
        button_google_sign_in.setOnClickListener(this)
        button_google_sign_in.setSize(SignInButton.SIZE_STANDARD)

        button_sign_up.setOnClickListener(this)
        layout_login_text.setOnClickListener(this)
    }

    private fun handleSignInGoogleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            setLoginTypeFromThirdParty(LOGIN_TYPE_GOOGLE)

            val account = completedTask.getResult(ApiException::class.java)
            presenter.doLoginWithThirdParty(
                    account?.email,
                    account?.displayName,
                    account?.id,
                    LOGIN_TYPE_GOOGLE)
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun handleSignInFacebookResult(accessToken: AccessToken) {
        try {
            setLoginTypeFromThirdParty(LOGIN_TYPE_FACEBOOK)

            val request = GraphRequest.newMeRequest(accessToken) { `object`, _ ->
                val userId = `object`.getString("id")
                val userName = `object`.getString("name")
                val userEmail = `object`.getString("email")

                OAppUserUtil.setFacebookUserId(userId)
                OAppUserUtil.setFacebookUserName(userName)

                presenter.doLoginWithThirdParty(
                        userEmail,
                        userName,
                        userId,
                        LOGIN_TYPE_FACEBOOK)
            }

            val parameters = Bundle()
            parameters.putString("fields", "id,name,email")
            request.parameters = parameters
            request.executeAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 99
    }

}