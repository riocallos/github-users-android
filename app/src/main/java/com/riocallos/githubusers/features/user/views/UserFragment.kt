package com.riocallos.githubusers.features.user.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.riocallos.githubusers.R
import com.riocallos.githubusers.base.BaseViewModelFragment
import com.riocallos.githubusers.databinding.FragmentUserBinding
import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.features.main.MainActivity
import com.riocallos.githubusers.features.user.UserState
import com.riocallos.githubusers.features.user.UserViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.subscribeBy

open class UserFragment : BaseViewModelFragment<FragmentUserBinding, UserViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_user

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupVmObservers()
        setupVm()
    }

    private fun setupViews() {
        (activity as MainActivity).showLoadingDialog()

        binding.user = User()
        binding.note = Note()

        binding.back.visibility = View.GONE

        binding.blogContainer.setOnClickListener {
            if(!viewModel.user.blog.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(viewModel.user.blog)
                startActivity(intent)
            }
        }

        binding.save.setOnClickListener {
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
                (activity as MainActivity).showLoadingDialog()
            }
            UserState.HideProgressLoading -> {
                (activity as MainActivity).dismissDialogs()
            }
            is UserState.ShowError -> {
                (activity as MainActivity).dismissDialogs()
                (activity as MainActivity).showAlertDialog("", state.error)
            }
            is UserState.ShowMessage -> {
                (activity as MainActivity).dismissDialogs()
                Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()
            }
            is UserState.ShowUser -> {
                binding.user = state.user
            }
        }
    }

    private fun setupVm() {
        viewModel.remoteUser(arguments?.getString("user")!!)
    }

}