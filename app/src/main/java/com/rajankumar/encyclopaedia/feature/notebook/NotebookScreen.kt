package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NotebookScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  val pages by dao.observeNotebookPages().collectAsStateWithLifecycle(emptyList())
  var title by remember { mutableStateOf("") }
  var pageToDelete by remember { mutableStateOf<NotebookPageEntity?>(null) }
  var selectedPage by remember { mutableStateOf<NotebookPageEntity?>(null) }

  pageToDelete?.let { page ->
    AlertDialog(
      onDismissRequest = { pageToDelete = null },
      title = { Text("Delete notebook page?") },
      text = { Text("${page.title} and every layer and handwritten stroke on it will be permanently deleted.") },
      confirmButton = {
        TextButton(onClick = {
          pageToDelete = null
          scope.launch(Dispatchers.IO) { dao.deleteNotebookPage(page.id) }
        }) { Text("Delete") }
      },
      dismissButton = { TextButton(onClick = { pageToDelete = null }) { Text("Cancel") } }
    )
  }

  selectedPage?.let { page ->
    NotebookPagePlaceholder(page = page, onBack = { selectedPage = null })
    return
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text("Notebook", style = MaterialTheme.typography.headlineMedium)
      Text("Handwritten study pages are stored locally and included in your backups.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    item {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("New page", style = MaterialTheme.typography.titleMedium)
          OutlinedTextField(
            value = title,
            onValueChange = { title = it.take(100) },
            label = { Text("Page title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Button(enabled = title.isNotBlank(), onClick = {
            val pageTitle = title.trim()
            title = ""
            scope.launch(Dispatchers.IO) {
              val pageId = UUID.randomUUID().toString()
              dao.upsertNotebookPage(NotebookPageEntity(id = pageId, title = pageTitle))
              dao.upsertNotebookLayer(NotebookLayerEntity(id = UUID.randomUUID().toString(), pageId = pageId, name = "Writing"))
            }
          }) { Text("Create page") }
        }
      }
    }
    if (pages.isEmpty()) {
      item { Text("No notebook pages yet. Create one above to start writing.") }
    } else {
      items(pages, key = { it.id }) { page ->
        Card(Modifier.fillMaxWidth().clickable { selectedPage = page }) {
          Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
              Text(page.title, style = MaterialTheme.typography.titleMedium)
              Text(page.background.lowercase().replaceFirstChar { it.uppercase() } + " page", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            TextButton(onClick = { pageToDelete = page }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
          }
        }
      }
    }
  }
}

@Composable
private fun NotebookPagePlaceholder(page: NotebookPageEntity, onBack: () -> Unit) {
  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    TextButton(onClick = onBack) { Text("← Pages") }
    Text(page.title, style = MaterialTheme.typography.headlineMedium)
    Text("Drawing canvas is the next build-gated notebook step.", color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}
