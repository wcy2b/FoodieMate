package com.example.foodiemate.data.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SiliconFlowService {
    @POST("chat/completions")
    suspend fun getChatCompletions(
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): ChatResponse
}


