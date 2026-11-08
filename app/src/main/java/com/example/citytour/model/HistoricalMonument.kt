package com.example.citytour.model

data class HistoricalMonument(
    val id: String,
    val name: String,
    val location: String,
    val summary: String,
    val description: String,
    val imageResIds: List<Int>,
    val audioResId: Int
)
