package com.rajankumar.encyclopaedia.feature.teacher

sealed interface TeacherRequestState {
  data object Idle : TeacherRequestState
  data object Sending : TeacherRequestState
  data class Failed(val connectionState: TeacherConnectionState) : TeacherRequestState
}

fun TeacherRequestState.canSend(): Boolean = this !is TeacherRequestState.Sending
