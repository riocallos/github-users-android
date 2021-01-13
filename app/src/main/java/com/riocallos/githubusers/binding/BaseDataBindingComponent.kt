package com.riocallos.githubusers.binding

/**
 * Base class that extends DataBindingComponent to handle
 * data binding initialization.
 *
 */
class BaseDataBindingComponent : androidx.databinding.DataBindingComponent {

    override fun getImageViewDataBinding(): ImageViewDataBinding {
        return ImageViewDataBinding()
    }

}