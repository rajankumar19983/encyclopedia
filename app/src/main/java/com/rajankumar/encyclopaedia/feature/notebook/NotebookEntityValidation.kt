package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity

internal fun NotebookPageEntity.hasValidNotebookFields(): Boolean =
  id.isNotBlank() && title.isNotBlank() &&
    isValidNotebookPageSize(pageWidth, pageHeight) &&
    isSupportedNotebookBackground(background)

internal fun NotebookLayerEntity.hasValidNotebookFields(): Boolean =
  id.isNotBlank() && pageId.isNotBlank() && normalizedLayerName(name).isNotBlank() &&
    normalizedLayerName(name).length <= NOTEBOOK_LAYER_NAME_MAX_LENGTH

internal fun NotebookStrokeEntity.hasValidNotebookFields(): Boolean =
  id.isNotBlank() && layerId.isNotBlank() && pointsJson.isNotBlank() &&
    isSupportedStrokeTool(tool) && isValidStrokeWidth(width)
