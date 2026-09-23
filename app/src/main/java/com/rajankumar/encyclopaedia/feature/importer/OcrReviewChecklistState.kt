package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewChecklistState(val checked: Set<Int> = emptySet()) {
  fun toggle(index: Int): OcrReviewChecklistState = copy(checked = if (index in checked) checked - index else checked + index)

  fun requiredComplete(items: List<OcrReviewChecklistItem>): Boolean = items.indices
    .filter { items[it].required }
    .all { it in checked }
}
