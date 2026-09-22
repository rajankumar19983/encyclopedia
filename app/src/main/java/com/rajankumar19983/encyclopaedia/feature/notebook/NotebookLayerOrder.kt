package com.rajankumar19983.encyclopaedia.feature.notebook

import com.rajankumar19983.encyclopaedia.data.local.NotebookLayerEntity

internal data class LayerOrderSwap(
  val firstId: String,
  val firstOrder: Int,
  val secondId: String,
  val secondOrder: Int
)

internal fun layerOrderSwap(
  layers: List<NotebookLayerEntity>,
  layerId: String,
  delta: Int
): LayerOrderSwap? {
  if (delta != -1 && delta != 1) return null
  val ordered = layers.sortedWith(
    compareBy<NotebookLayerEntity> { it.sortOrder }
      .thenBy { it.createdAt }
      .thenBy { it.id }
  )
  val index = ordered.indexOfFirst { it.id == layerId }
  if (index < 0) return null
  val targetIndex = index + delta
  if (targetIndex !in ordered.indices) return null
  val current = ordered[index]
  val target = ordered[targetIndex]
  return LayerOrderSwap(
    firstId = current.id,
    firstOrder = current.sortOrder,
    secondId = target.id,
    secondOrder = target.sortOrder
  )
}
