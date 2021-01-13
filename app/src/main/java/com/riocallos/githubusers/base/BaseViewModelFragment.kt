package com.riocallos.githubusers.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.riocallos.githubusers.di.Injectable
import java.lang.reflect.ParameterizedType

abstract class BaseViewModelFragment<B : ViewDataBinding, VM : BaseViewModel> :
        DaggerFragment<B>(), Injectable {

    lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModelClass = (javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[1] as Class<VM>

        viewModel = if (setActivityAsViewModelProvider()) {
            ViewModelProvider(requireActivity(), viewModelFactory)
                    .get(viewModelClass)
                    .apply {
                        onCreate(arguments)
                    }
        } else {
            ViewModelProvider(this, viewModelFactory)
                    .get(viewModelClass).apply {
                        onCreate(arguments)
                    }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun setActivityAsViewModelProvider(): Boolean {
        return true
    }
}