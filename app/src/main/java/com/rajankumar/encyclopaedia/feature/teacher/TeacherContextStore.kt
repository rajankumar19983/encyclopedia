package com.rajankumar.encyclopaedia.feature.teacher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object TeacherContextStore {
  var current by mutableStateOf<TeacherContext?>(null)
    private set

  fun update(context: TeacherContext?) {
    current = context
  }

  fun clear() {
    current = null
  }
}

@Composable
fun PublishTeacherContext(context: TeacherContext?) {
  DisposableEffect(context) {
    TeacherContextStore.update(context)
    onDispose {
      if (TeacherContextStore.current == context) {
        TeacherContextStore.clear()
      }
    }
  }
}
