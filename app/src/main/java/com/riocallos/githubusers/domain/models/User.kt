package com.riocallos.githubusers.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Model for user.
 *
 */
@Entity(tableName = "users")
data class User(@SerializedName("id")
                  @PrimaryKey var id: Int = 0) {

    @SerializedName("avatar_url")
    var avatarUrl: String = ""

    @SerializedName("login")
    var username: String = ""

    @SerializedName("followers")
    var followers: String? = "0"

    @SerializedName("following")
    var following: String? = "0"

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("company")
    var company: String? = ""

    @SerializedName("blog")
    var blog: String? = ""

}