package com.rajankumar.encyclopaedia.feature.accessibility

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccessibilitySettingsScreen() {
  val speech = rememberTextToSpeechController()
  var rate by remember { mutableStateOf(SpeechRate.NORMAL) }

  Column(
    modifier = Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    Text("Settings", style = MaterialTheme.typography.headlineMedium)
    Text("Accessibility", style = MaterialTheme.typography.titleLarge)
    Text(
      "Choose how quickly lessons, questions and explanations are read aloud.",
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text("Reading speed", style = MaterialTheme.typography.titleMedium)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      SpeechRate.entries.forEach { option ->
        FilterChip(
          selected = rate == option,
          onClick = {
            rate = option
            speech.setSpeechRate(option)
          },
          label = { Text(option.label) }
        )
      }
    }
    Button(
      onClick = {
        speech.setSpeechRate(rate)
        speech.speak("This is a preview of the selected reading speed.")
      }
    ) {
      Text("Preview voice")
    }
  }
}
