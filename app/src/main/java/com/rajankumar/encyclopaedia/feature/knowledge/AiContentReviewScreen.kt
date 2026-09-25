package com.rajankumar.encyclopaedia.feature.knowledge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiContentReviewScreen(
  proposal: AiContentProposal,
  destinationLabel: String?,
  isSaving: Boolean,
  saveError: String?,
  onProposalChange: (AiContentProposal) -> Unit,
  onApprove: () -> Unit,
  onDiscard: () -> Unit,
) {
  val validation = AiContentValidator.validate(proposal)
  val rows = flattenReviewRows(proposal.root)

  Column(
    modifier = Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column(Modifier.weight(1f)) {
        Text("Review AI draft", style = MaterialTheme.typography.headlineMedium)
        Text(
          if (destinationLabel == null) {
            "Review and edit this hierarchy before adding it to your knowledge base."
          } else {
            "Review and edit this hierarchy before adding it inside $destinationLabel."
          },
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(onClick = onDiscard, enabled = !isSaving) { Text("Discard") }
        Button(
          onClick = onApprove,
          enabled = validation.isValid && !isSaving,
        ) { Text(if (isSaving) "Saving…" else "Approve & save") }
      }
    }

    if (!validation.isValid) {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("Fix these items before saving", style = MaterialTheme.typography.titleMedium)
          validation.errors.forEach { error -> Text("• $error") }
        }
      }
    }
    saveError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      items(rows, key = { it.key }) { row ->
        when (row) {
          is AiReviewRow.Node -> NodeReviewCard(
            row = row,
            proposal = proposal,
            enabled = !isSaving,
            onProposalChange = onProposalChange,
          )
          is AiReviewRow.Lesson -> LessonReviewCard(
            row = row,
            proposal = proposal,
            enabled = !isSaving,
            onProposalChange = onProposalChange,
          )
        }
      }
    }
  }
}

@Composable
private fun NodeReviewCard(
  row: AiReviewRow.Node,
  proposal: AiContentProposal,
  enabled: Boolean,
  onProposalChange: (AiContentProposal) -> Unit,
) {
  Card(
    Modifier
      .fillMaxWidth()
      .padding(start = (row.depth * 14).dp),
  ) {
    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(
        if (row.path.isEmpty()) "Root knowledge node" else "Knowledge node • level ${row.depth + 1}",
        style = MaterialTheme.typography.labelLarge,
      )
      OutlinedTextField(
        value = row.node.title,
        onValueChange = { title ->
          val root = AiContentDraftEditor.updateNodeAtPath(proposal.root, row.path) {
            it.copy(title = title)
          }
          onProposalChange(proposal.copy(root = root))
        },
        label = { Text("Title") },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = row.node.description.orEmpty(),
        onValueChange = { description ->
          val root = AiContentDraftEditor.updateNodeAtPath(proposal.root, row.path) {
            it.copy(description = description.ifBlank { null })
          }
          onProposalChange(proposal.copy(root = root))
        },
        label = { Text("Description") },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
      )
      if (row.path.isNotEmpty()) {
        TextButton(
          onClick = {
            onProposalChange(
              proposal.copy(root = AiContentDraftEditor.removeNodeAtPath(proposal.root, row.path)),
            )
          },
          enabled = enabled,
        ) { Text("Remove node") }
      }
    }
  }
}

@Composable
private fun LessonReviewCard(
  row: AiReviewRow.Lesson,
  proposal: AiContentProposal,
  enabled: Boolean,
  onProposalChange: (AiContentProposal) -> Unit,
) {
  Card(
    Modifier
      .fillMaxWidth()
      .padding(start = ((row.depth + 1) * 14).dp),
  ) {
    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text("Lesson", style = MaterialTheme.typography.labelLarge)
      OutlinedTextField(
        value = row.lesson.title,
        onValueChange = { title ->
          val root = AiContentDraftEditor.updateLessonAtPath(
            proposal.root,
            row.nodePath,
            row.lessonIndex,
          ) { it.copy(title = title) }
          onProposalChange(proposal.copy(root = root))
        },
        label = { Text("Lesson title") },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = row.lesson.content,
        onValueChange = { content ->
          val root = AiContentDraftEditor.updateLessonAtPath(
            proposal.root,
            row.nodePath,
            row.lessonIndex,
          ) { it.copy(content = content) }
          onProposalChange(proposal.copy(root = root))
        },
        label = { Text("Lesson content") },
        enabled = enabled,
        minLines = 3,
        modifier = Modifier.fillMaxWidth(),
      )
      TextButton(
        onClick = {
          onProposalChange(
            proposal.copy(
              root = AiContentDraftEditor.removeLessonAtPath(
                proposal.root,
                row.nodePath,
                row.lessonIndex,
              ),
            ),
          )
        },
        enabled = enabled,
      ) { Text("Remove lesson") }
    }
  }
}

private sealed interface AiReviewRow {
  val key: String

  data class Node(
    val path: List<Int>,
    val depth: Int,
    val node: AiKnowledgeDraft,
  ) : AiReviewRow {
    override val key: String = "node:${path.joinToString(".")}"
  }

  data class Lesson(
    val nodePath: List<Int>,
    val lessonIndex: Int,
    val depth: Int,
    val lesson: AiLessonDraft,
  ) : AiReviewRow {
    override val key: String = "lesson:${nodePath.joinToString(".")}:$lessonIndex"
  }
}

private fun flattenReviewRows(root: AiKnowledgeDraft): List<AiReviewRow> = buildList {
  fun visit(node: AiKnowledgeDraft, path: List<Int>, depth: Int) {
    add(AiReviewRow.Node(path, depth, node))
    node.lessons.forEachIndexed { index, lesson ->
      add(AiReviewRow.Lesson(path, index, depth, lesson))
    }
    node.children.forEachIndexed { index, child ->
      visit(child, path + index, depth + 1)
    }
  }
  visit(root, emptyList(), 0)
}
