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
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import com.rajankumar.encyclopaedia.feature.accessibility.SpeakableContent
import com.rajankumar.encyclopaedia.feature.accessibility.rememberTextToSpeechController
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun KnowledgeScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val roots by dao.observeRootNodes().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  val speech = rememberTextToSpeechController()
  var selected by remember { mutableStateOf<KnowledgeNodeEntity?>(null) }
  var addNode by remember { mutableStateOf(false) }
  var addLesson by remember { mutableStateOf(false) }

  if (selected == null) {
    Column(
      modifier = Modifier.fillMaxSize().padding(28.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Header("Knowledge & Lessons", "Build subjects, topics, subtopics and permanent lessons.") {
        addNode = true
      }
      if (roots.isEmpty()) Text("No subjects yet. Add your first subject to begin.")
      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(roots, key = { it.id }) { node ->
          NodeCard(node) { selected = node }
        }
      }
    }
  } else {
    val node = selected!!
    val children by dao.observeChildren(node.id).collectAsStateWithLifecycle(emptyList())
    val lessons by dao.observeLessons(node.id).collectAsStateWithLifecycle(emptyList())

    Column(
      modifier = Modifier.fillMaxSize().padding(28.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
          IconButton(onClick = { speech.stop(); selected = null }) {
            Icon(Icons.Default.ArrowBack, "Back")
          }
          Column {
            Text(node.name, style = MaterialTheme.typography.headlineMedium)
            Text(
              "${children.size} subtopics • ${lessons.size} lessons",
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(onClick = { addNode = true }) { Text("Add Subtopic") }
          Button(onClick = { addLesson = true }) { Text("Add Lesson") }
        }
      }

      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (children.isNotEmpty()) {
          item { Text("Subtopics", style = MaterialTheme.typography.titleLarge) }
        }
        items(children, key = { it.id }) { child ->
          NodeCard(child) { selected = child }
        }
        if (lessons.isNotEmpty()) {
          item {
            Text(
              "Lessons",
              style = MaterialTheme.typography.titleLarge,
              modifier = Modifier.padding(top = 10.dp)
            )
          }
        }
        items(lessons, key = { it.id }) { lesson ->
          Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(lesson.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Row {
                  IconButton(
                    onClick = {
                      speech.speak(SpeakableContent(title = lesson.title, body = lesson.content))
                    }
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
      }
    }
  }

  if (addNode) {
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
              description = desc.trim().ifBlank { null }
            )
          )
        }
        addNode = false
      }
    )
  }

  if (addLesson && selected != null) {
    LessonDialog(
      dismiss = { addLesson = false },
      save = { title: String, content: String ->
        scope.launch {
          dao.upsertLesson(
            LessonEntity(
              id = UUID.randomUUID().toString(),
              knowledgeNodeId = selected!!.id,
              title = title.trim(),
              content = content.trim()
            )
          )
        }
        addLesson = false
      }
    )
  }
}

@Composable
private fun Header(title: String, subtitle: String, onAdd: () -> Unit) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Column {
      Text(title, style = MaterialTheme.typography.headlineMedium)
      Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Button(onClick = onAdd) {
      Icon(Icons.Default.Add, null)
      Text(" Add Subject")
    }
  }
}

@Composable
private fun NodeCard(node: KnowledgeNodeEntity, onClick: () -> Unit) {
  Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
    Column(Modifier.padding(18.dp)) {
      Text(node.name, style = MaterialTheme.typography.titleMedium)
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
    dismissButton = { TextButton(onClick = dismiss) { Text("Cancel") } }
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
          minLines = 6
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = { save(title, content) },
        enabled = title.isNotBlank() && content.isNotBlank()
      ) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = dismiss) { Text("Cancel") } }
  )
}
