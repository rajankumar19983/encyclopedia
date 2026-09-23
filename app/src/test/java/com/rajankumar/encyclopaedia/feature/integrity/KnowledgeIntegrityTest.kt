package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import org.junit.Assert.*
import org.junit.Test

class KnowledgeIntegrityTest {
  @Test fun nodeRequiresName() = assertFalse(KnowledgeNodeEntity("n", null, "").hasValidKnowledgeFields())
  @Test fun lessonRequiresContent() = assertFalse(LessonEntity("l", "n", "Title", "").hasValidLessonFields())
  @Test fun validLessonPasses() = assertTrue(LessonEntity("l", "n", "Title", "Body").hasValidLessonFields())
}
