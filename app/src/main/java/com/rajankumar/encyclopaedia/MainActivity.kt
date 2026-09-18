package com.rajankumar.encyclopaedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      EncyclopaediaApp()
    }
  }
}

@Composable
private fun EncyclopaediaApp() {
  MaterialTheme(colorScheme = lightColorScheme()) {
    Surface(modifier = Modifier.fillMaxSize()) {
      Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "Encyclopaedia",
          style = MaterialTheme.typography.headlineLarge
        )
        Text(
          text = "Your knowledge and exam preparation system.",
          style = MaterialTheme.typography.titleMedium
        )
        Text(
          text = "Foundation build is running successfully. The full tablet dashboard comes next.",
          style = MaterialTheme.typography.bodyLarge
        )
      }
    }
  }
}
