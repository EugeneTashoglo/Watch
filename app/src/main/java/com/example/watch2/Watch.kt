package com.example.watch2

data class Watch(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val imageFileName: String = "",
    val imageFrameName: String = "",
    val price: Int = 0,
    val rating: Double = 0.0,
    val new: Boolean = false,
    val popular: Boolean = false
)
