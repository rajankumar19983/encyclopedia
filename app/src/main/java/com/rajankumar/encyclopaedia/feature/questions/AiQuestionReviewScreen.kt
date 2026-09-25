package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiQuestionReviewScreen(
  proposal: AiQuestionProposal,
  destinationLabel: String?,
  duplicateConflicts: List<AiQuestionDuplicateConflict>,
  isSaving: Boolean,
  isRegenerating: Boolean,
  saveError: String?,
  generationError: String?,
  onProposalChange: (AiQuestionProposal) -> Unit,
  onApprove: () -> Unit,
  onRegenerate: () -> Unit,
  onDiscard: () -> Unit,
) {
  val validation = remember(proposal) { AiQuestionValidator.validate(proposal) }
  val duplicateIndices = remember(duplicateConflicts) { duplicateConflicts.map { it.proposalIndex }.toSet() }
  var editingIndex by remember { mutableStateOf<Int?>(null) }
  var addingQuestion by remember { mutableStateOf(false) }
  var confirmDiscard by remember { mutableStateOf(false) }
  val isBusy = isSaving || isRegenerating

  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("Review AI Questions", style = MaterialTheme.typography.headlineMedium)
        Text(
          "${proposal.questions.size} questions • ${destinationLabel ?: "No topic link"}",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          "Generated items stay AI; questions you add here save as USER. Nothing is saved until approval.",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text("Neither source is marked as PYQ by this flow.", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(
          onClick = { addingQuestion = true },
          enabled = proposal.questions.size < AI_QUESTION_PROPOSAL_MAX_SIZE && !isBusy,
        ) { Text("Add MCQ") }
        TextButton(onClick = { confirmDiscard = true }, enabled = !isBusy) { Text("Discard") }
        TextButton(onClick = onRegenerate, enabled = !isBusy) { Text(if (isRegenerating) "Regenerating…" else "Regenerate") }
        Button(
          onClick = onApprove,
          enabled = validation.isValid && duplicateConflicts.isEmpty() && !isBusy,
        ) { Text(if (isSaving) "Saving…" else "Approve & save") }
      }
    }

    if (proposal.questions.size >= AI_QUESTION_PROPOSAL_MAX_SIZE) {
      Text(
        "Draft limit reached: $AI_QUESTION_PROPOSAL_MAX_SIZE questions.",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
      )
    }

    if (!validation.isValid) {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("Fix these issues before approval", color = MaterialTheme.colorScheme.error)
          validation.errors.take(8).forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
        }
      }
    }

    if (duplicateConflicts.isNotEmpty()) {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            "${duplicateConflicts.size} draft question${if (duplicateConflicts.size == 1) "" else "s"} already exist in your Question Bank.",
            color = MaterialTheme.colorScheme.error,
          )
          Text("Edit or remove the highlighted questions before approval.", style = MaterialTheme.typography.bodySmall)
          duplicateConflicts.take(6).forEach { conflict ->
            Text(
              "• #${conflict.proposalIndex + 1} matches: ${conflict.existingQuestionText}",
              style = MaterialTheme.typography.bodySmall,
            )
          }
        }
      }
    }

    generationError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    saveError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      itemsIndexed(proposal.questions, key = { index, draft -> "$index-${draft.questionText}-${draft.origin}" }) { index, draft ->
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("${index + 1}. ${draft.questionText}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
              Row {
                TextButton(onClick = { editingIndex = index }, enabled = !isBusy) { Text("Edit") }
                TextButton(
                  onClick = { onProposalChange(removeAiQuestion(proposal, index)) },
                  enabled = proposal.questions.size > 1 && !isBusy,
                ) { Text("Remove") }
              }
            }
            if (index in duplicateIndices) {
              Text("Already in Question Bank", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
            }
            draft.options.forEachIndexed { optionIndex, option ->
              val marker = if (optionIndex == draft.correctIndex) "✓" else " "
              Text("$marker ${optionLetter(optionIndex)}. $option")
            }
            val originLabel = when (draft.origin) {
              AiQuestionDraftOrigin.AI -> "AI generated"
              AiQuestionDraftOrigin.USER -> "Added manually"
            }
            Text("${draft.difficulty} • $originLabel", style = MaterialTheme.typography.labelMedium)
            Text("Explanation: ${draft.explanation}", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }

  editingIndex?.let { index ->
    if (index in proposal.questions.indices) {
      AiQuestionEditDialog(
        draft = proposal.questions[index],
        title = "Edit MCQ",
        confirmLabel = "Apply",
        onDismiss = { editingIndex = null },
        onSave = { updated ->
          onProposalChange(updateAiQuestion(proposal, index) { updated })
          editingIndex = null
        },
      )
    } else {
      editingIndex = null
    }
  }

  if (addingQuestion) {
    val manualDraft = remember { newManualAiQuestionDraft() }
    AiQuestionEditDialog(
      draft = manualDraft,
      title = "Add MCQ to draft",
      confirmLabel = "Add question",
      onDismiss = { addingQuestion = false },
      onSave = { added ->
        onProposalChange(appendAiQuestion(proposal, added.copy(origin = AiQuestionDraftOrigin.USER)))
        addingQuestion = false
      },
    )
  }

  if (confirmDiscard) {
    AlertDialog(
      onDismissRequest = { confirmDiscard = false },
      title = { Text("Discard question draft?") },
      text = { Text("All edits and manually added questions in this draft will be lost. Nothing has been saved yet.") },
      confirmButton = { TextButton(onClick = onDiscard) { Text("Discard") } },
      dismissButton = { TextButton(onClick = { confirmDiscard = false }) { Text("Keep reviewing") } },
    )
  }
}

@Composable
private fun AiQuestionEditDialog(
  draft: AiQuestionDraft,
  title: String,
  confirmLabel: String,
  onDismiss: () -> Unit,
  onSave: (AiQuestionDraft) -> Unit,
) {
  var questionText by remember(draft) { mutableStateOf(draft.questionText) }
  val options = remember(draft) { mutableStateListOf<String>().apply { addAll(draft.options) } }
  var correctIndex by remember(draft) { mutableStateOf(draft.correctIndex) }
  var explanation by remember(draft) { mutableStateOf(draft.explanation) }
  var difficulty by remember(draft) { mutableStateOf(draft.difficulty) }
  val valid = questionText.isNotBlank() && options.size in 4..6 && options.all { it.isNotBlank() } &&
    options.map { it.trim().lowercase() }.distinct().size == options.size && correctIndex in options.indices &&
    explanation.isNotBlank() && difficulty in setOf("EASY", "MEDIUM", "HARD")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(title) },
    text = {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { OutlinedTextField(questionText, { questionText = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth()) }
        items(options.size) { index ->
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TextButton(onClick = { correctIndex = index }) { Text(if (correctIndex == index) "✓ ${optionLetter(index)}" else optionLetter(index)) }
            OutlinedTextField(options[index], { options[index] = it }, label = { Text("Option ${index + 1}") }, modifier = Modifier.weight(1f))
            if (options.size > 4) {
              TextButton(onClick = {
                options.removeAt(index)
                correctIndex = when {
                  options.isEmpty() -> 0
                  index < correctIndex -> correctIndex - 1
                  correctIndex > options.lastIndex -> options.lastIndex
                  else -> correctIndex
                }
              }) { Text("−") }
            }
          }
        }
        if (options.size < 6) item { TextButton(onClick = { options.add("") }) { Text("+ Add option") } }
        item { OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation") }, modifier = Modifier.fillMaxWidth(), minLines = 3) }
        item {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("EASY", "MEDIUM", "HARD").forEach { value ->
              TextButton(onClick = { difficulty = value }) { Text(if (difficulty == value) "✓ $value" else value) }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onSave(
            draft.copy(
              questionText = questionText.trim(),
              options = options.map(String::trim),
              correctIndex = correctIndex,
              explanation = explanation.trim(),
              difficulty = difficulty,
            ),
          )
        },
        enabled = valid,
      ) { Text(confirmLabel) }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
}
