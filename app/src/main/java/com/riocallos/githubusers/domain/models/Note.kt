package com.riocallos.githubusers.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Model for note.
 *
 */
@Entity(tableName = "notes")
data class Note(@SerializedName("id")
                  @PrimaryKey(autoGenerate = true) var id: Int = 0) {

    @SerializedName("username")
    var username: String = ""

    @SerializedName("text")
    var text: String? = ""

}