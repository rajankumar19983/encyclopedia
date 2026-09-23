package com.rajankumar.encyclopaedia.feature.teacher

import com.rajankumar.encyclopaedia.data.local.LessonEntity

object TeacherSelection {
  fun publishLesson(lesson: LessonEntity, topicName: String?) {
    TeacherContextStore.update(lesson.asTeacherContext(topicName))
  }

  fun clear() {
    TeacherContextStore.clear()
  }
}
