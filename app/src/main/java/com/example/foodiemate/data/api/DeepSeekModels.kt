package com.example.foodiemate.data.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val stream: Boolean = false
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<ChatChoice>
)

data class ChatChoice(
    val message: ChatMessage,
    @SerializedName("finish_reason")
    val finishReason: String
)


