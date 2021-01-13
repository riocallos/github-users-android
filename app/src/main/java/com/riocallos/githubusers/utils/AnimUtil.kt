package com.riocallos.githubusers.utils

import com.riocallos.githubusers.R

object AnimUtil {

    /**
     * 1st anim for going from left to right
     *
     * @return
     */
    fun inF(): Int {
        return R.anim.slide_in_left
    }

    /**
     * 2nd anim for going from left to right
     *
     * @return
     */
    fun inS(): Int {
        return R.anim.slide_out_left
    }

    /**
     * 1st anim for going from left to right
     *
     * @return
     */
    fun outF(): Int {
        return android.R.anim.slide_out_right
    }

    /**
     * 2nd anim for going from left to right
     *
     * @return
     */
    fun outS(): Int {
        return R.anim.slide_in_right
    }

}