package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity

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
  val index = layers.indexOfFirst { it.id == layerId }
  val targetIndex = index + delta
  if (index < 0 || targetIndex !in layers.indices) return null
  val current = layers[index]
  val target = layers[targetIndex]
  return LayerOrderSwap(
    firstId = current.id,
    firstOrder = current.sortOrder,
    secondId = target.id,
    secondOrder = target.sortOrder
  )
}
