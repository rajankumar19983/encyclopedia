package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity

@Composable
fun AiQuestionBuilderScreen(
  state: AiQuestionUiState,
  topics: List<KnowledgeNodeEntity>,
  targetTopicId: String?,
  hasApiKey: Boolean,
  onBack: () -> Unit,
  onTopicChange: (String) -> Unit,
  onCountChange: (Int) -> Unit,
  onDifficultyChange: (AiQuestionDifficulty) -> Unit,
  onTargetTopicChange: (String?) -> Unit,
  onManageApiKey: () -> Unit,
  onGenerate: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("AI Question Builder", style = MaterialTheme.typography.headlineMedium)
        Text("Generate a draft, review every MCQ, then explicitly approve it.", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      TextButton(onClick = onBack, enabled = !state.isGenerating) { Text("Back") }
    }

    OutlinedTextField(
      value = state.topic,
      onValueChange = onTopicChange,
      label = { Text("What should the questions cover?") },
      supportingText = { Text("Example: CPU instruction cycle, addressing modes and registers") },
      modifier = Modifier.fillMaxWidth(),
      minLines = 2,
    )

    Text("Question count", style = MaterialTheme.typography.titleMedium)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      listOf(5, 10, 15, 20).forEach { count ->
        TextButton(onClick = { onCountChange(count) }) { Text(if (state.count == count) "✓ $count" else count.toString()) }
      }
    }

    Text("Difficulty", style = MaterialTheme.typography.titleMedium)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      AiQuestionDifficulty.entries.forEach { difficulty ->
        TextButton(onClick = { onDifficultyChange(difficulty) }) {
          Text(if (state.difficulty == difficulty) "✓ ${difficulty.name}" else difficulty.name)
        }
      }
    }

    Text("Ground with a Knowledge topic and save approved questions there (optional)", style = MaterialTheme.typography.titleMedium)
    Text(
      "When selected, its current lessons and direct subtopics are used as bounded reference material for generation and regeneration.",
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      style = MaterialTheme.typography.bodySmall,
    )
    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      item {
        TextButton(onClick = { onTargetTopicChange(null) }) {
          Text(if (targetTopicId == null) "✓ No Knowledge grounding" else "No Knowledge grounding")
        }
      }
      items(topics.take(40), key = { it.id }) { topic ->
        TextButton(onClick = { onTargetTopicChange(topic.id) }) {
          Text(if (targetTopicId == topic.id) "✓ ${topic.name}" else topic.name)
        }
      }
    }

    state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    Text(
      if (hasApiKey) "API key is configured on this device." else "An API key is required before generation.",
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      TextButton(onClick = onManageApiKey, enabled = !state.isGenerating) { Text("Manage API key") }
      Button(onClick = onGenerate, enabled = state.canGenerate && hasApiKey) {
        Text(if (state.isGenerating) "Generating…" else "Generate draft")
      }
    }
  }
}
