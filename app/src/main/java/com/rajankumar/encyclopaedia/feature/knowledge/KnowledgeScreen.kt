package com.rajankumar.encyclopaedia.feature.knowledge

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun KnowledgeScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val nodes by dao.observeRootNodes().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  var showAdd by remember { mutableStateOf(false) }

  Column(
    Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("Knowledge & Lessons", style = MaterialTheme.typography.headlineMedium)
        Text("Build your editable knowledge tree.", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Button(onClick = { showAdd = true }) {
        Icon(Icons.Default.Add, null)
        Text(" Add Topic")
      }
    }

    if (nodes.isEmpty()) {
      Text("No topics yet. Add your first subject or topic to begin.")
    } else {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(nodes, key = { it.id }) { node ->
          Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
              Text(node.name, style = MaterialTheme.typography.titleMedium)
              node.description?.takeIf { it.isNotBlank() }?.let {
                Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }
      }
    }
  }

  if (showAdd) {
    AddTopicDialog(
      onDismiss = { showAdd = false },
      onSave = { name, description ->
        scope.launch {
          dao.upsertNode(
            KnowledgeNodeEntity(
              id = UUID.randomUUID().toString(),
              parentId = null,
              name = name.trim(),
              description = description.trim().ifBlank { null },
              sortOrder = nodes.size
            )
          )
        }
        showAdd = false
      }
    )
  }
}

@Composable
private fun AddTopicDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
  var name by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add topic") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(name, { name = it }, label = { Text("Name") }, singleLine = true)
        OutlinedTextField(description, { description = it }, label = { Text("Description") })
      }
    },
    confirmButton = {
      TextButton(onClick = { onSave(name, description) }, enabled = name.isNotBlank()) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}
