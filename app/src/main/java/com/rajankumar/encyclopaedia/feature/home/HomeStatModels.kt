package com.rajankumar.encyclopaedia.feature.home

data class HomeStatModel(
  val title: String,
  val value: String,
  val detail: String,
  val progress: Float
)

fun buildHomeStatModels(topicCount: Int, questionCount: Int, attemptCount: Int, correctCount: Int, practisedCount: Int): List<HomeStatModel> {
  val metrics = calculateHomeMetrics(questionCount, attemptCount, correctCount, practisedCount)
  return listOf(
    HomeStatModel("Topics", topicCount.toString(), if (topicCount == 0) "Build your knowledge tree" else "Stored in your knowledge tree", if (topicCount > 0) 1f else 0f),
    HomeStatModel("Questions", questionCount.toString(), "$practisedCount practised • ${metrics.coveragePercent}% coverage", percentProgress(metrics.coveragePercent)),
    HomeStatModel("Practice", attemptCount.toString(), if (attemptCount == 0) "No attempts recorded yet" else "$correctCount correct answers", practiceProgress(attemptCount)),
    HomeStatModel("Accuracy", "${metrics.accuracyPercent}%", if (attemptCount == 0) "Practice to build your baseline" else "$correctCount of $attemptCount correct", percentProgress(metrics.accuracyPercent))
  )
}
