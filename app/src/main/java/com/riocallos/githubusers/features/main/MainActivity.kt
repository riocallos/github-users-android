package com.riocallos.githubusers.features.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.riocallos.githubusers.R
import com.riocallos.githubusers.base.BaseViewModelActivity
import com.riocallos.githubusers.base.OnItemClickListener
import com.riocallos.githubusers.databinding.ActivityMainBinding
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.features.user.views.UserActivity
import com.riocallos.githubusers.features.user.views.UserFragment
import com.riocallos.githubusers.receivers.NetworkReceiver
import com.riocallos.githubusers.utils.AnimUtil
import com.riocallos.githubusers.utils.PaginationScrollListener
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.subscribeBy

class MainActivity : BaseViewModelActivity<ActivityMainBinding, MainViewModel>(), OnItemClickListener<User> {

    override fun getLayoutId(): Int = R.layout.activity_main

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private val mainAdapter by lazy {
        MainAdapter(this)
    }

    private lateinit var receiver: NetworkReceiver

    private var initialLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupVmObservers()
        setupVm()

    }

    override fun onResume() {
        super.onResume()
        mainAdapter.notifyDataSetChanged()
    }

    public override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(receiver)
    }

    private fun setupViews() {

        binding.swipeRefreshLayout.isRefreshing = true

        twoPane = binding.frameLayout != null

        binding.searchButton.setOnClickListener(View.OnClickListener {
            viewModel.clearSearch()
            binding.retrieved.visibility = View.GONE
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.toolbar)
            constraintSet.connect(R.id.searchCard, ConstraintSet.START, R.id.toolbar, ConstraintSet.START, 0)
            val transition: Transition = ChangeBounds()
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {
                    binding.title.visibility = View.GONE
                    binding.searchButton.visibility = View.GONE
                }

                override fun onTransitionEnd(transition: Transition) {
                    binding.searchText.setText("")
                    binding.searchText.requestLayout()
                    binding.searchText.requestFocus()
                }

                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionResume(transition: Transition) {}
            })
            transition.duration = 300
            TransitionManager.beginDelayedTransition(binding.toolbar, transition)
            constraintSet.applyTo(binding.toolbar)

        })

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val query = editable.toString()
                if(query.isNotEmpty()) {
                    viewModel.search(query)
                }
            }
        })

        binding.searchClose.setOnClickListener(View.OnClickListener {
            viewModel.clearSearch()
            binding.retrieved.visibility = View.VISIBLE
            binding.searchEmpty.visibility = View.GONE
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.toolbar)
            constraintSet.connect(R.id.searchCard, ConstraintSet.START, R.id.toolbar, ConstraintSet.END, 0)
            val transition: Transition = ChangeBounds()
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {}
                override fun onTransitionEnd(transition: Transition) {
                    binding.title.visibility = View.VISIBLE
                    binding.searchButton.visibility = View.VISIBLE
                }

                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionResume(transition: Transition) {}
            })
            transition.duration = 300
            TransitionManager.beginDelayedTransition(binding.toolbar, transition)
            constraintSet.applyTo(binding.toolbar)
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.shimmer.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            viewModel.remoteUsers()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView
            .apply {
                this.layoutManager = layoutManager
                post {
                    this.adapter = mainAdapter
                    mainAdapter.itemClickListener = this@MainActivity
                }
            }

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {

            override fun loadMoreItems() {
                if(binding.title.visibility == View.VISIBLE) {
                    binding.progressEnd.visibility = View.VISIBLE
                    viewModel.remoteUsers()
                }
            }

            override fun isLoading(): Boolean {
                return binding.progressEnd.isVisible
            }

        })

        binding.progressEnd.indeterminateDrawable?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(this, R.color.green), BlendModeCompat.SRC_ATOP)

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

    private fun handleState(state: MainState) {
        if (lifecycle.currentState != Lifecycle.State.RESUMED) return
        when (state) {
            MainState.ShowProgressLoading -> {
                binding.swipeRefreshLayout.isRefreshing = true
            }
            MainState.HideProgressLoading -> {
                binding.swipeRefreshLayout.isRefreshing = false
                dismissDialogs()
            }
            is MainState.ShowRetrieved -> {
                binding.retrieved.text = state.retrieved
            }
            is MainState.ShowUsers -> {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressEnd.visibility = View.GONE
                binding.shimmer.visibility = View.GONE

                if(binding.title.visibility == View.VISIBLE) {
                    binding.searchButton.visibility = View.VISIBLE
                }

                if(binding.title.visibility == View.GONE && state.users.isEmpty()) {
                    binding.searchEmpty.visibility = View.VISIBLE
                } else {
                    binding.searchEmpty.visibility = View.GONE
                }

                binding.recyclerView.visibility = View.VISIBLE

                mainAdapter.submitList(state.users)
                mainAdapter.notifyDataSetChanged()

                if(initialLaunch) {
                    initialLaunch = false
                    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    connMgr.allNetworks.forEach { network ->
                        connMgr.getNetworkInfo(network).apply {
                            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                            receiver = NetworkReceiver(object : NetworkReceiver.OnNetworkChanged {
                                override fun connected() {
                                    viewModel.remoteUsers()
                                    Toast.makeText(this@MainActivity, "Network connected.", Toast.LENGTH_SHORT).show()
                                }

                                override fun disconnected() {
                                    Toast.makeText(this@MainActivity, "Network disconnected.", Toast.LENGTH_SHORT).show()
                                }

                            })
                            this@MainActivity.registerReceiver(receiver, filter)
                        }
                    }
                }
            }
            is MainState.ShowUser -> {
                showUser(state.user)
            }
        }
    }

    private fun setupVm() {
        viewModel.getRetrieved()
        viewModel.localUsers()
        viewModel.remoteUsers()
    }

    fun hasNote(username: String) = viewModel.hasNote(username)

    override fun onItemClick(v: View, item: User, position: Int) {
        showUser(item)
    }

    private fun showUser(user: User) {
        if (twoPane) {
            val fragment = UserFragment()
            val arguments = Bundle()
            arguments.putString("user", user.username)
            fragment.arguments = arguments
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
        } else {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("user", user.username)
            startActivity(intent)
            overridePendingTransition(AnimUtil.inF(), AnimUtil.inS())
        }
    }



}