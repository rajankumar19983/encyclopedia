package com.rajankumar.encyclopaedia.feature.notebook

import android.content.Context

internal class NotebookPreferences(context: Context) {
  private val preferences = context.getSharedPreferences("notebook_preferences", Context.MODE_PRIVATE)

  var penWidth: Float
    get() = preferences.getFloat(KEY_PEN_WIDTH, DEFAULT_PEN_WIDTH)
    set(value) {
      preferences.edit().putFloat(KEY_PEN_WIDTH, value).apply()
    }

  var penColorArgb: Long
    get() = preferences.getLong(KEY_PEN_COLOR, DEFAULT_PEN_COLOR)
    set(value) {
      preferences.edit().putLong(KEY_PEN_COLOR, value).apply()
    }

  var palmRejection: Boolean
    get() = preferences.getBoolean(KEY_PALM_REJECTION, true)
    set(value) {
      preferences.edit().putBoolean(KEY_PALM_REJECTION, value).apply()
    }

  var selectedTool: NotebookTool
    get() = runCatching {
      NotebookTool.valueOf(preferences.getString(KEY_SELECTED_TOOL, NotebookTool.PEN.name)!!)
    }.getOrDefault(NotebookTool.PEN)
    set(value) {
      preferences.edit().putString(KEY_SELECTED_TOOL, value.name).apply()
    }

  var preferredBackground: NotebookBackground
    get() = runCatching {
      NotebookBackground.valueOf(
        preferences.getString(KEY_PREFERRED_BACKGROUND, NotebookBackground.PLAIN.name)!!
      )
    }.getOrDefault(NotebookBackground.PLAIN)
    set(value) {
      preferences.edit().putString(KEY_PREFERRED_BACKGROUND, value.name).apply()
    }

  companion object {
    const val DEFAULT_PEN_WIDTH = 4f
    const val DEFAULT_PEN_COLOR = 0xFF111111
    private const val KEY_PEN_WIDTH = "pen_width"
    private const val KEY_PEN_COLOR = "pen_color_argb"
    private const val KEY_PALM_REJECTION = "palm_rejection"
    private const val KEY_SELECTED_TOOL = "selected_tool"
    private const val KEY_PREFERRED_BACKGROUND = "preferred_background"
  }
}
