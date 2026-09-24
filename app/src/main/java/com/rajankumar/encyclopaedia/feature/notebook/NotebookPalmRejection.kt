package com.rajankumar.encyclopaedia.feature.notebook

fun palmRejectionLabel(enabled: Boolean): String = if (enabled) "Palm rejection on" else "Palm rejection off"
fun palmRejectionGuidance(enabled: Boolean): String = if (enabled) "Stylus input writes; touch remains available for multi-finger navigation." else "Touch and stylus can both write on the page."
