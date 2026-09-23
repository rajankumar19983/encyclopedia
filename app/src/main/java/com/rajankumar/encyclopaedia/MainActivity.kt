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
import com.rajankumar.encyclopaedia.feature.backup.AutomaticBackupScheduler
import com.rajankumar.encyclopaedia.feature.teacher.OpenAiApiKeyStore
import com.rajankumar.encyclopaedia.feature.teacher.OpenAiHttpApi
import com.rajankumar.encyclopaedia.feature.teacher.OpenAiSettings
import com.rajankumar.encyclopaedia.feature.teacher.OpenAiTeacherResponder
import com.rajankumar.encyclopaedia.feature.teacher.TeacherResponderRegistry
import com.rajankumar.encyclopaedia.navigation.EncyclopaediaShell
import com.rajankumar.encyclopaedia.ui.theme.EncyclopaediaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val settings = OpenAiSettings(applicationContext)
    TeacherResponderRegistry.responder = OpenAiTeacherResponder(
      keyStore = OpenAiApiKeyStore(applicationContext),
      api = OpenAiHttpApi(),
      config = settings::read
    )
    AutomaticBackupScheduler.schedule(applicationContext)
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
