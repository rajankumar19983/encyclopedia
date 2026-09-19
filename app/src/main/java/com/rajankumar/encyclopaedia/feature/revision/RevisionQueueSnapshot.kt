package com.rajankumar.encyclopaedia.feature.revision

data class RevisionQueueSnapshot(val stats: RevisionStats, val health: RevisionHealth, val recommendation: String)

fun List<RevisionItem>.snapshot() = RevisionQueueSnapshot(revisionStats(), revisionHealth(), revisionRecommendation())
