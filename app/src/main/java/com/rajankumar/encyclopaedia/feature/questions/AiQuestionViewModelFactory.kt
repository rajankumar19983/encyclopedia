package com.rajankumar.encyclopaedia.feature.questions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.feature.knowledge.LocalAiApiKeyStore
import com.rajankumar.encyclopaedia.feature.knowledge.OpenAiProvider

class AiQuestionViewModelFactory(context: Context) : ViewModelProvider.Factory {
  private val applicationContext = context.applicationContext

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    require(modelClass.isAssignableFrom(AiQuestionViewModel::class.java)) {
      "Unsupported ViewModel: ${modelClass.name}"
    }
    val keyStore = LocalAiApiKeyStore(applicationContext)
    val dao = EncyclopaediaDatabase.get(applicationContext).dao()
    return AiQuestionViewModel(
      generator = AiQuestionGenerator(OpenAiProvider(keyStore)),
      referenceSource = RoomAiQuestionReferenceSource(dao),
      avoidanceSource = RoomAiQuestionAvoidanceSource(dao),
    ) as T
  }
}
