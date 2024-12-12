package com.example.capstone.result

import java.io.File

data class ProcessImageRequest(
    val image: File
)

data class ProcessImageResponse(
    val function: String,
    val name: String,
    val predicted_rating: String
)

data class SkincareResponse(
    val predicted_skincare_categories: List<String>,
    val ingredients: List<ProcessImageResponse>
)