package com.rajankumar.encyclopaedia.feature.knowledge

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AiGenerationViewModelFactory(
  context: Context,
) : ViewModelProvider.Factory {
  private val applicationContext = context.applicationContext

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    require(modelClass.isAssignableFrom(AiGenerationViewModel::class.java)) {
      "Unsupported ViewModel: ${modelClass.name}"
    }
    val keyStore = LocalAiApiKeyStore(applicationContext)
    val provider = OpenAiProvider(keyStore)
    return AiGenerationViewModel(AiContentGenerator(provider)) as T
  }
}
