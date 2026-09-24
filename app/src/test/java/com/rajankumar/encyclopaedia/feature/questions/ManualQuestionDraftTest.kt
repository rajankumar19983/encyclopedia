package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.*
import org.junit.Test

class ManualQuestionDraftTest {
  @Test fun acceptsLetterAnswer() = assertTrue(ManualQuestionDraft("Q?", "One\nTwo", "B").isValid())
  @Test fun acceptsNumericAnswer() = assertTrue(ManualQuestionDraft("Q?", "One\nTwo", "2").isValid())
  @Test fun rejectsAnswerOutsideOptions() = assertFalse(ManualQuestionDraft("Q?", "One\nTwo", "C").isValid())
  @Test fun rejectsTooManyOptions() = assertFalse(ManualQuestionDraft("Q?", "1\n2\n3\n4\n5\n6\n7", "A").isValid())
  @Test fun trimsBlankOptionLines() = assertEquals(listOf("One", "Two"), ManualQuestionDraft("Q?", " One \n\n Two ", "A").options)
}
