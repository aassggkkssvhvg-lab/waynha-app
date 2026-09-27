package iq.waynha.app.model

data class Review(
    val id: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    val date: String
)
