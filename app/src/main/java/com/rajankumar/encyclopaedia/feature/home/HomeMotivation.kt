package com.rajankumar.encyclopaedia.feature.home

fun homeMotivation(status: HomeStudyStatus): String = when (status) {
  HomeStudyStatus.EMPTY -> "Add your first study material and make the dashboard yours."
  HomeStudyStatus.STARTING -> "A short baseline session is enough to start revealing your weak areas."
  HomeStudyStatus.NEEDS_REVIEW -> "Correcting mistakes now will make the next practice round more useful."
  HomeStudyStatus.BUILDING_COVERAGE -> "Your accuracy is taking shape; keep expanding into unseen questions."
  HomeStudyStatus.ESTABLISHED -> "Your study history is ready for mixed recall and targeted revision."
}
