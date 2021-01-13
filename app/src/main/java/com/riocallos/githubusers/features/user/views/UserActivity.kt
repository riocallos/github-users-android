package com.riocallos.githubusers.features.user.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.riocallos.githubusers.R
import com.riocallos.githubusers.base.BaseViewModelActivity
import com.riocallos.githubusers.databinding.ActivityUserBinding
import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.features.user.UserState
import com.riocallos.githubusers.features.user.UserViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.subscribeBy
import java.util.*

class UserActivity : BaseViewModelActivity<ActivityUserBinding, UserViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        setupVmObservers()
        setupVm()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onBackPressed() {
        viewModel.removeUser()
        super.onBackPressed()
    }

    private fun setupViews() {

        showLoadingDialog()

        binding.user = User()
        binding.note = Note()

        binding.back.setOnClickListener { onBackPressed() }

        binding.blogContainer.setOnClickListener {
            if(!viewModel.user.blog.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(viewModel.user.blog)
                startActivity(intent)
            }
        }

        binding.save.setOnClickListener {
            (Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE)) as InputMethodManager).hideSoftInputFromWindow(
                binding.noteText.windowToken,
                0
            )
            viewModel.updateNote(binding.noteText.text.toString())
        }

    }

    private fun setupVmObservers() {
        viewModel.state
            .toFlowable(BackpressureStrategy.BUFFER)
            .observeOn(schedulers.ui())
            .subscribeBy(
                onNext = { state ->
                    handleState(state)
                },
                onError = {
                    //AppLogger.error(it.message)
                }
            ).apply {
                disposables.add(this)
            }

    }

    private fun handleState(state: UserState) {
        if (lifecycle.currentState != Lifecycle.State.RESUMED) return
        when (state) {
            UserState.ShowProgressLoading -> {
                showLoadingDialog()
            }
            UserState.HideProgressLoading -> {
                dismissDialogs()
            }
            is UserState.ShowError -> {
                dismissDialogs()
                showAlertDialog("", state.error)
            }
            is UserState.ShowMessage -> {
                dismissDialogs()
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is UserState.ShowUser -> {
                binding.user = state.user
            }
            is UserState.ShowNote -> {
                binding.note = state.note
            }
        }
    }

    private fun setupVm() {
        val bundle = intent.extras
        viewModel.remoteUser(bundle?.getString("user")!!)
    }

}