package com.example.nush_hack21.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    val title: String,
    val imageUrl: String?,
) : Parcelable