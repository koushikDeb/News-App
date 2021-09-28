package com.example.assignmentnewsapp.networking.model


data class Response(
    val status : String,
    val totalResults : Int,
    val articles : ArrayList<Articles>
)
