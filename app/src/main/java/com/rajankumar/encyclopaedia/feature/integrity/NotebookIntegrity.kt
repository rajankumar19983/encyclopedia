package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.*

fun NotebookPageEntity.hasValidNotebookPageFields(): Boolean = id.isNotBlank() && title.isNotBlank() && pageWidth > 0f && pageHeight > 0f && sortOrder >= 0
fun NotebookLayerEntity.hasValidNotebookLayerFields(): Boolean = id.isNotBlank() && pageId.isNotBlank() && name.isNotBlank() && sortOrder >= 0
fun NotebookStrokeEntity.hasValidNotebookStrokeFields(): Boolean = id.isNotBlank() && layerId.isNotBlank() && pointsJson.isNotBlank() && tool.isNotBlank() && width > 0f
