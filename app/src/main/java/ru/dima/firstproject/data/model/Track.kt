package ru.dima.firstproject.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val name: String,
    val srcAudio: Int,
    val srcImage: Int
) : Parcelable
