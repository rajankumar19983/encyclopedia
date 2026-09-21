package com.rajankumar.encyclopaedia.feature.teacher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TeacherLauncher(onClick: () -> Unit) {
  ExtendedFloatingActionButton(
    onClick = onClick,
    icon = { Icon(Icons.Default.School, contentDescription = null) },
    text = { Text("AI Teacher") }
  )
}
