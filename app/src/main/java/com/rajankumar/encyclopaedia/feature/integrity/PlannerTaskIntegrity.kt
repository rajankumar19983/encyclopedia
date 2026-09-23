package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity

private val isoDate = Regex("\\d{4}-\\d{2}-\\d{2}")

fun PlannerTaskEntity.hasValidPlannerFields(): Boolean =
  id.isNotBlank() && title.isNotBlank() && isoDate.matches(scheduledDate) &&
    (carriedFromDate == null || isoDate.matches(carriedFromDate)) &&
    (isCompleted == (completedAt != null))
