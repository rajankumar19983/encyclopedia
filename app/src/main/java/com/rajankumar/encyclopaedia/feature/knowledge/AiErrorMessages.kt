package com.rajankumar.encyclopaedia.feature.knowledge

object AiErrorMessages {
  fun forError(error: AiProviderError): String = when (error) {
    AiProviderError.MissingApiKey -> "Add an API key before generating content."
    AiProviderError.Unauthorized -> "The API key was rejected. Check it and try again."
    AiProviderError.RateLimited -> "The AI service is busy. Try again shortly."
    is AiProviderError.Http -> error.message ?: "The AI request failed with HTTP ${error.statusCode}."
    is AiProviderError.Network -> error.message ?: "Could not reach the AI service. Check your connection."
    is AiProviderError.InvalidResponse -> error.message ?: "The AI service returned an unreadable response."
  }
}
