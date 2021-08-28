package com.example.nush_hack21.model

data class User(
    val uid: String,
    val history: List<Record>,
    val displayName: String
)
