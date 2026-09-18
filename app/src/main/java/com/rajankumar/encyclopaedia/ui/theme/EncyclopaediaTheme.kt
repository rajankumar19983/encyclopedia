package com.rajankumar.encyclopaedia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EncyclopaediaColors = lightColorScheme(
  primary = Color(0xFF6650A4),
  onPrimary = Color.White,
  primaryContainer = Color(0xFFEADDFF),
  onPrimaryContainer = Color(0xFF21005D),
  secondary = Color(0xFF625B71),
  background = Color(0xFFF8F7FC),
  surface = Color(0xFFFFFBFE),
  surfaceVariant = Color(0xFFE7E0EC),
  onSurfaceVariant = Color(0xFF49454F)
)

@Composable
fun EncyclopaediaTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = EncyclopaediaColors,
    content = content
  )
}
