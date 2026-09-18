package com.rajankumar.encyclopaedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.rajankumar.encyclopaedia.navigation.EncyclopaediaShell
import com.rajankumar.encyclopaedia.ui.theme.EncyclopaediaTheme

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
  EncyclopaediaTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      BoxWithConstraints(Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        EncyclopaediaShell(
          navController = navController,
          useNavigationRail = maxWidth >= 700.dp
        )
      }
    }
  }
}
