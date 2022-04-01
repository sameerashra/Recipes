package com.example.recipes.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Long,
    val title: String,
    val summary: String,
    val instructions: String,
    val image: String
): Parcelable
