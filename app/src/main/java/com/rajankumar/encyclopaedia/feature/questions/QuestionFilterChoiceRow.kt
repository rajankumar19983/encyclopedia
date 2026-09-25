package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
internal fun QuestionFilterChoiceRow(
  title: String,
  choices: List<Pair<String?, String>>,
  selected: String?,
  onSelect: (String?) -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(title, style = MaterialTheme.typography.titleSmall)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(choices, key = { choice -> "$title:${choice.first ?: "ALL"}" }) { (value, label) ->
        FilterChip(
          selected = selected == value,
          onClick = { onSelect(value) },
          label = { Text(label) },
        )
      }
    }
  }
}
