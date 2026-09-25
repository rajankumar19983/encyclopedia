package com.rajankumar.encyclopaedia.feature.knowledge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiContentReviewScreen(
  proposal: AiContentProposal,
  destinationLabel: String?,
  isSaving: Boolean,
  isRegenerating: Boolean,
  saveError: String?,
  generationError: String?,
  onProposalChange: (AiContentProposal) -> Unit,
  onApprove: () -> Unit,
  onRegenerate: () -> Unit,
  onDiscard: () -> Unit,
) {
  val validation = AiContentValidator.validate(proposal)
  val stats = proposal.root.reviewStats()
  val collapsibleKeys = proposal.root.collapsibleNodeKeys()
  var collapsedKeys by remember(proposal.generatedAt) { mutableStateOf(emptySet<String>()) }
  var confirmDiscard by remember { mutableStateOf(false) }
  var confirmRegenerate by remember { mutableStateOf(false) }
  var addNodePath by remember(proposal.generatedAt) { mutableStateOf<List<Int>?>(null) }
  var addLessonPath by remember(proposal.generatedAt) { mutableStateOf<List<Int>?>(null) }
  val rows = proposal.root.reviewRows(collapsedKeys)
  val isBusy = isSaving || isRegenerating

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
        Text(
          "${stats.nodeCount} nodes • ${stats.lessonCount} lessons • ${stats.levelCount} levels",
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(
          onClick = { collapsedKeys = collapsibleKeys },
          enabled = !isBusy && collapsibleKeys.isNotEmpty(),
        ) { Text("Collapse all") }
        TextButton(
          onClick = { collapsedKeys = emptySet() },
          enabled = !isBusy && collapsedKeys.isNotEmpty(),
        ) { Text("Expand all") }
        TextButton(
          onClick = { confirmRegenerate = true },
          enabled = !isBusy,
        ) { Text(if (isRegenerating) "Regenerating…" else "Regenerate") }
        TextButton(onClick = { confirmDiscard = true }, enabled = !isBusy) { Text("Discard") }
        Button(
          onClick = onApprove,
          enabled = validation.isValid && !isBusy,
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
    generationError?.let {
      Text("Regeneration failed: $it", color = MaterialTheme.colorScheme.error)
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
            enabled = !isBusy,
            collapsed = row.collapseKey in collapsedKeys,
            canCollapse = row.collapseKey in collapsibleKeys,
            onToggleCollapsed = {
              collapsedKeys = if (row.collapseKey in collapsedKeys) {
                collapsedKeys - row.collapseKey
              } else {
                collapsedKeys + row.collapseKey
              }
            },
            onAddNode = { addNodePath = row.path },
            onAddLesson = { addLessonPath = row.path },
            onProposalChange = onProposalChange,
          )
          is AiReviewRow.Lesson -> LessonReviewCard(
            row = row,
            proposal = proposal,
            enabled = !isBusy,
            onProposalChange = onProposalChange,
          )
        }
      }
    }
  }

  addNodePath?.let { parentPath ->
    AddDraftNodeDialog(
      onDismiss = { addNodePath = null },
      onAdd = { title, description ->
        val root = AiContentDraftEditor.addNodeAtPath(
          proposal.root,
          parentPath,
          AiKnowledgeDraft(
            title = title.trim(),
            description = description.trim().ifBlank { null },
          ),
        )
        collapsedKeys = collapsedKeys - collapseKey(parentPath)
        addNodePath = null
        onProposalChange(proposal.copy(root = root))
      },
    )
  }

  addLessonPath?.let { nodePath ->
    AddDraftLessonDialog(
      onDismiss = { addLessonPath = null },
      onAdd = { title, content ->
        val root = AiContentDraftEditor.addLessonAtPath(
          proposal.root,
          nodePath,
          AiLessonDraft(
            title = title.trim(),
            content = content.trim(),
          ),
        )
        collapsedKeys = collapsedKeys - collapseKey(nodePath)
        addLessonPath = null
        onProposalChange(proposal.copy(root = root))
      },
    )
  }

  if (confirmRegenerate) {
    AlertDialog(
      onDismissRequest = { confirmRegenerate = false },
      title = { Text("Generate a replacement draft?") },
      text = {
        Text(
          "A successful regeneration will replace this draft and its edits. If generation fails, the current draft will remain available.",
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            confirmRegenerate = false
            onRegenerate()
          },
        ) { Text("Regenerate draft") }
      },
      dismissButton = {
        TextButton(onClick = { confirmRegenerate = false }) { Text("Keep current draft") }
      },
    )
  }

  if (confirmDiscard) {
    AlertDialog(
      onDismissRequest = { confirmDiscard = false },
      title = { Text("Discard AI draft?") },
      text = {
        Text("All edits to this draft will be lost. Nothing from this draft has been saved to your knowledge base yet.")
      },
      confirmButton = {
        TextButton(
          onClick = {
            confirmDiscard = false
            onDiscard()
          },
        ) { Text("Discard draft") }
      },
      dismissButton = {
        TextButton(onClick = { confirmDiscard = false }) { Text("Keep reviewing") }
      },
    )
  }
}

@Composable
private fun NodeReviewCard(
  row: AiReviewRow.Node,
  proposal: AiContentProposal,
  enabled: Boolean,
  collapsed: Boolean,
  canCollapse: Boolean,
  onToggleCollapsed: () -> Unit,
  onAddNode: () -> Unit,
  onAddLesson: () -> Unit,
  onProposalChange: (AiContentProposal) -> Unit,
) {
  Card(
    Modifier
      .fillMaxWidth()
      .padding(start = (row.depth * 14).dp),
  ) {
    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
          if (row.path.isEmpty()) "Root knowledge node" else "Knowledge node • level ${row.depth + 1}",
          style = MaterialTheme.typography.labelLarge,
        )
        if (canCollapse) {
          TextButton(onClick = onToggleCollapsed, enabled = enabled) {
            Text(if (collapsed) "Expand contents" else "Collapse contents")
          }
        }
      }
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
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(onClick = onAddNode, enabled = enabled) { Text("Add subtopic") }
        TextButton(onClick = onAddLesson, enabled = enabled) { Text("Add lesson") }
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

@Composable
private fun AddDraftNodeDialog(
  onDismiss: () -> Unit,
  onAdd: (String, String) -> Unit,
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add subtopic to draft") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Title") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description (optional)") },
          modifier = Modifier.fillMaxWidth(),
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = { onAdd(title, description) },
        enabled = title.isNotBlank(),
      ) { Text("Add subtopic") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
}

@Composable
private fun AddDraftLessonDialog(
  onDismiss: () -> Unit,
  onAdd: (String, String) -> Unit,
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add lesson to draft") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Lesson title") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Lesson content") },
          minLines = 4,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = { onAdd(title, content) },
        enabled = title.isNotBlank() && content.isNotBlank(),
      ) { Text("Add lesson") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
}

private fun collapseKey(path: List<Int>): String = "node:${path.joinToString(".")}"
