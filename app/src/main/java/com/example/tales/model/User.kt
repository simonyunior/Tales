package com.example.tales.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String,
    val name: String,
    val email: String,
    val token: String
) : Parcelable
