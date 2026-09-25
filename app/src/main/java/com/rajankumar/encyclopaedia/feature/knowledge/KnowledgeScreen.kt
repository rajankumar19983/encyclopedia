package com.rajankumar.encyclopaedia.feature.knowledge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import com.rajankumar.encyclopaedia.feature.accessibility.SpeakableContent
import com.rajankumar.encyclopaedia.feature.accessibility.rememberTextToSpeechController
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun KnowledgeScreen() {
  val context = LocalContext.current
  val dao = EncyclopaediaDatabase.get(context).dao()
  val roots by dao.observeRootNodes().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  val speech = rememberTextToSpeechController()
  val keyStore = remember(context) { LocalAiApiKeyStore(context) }
  val aiFactory = remember(context) { AiGenerationViewModelFactory(context) }
  val aiViewModel: AiGenerationViewModel = viewModel(factory = aiFactory)
  val aiState by aiViewModel.uiState.collectAsStateWithLifecycle()

  var selected by remember { mutableStateOf<KnowledgeNodeEntity?>(null) }
  var sourceFilter by remember { mutableStateOf(KnowledgeSourceFilter.ALL) }
  var addNode by remember { mutableStateOf(false) }
  var addLesson by remember { mutableStateOf(false) }
  var showAiBuilder by remember { mutableStateOf(false) }
  var showApiKeyDialog by remember { mutableStateOf(false) }
  var hasApiKey by remember { mutableStateOf(keyStore.hasApiKey()) }
  var isApprovingAi by remember { mutableStateOf(false) }
  var approvalError by remember { mutableStateOf<String?>(null) }

  val proposal = aiState.proposal
  if (proposal != null) {
    AiContentReviewScreen(
      proposal = proposal,
      destinationLabel = aiState.destinationLabel,
      isSaving = isApprovingAi,
      isRegenerating = aiState.isGenerating,
      saveError = approvalError,
      generationError = aiState.errorMessage,
      onProposalChange = { updatedProposal ->
        approvalError = null
        aiViewModel.updateProposal(updatedProposal)
      },
      onApprove = {
        if (!isApprovingAi && !aiState.isGenerating) {
          isApprovingAi = true
          approvalError = null
          val destinationNodeId = aiState.destinationNodeId
          scope.launch {
            try {
              AiContentApprovalService(dao).approve(proposal, destinationNodeId)
              aiViewModel.clearProposal()
              aiViewModel.setTopic("")
              showAiBuilder = false
            } catch (error: Exception) {
              approvalError = error.message ?: "Could not save the approved AI content."
            } finally {
              isApprovingAi = false
            }
          }
        }
      },
      onRegenerate = {
        approvalError = null
        aiViewModel.regenerate()
      },
      onDiscard = {
        aiViewModel.clearProposal()
        approvalError = null
        showAiBuilder = false
      },
    )
  } else if (selected == null) {
    val visibleRoots = filterKnowledgeNodesBySource(roots, sourceFilter)
    val sourceSummary = summarizeKnowledgeSources(roots.map { it.source })

    Column(
      modifier = Modifier.fillMaxSize().padding(28.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Header(
        title = "Knowledge & Lessons",
        subtitle = "Build subjects, topics, subtopics and permanent lessons.",
        onAdd = { addNode = true },
        onAi = { showAiBuilder = true },
      )
      KnowledgeSourceFilterBar(sourceFilter, sourceSummary) { sourceFilter = it }
      if (visibleRoots.isEmpty()) {
        Text(
          if (roots.isEmpty()) {
            "No subjects yet. Add your first subject or generate a reviewed AI draft."
          } else {
            sourceFilter.emptyMessage("subjects")
          },
        )
      }
      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(visibleRoots, key = { it.id }) { node ->
          NodeCard(node) { selected = node }
        }
      }
    }
  } else {
    val node = selected!!
    val children by dao.observeChildren(node.id).collectAsStateWithLifecycle(emptyList())
    val lessons by dao.observeLessons(node.id).collectAsStateWithLifecycle(emptyList())
    val visibleChildren = filterKnowledgeNodesBySource(children, sourceFilter)
    val visibleLessons = filterLessonsBySource(lessons, sourceFilter)
    val sourceSummary = summarizeKnowledgeSources(
      children.map { it.source } + lessons.map { it.source },
    )

    Column(
      modifier = Modifier.fillMaxSize().padding(28.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
          IconButton(onClick = { speech.stop(); selected = null }) {
            Icon(Icons.Default.ArrowBack, "Back")
          }
          Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(node.name, style = MaterialTheme.typography.headlineMedium)
              KnowledgeSourceBadge(node.source)
            }
            Text(
              "${children.size} subtopics • ${lessons.size} lessons",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(onClick = { showAiBuilder = true }) { Text("AI Builder") }
          Button(onClick = { addNode = true }) { Text("Add Subtopic") }
          Button(onClick = { addLesson = true }) { Text("Add Lesson") }
        }
      }

      KnowledgeSourceFilterBar(sourceFilter, sourceSummary) { sourceFilter = it }

      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (visibleChildren.isNotEmpty()) {
          item { Text("Subtopics", style = MaterialTheme.typography.titleLarge) }
        }
        items(visibleChildren, key = { it.id }) { child ->
          NodeCard(child) { selected = child }
        }
        if (visibleLessons.isNotEmpty()) {
          item {
            Text(
              "Lessons",
              style = MaterialTheme.typography.titleLarge,
              modifier = Modifier.padding(top = 10.dp),
            )
          }
        }
        items(visibleLessons, key = { it.id }) { lesson ->
          Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                  Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                  KnowledgeSourceBadge(lesson.source)
                }
                Row {
                  IconButton(
                    onClick = {
                      speech.speak(SpeakableContent(title = lesson.title, body = lesson.content))
                    },
                  ) {
                    Icon(Icons.Default.VolumeUp, "Read lesson aloud")
                  }
                  IconButton(onClick = speech::stop) {
                    Icon(Icons.Default.Stop, "Stop reading")
                  }
                }
              }
              Text(lesson.content, maxLines = 5, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
        if ((children.isNotEmpty() || lessons.isNotEmpty()) && visibleChildren.isEmpty() && visibleLessons.isEmpty()) {
          item { Text(sourceFilter.emptyMessage("subtopics or lessons")) }
        }
      }
    }
  }

  if (addNode && proposal == null) {
    NodeDialog(
      title = if (selected == null) "Add subject" else "Add subtopic",
      dismiss = { addNode = false },
      save = { name: String, desc: String ->
        scope.launch {
          dao.upsertNode(
            KnowledgeNodeEntity(
              id = UUID.randomUUID().toString(),
              parentId = selected?.id,
              name = name.trim(),
              description = desc.trim().ifBlank { null },
            ),
          )
        }
        addNode = false
      },
    )
  }

  if (addLesson && selected != null && proposal == null) {
    LessonDialog(
      dismiss = { addLesson = false },
      save = { title: String, content: String ->
        scope.launch {
          dao.upsertLesson(
            LessonEntity(
              id = UUID.randomUUID().toString(),
              knowledgeNodeId = selected!!.id,
              title = title.trim(),
              content = content.trim(),
            ),
          )
        }
        addLesson = false
      },
    )
  }

  if (showAiBuilder && proposal == null) {
    AiContentBuilderDialog(
      state = aiState,
      hasApiKey = hasApiKey,
      destinationLabel = selected?.name,
      onTopicChange = aiViewModel::setTopic,
      onDepthChange = aiViewModel::setDepth,
      onIncludeLessonsChange = aiViewModel::setIncludeLessons,
      onGenerate = {
        approvalError = null
        aiViewModel.generate(selected?.id, selected?.name)
      },
      onManageApiKey = { showApiKeyDialog = true },
      onDismiss = {
        showAiBuilder = false
        aiViewModel.clearError()
      },
    )
  }

  if (showApiKeyDialog && proposal == null) {
    AiApiKeyDialog(
      hasSavedKey = hasApiKey,
      onSave = { apiKey ->
        keyStore.saveApiKey(apiKey)
        hasApiKey = keyStore.hasApiKey()
        showApiKeyDialog = false
        aiViewModel.clearError()
      },
      onClear = {
        keyStore.clearApiKey()
        hasApiKey = false
        showApiKeyDialog = false
      },
      onDismiss = { showApiKeyDialog = false },
    )
  }
}

@Composable
private fun KnowledgeSourceFilterBar(
  selectedFilter: KnowledgeSourceFilter,
  summary: KnowledgeSourceSummary,
  onFilterChange: (KnowledgeSourceFilter) -> Unit,
) {
  Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    KnowledgeSourceFilter.entries.forEach { filter ->
      val label = "${filter.label} (${summary.countFor(filter)})"
      if (filter == selectedFilter) {
        Button(onClick = { onFilterChange(filter) }) { Text(label) }
      } else {
        TextButton(onClick = { onFilterChange(filter) }) { Text(label) }
      }
    }
  }
}

@Composable
private fun KnowledgeSourceBadge(source: String) {
  knowledgeSourceBadge(source)?.let { label ->
    Text(
      label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.primary,
    )
  }
}

@Composable
private fun Header(
  title: String,
  subtitle: String,
  onAdd: () -> Unit,
  onAi: () -> Unit,
) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Column {
      Text(title, style = MaterialTheme.typography.headlineMedium)
      Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(onClick = onAi) { Text("AI Builder") }
      Button(onClick = onAdd) {
        Icon(Icons.Default.Add, null)
        Text(" Add Subject")
      }
    }
  }
}

@Composable
private fun NodeCard(node: KnowledgeNodeEntity, onClick: () -> Unit) {
  Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(node.name, style = MaterialTheme.typography.titleMedium)
        KnowledgeSourceBadge(node.source)
      }
      node.description?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
  }
}

@Composable
private fun NodeDialog(title: String, dismiss: () -> Unit, save: (String, String) -> Unit) {
  var name by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = dismiss,
    title = { Text(title) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
      }
    },
    confirmButton = {
      TextButton(onClick = { save(name, desc) }, enabled = name.isNotBlank()) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = dismiss) { Text("Cancel") } },
  )
}

@Composable
private fun LessonDialog(dismiss: () -> Unit, save: (String, String) -> Unit) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = dismiss,
    title = { Text("Add permanent lesson") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Lesson title") })
        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Lesson content") },
          minLines = 6,
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = { save(title, content) },
        enabled = title.isNotBlank() && content.isNotBlank(),
      ) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = dismiss) { Text("Cancel") } },
  )
}
