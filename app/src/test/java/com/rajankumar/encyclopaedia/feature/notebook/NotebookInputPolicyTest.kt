package com.rajankumar.encyclopaedia.feature.notebook

import android.view.MotionEvent
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookInputPolicyTest {
  @Test
  fun palmRejectionAllowsStylusAndHardwareEraser() {
    assertTrue(
      NotebookInputPolicy.canStartStroke(
        palmRejection = true,
        toolType = MotionEvent.TOOL_TYPE_STYLUS,
      )
    )
    assertTrue(
      NotebookInputPolicy.canStartStroke(
        palmRejection = true,
        toolType = MotionEvent.TOOL_TYPE_ERASER,
      )
    )
  }

  @Test
  fun palmRejectionBlocksFingerAndUnknownStrokeInput() {
    assertFalse(
      NotebookInputPolicy.canStartStroke(
        palmRejection = true,
        toolType = MotionEvent.TOOL_TYPE_FINGER,
      )
    )
    assertFalse(
      NotebookInputPolicy.canStartStroke(
        palmRejection = true,
        toolType = MotionEvent.TOOL_TYPE_UNKNOWN,
      )
    )
  }

  @Test
  fun disabledPalmRejectionAllowsFingerDrawing() {
    assertTrue(
      NotebookInputPolicy.canStartStroke(
        palmRejection = false,
        toolType = MotionEvent.TOOL_TYPE_FINGER,
      )
    )
  }

  @Test
  fun twoFingerGestureIsAllowedWithPalmRejection() {
    assertTrue(
      NotebookInputPolicy.shouldStartViewportGesture(
        palmRejection = true,
        pointerToolTypes = listOf(
          MotionEvent.TOOL_TYPE_FINGER,
          MotionEvent.TOOL_TYPE_FINGER,
        ),
      )
    )
  }

  @Test
  fun fingerTouchDoesNotCancelActiveStylusStroke() {
    assertFalse(
      NotebookInputPolicy.shouldStartViewportGesture(
        palmRejection = true,
        pointerToolTypes = listOf(
          MotionEvent.TOOL_TYPE_STYLUS,
          MotionEvent.TOOL_TYPE_FINGER,
        ),
      )
    )
  }

  @Test
  fun hardwareEraserAndPalmDoNotBecomeViewportGesture() {
    assertFalse(
      NotebookInputPolicy.shouldStartViewportGesture(
        palmRejection = true,
        pointerToolTypes = listOf(
          MotionEvent.TOOL_TYPE_ERASER,
          MotionEvent.TOOL_TYPE_FINGER,
        ),
      )
    )
  }

  @Test
  fun multiTouchGestureStillWorksWhenPalmRejectionIsDisabled() {
    assertTrue(
      NotebookInputPolicy.shouldStartViewportGesture(
        palmRejection = false,
        pointerToolTypes = listOf(
          MotionEvent.TOOL_TYPE_STYLUS,
          MotionEvent.TOOL_TYPE_FINGER,
        ),
      )
    )
  }
}
