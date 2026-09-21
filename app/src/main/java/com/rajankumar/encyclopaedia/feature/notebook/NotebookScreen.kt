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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

private data class NotebookHistoryAction(val stroke: NotebookStrokeEntity, val added: Boolean)

@Composable
fun NotebookScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  val pages by dao.observeNotebookPages().collectAsStateWithLifecycle(emptyList())
  var title by remember { mutableStateOf("") }
  var pageToDelete by remember { mutableStateOf<NotebookPageEntity?>(null) }
  var selectedPage by remember { mutableStateOf<NotebookPageEntity?>(null) }

  pageToDelete?.let { page ->
    AlertDialog(onDismissRequest = { pageToDelete = null }, title = { Text("Delete notebook page?") }, text = { Text("${page.title} and every layer and handwritten stroke on it will be permanently deleted.") }, confirmButton = { TextButton(onClick = { pageToDelete = null; scope.launch(Dispatchers.IO) { dao.deleteNotebookPage(page.id) } }) { Text("Delete") } }, dismissButton = { TextButton(onClick = { pageToDelete = null }) { Text("Cancel") } })
  }
  selectedPage?.let { page -> NotebookPageCanvas(page = page, onBack = { selectedPage = null }); return }

  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    item { Text("Notebook", style = MaterialTheme.typography.headlineMedium); Text("Handwritten study pages are stored locally and included in your backups.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text("New page", style = MaterialTheme.typography.titleMedium)
      OutlinedTextField(value = title, onValueChange = { title = it.take(100) }, label = { Text("Page title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
      Button(enabled = title.isNotBlank(), onClick = { val pageTitle = title.trim(); title = ""; scope.launch(Dispatchers.IO) { val pageId = UUID.randomUUID().toString(); dao.upsertNotebookPage(NotebookPageEntity(id = pageId, title = pageTitle)); dao.upsertNotebookLayer(NotebookLayerEntity(id = UUID.randomUUID().toString(), pageId = pageId, name = "Writing")) } }) { Text("Create page") }
    } } }
    if (pages.isEmpty()) item { Text("No notebook pages yet. Create one above to start writing.") }
    else items(pages, key = { it.id }) { page -> Card(Modifier.fillMaxWidth().clickable { selectedPage = page }) { Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { Text(page.title, style = MaterialTheme.typography.titleMedium); Text(page.background.lowercase().replaceFirstChar { it.uppercase() } + " page", color = MaterialTheme.colorScheme.onSurfaceVariant) }; TextButton(onClick = { pageToDelete = page }) { Text("Delete", color = MaterialTheme.colorScheme.error) } } } }
  }
}

@Composable
private fun NotebookPageCanvas(page: NotebookPageEntity, onBack: () -> Unit) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  val layers by dao.observeNotebookLayers(page.id).collectAsStateWithLifecycle(emptyList())
  var selectedLayerId by remember { mutableStateOf<String?>(null) }
  val activeLayer = layers.firstOrNull { it.id == selectedLayerId && it.isVisible && !it.isLocked }
    ?: layers.firstOrNull { it.isVisible && !it.isLocked }
  val stored by (activeLayer?.let { dao.observeNotebookStrokes(it.id) } ?: flowOf(emptyList())).collectAsStateWithLifecycle(emptyList())
  var palmRejection by remember { mutableStateOf(true) }
  var selectedTool by remember { mutableStateOf(NotebookTool.PEN) }
  var newLayerName by remember { mutableStateOf("") }
  var layerToDelete by remember { mutableStateOf<NotebookLayerEntity?>(null) }
  val undoStack = remember { mutableStateListOf<NotebookHistoryAction>() }
  val redoStack = remember { mutableStateListOf<NotebookHistoryAction>() }
  val decodedStrokes = remember(stored) { stored.mapNotNull { entity -> decodeStroke(entity)?.let { entity to it } } }
  val canvasStrokes = remember(decodedStrokes) { decodedStrokes.map { it.second } }

  layerToDelete?.let { layer -> AlertDialog(onDismissRequest = { layerToDelete = null }, title = { Text("Delete layer?") }, text = { Text("${layer.name} and every stroke on it will be permanently deleted.") }, confirmButton = { TextButton(onClick = { layerToDelete = null; undoStack.clear(); redoStack.clear(); scope.launch(Dispatchers.IO) { dao.deleteNotebookLayer(layer.id) } }) { Text("Delete") } }, dismissButton = { TextButton(onClick = { layerToDelete = null }) { Text("Cancel") } }) }

  Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { TextButton(onClick = onBack) { Text("← Pages") }; Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Text("Palm rejection"); Switch(checked = palmRejection, onCheckedChange = { palmRejection = it }) } }
    Text(page.title, style = MaterialTheme.typography.headlineSmall)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      FilterChip(selected = selectedTool == NotebookTool.PEN, onClick = { selectedTool = NotebookTool.PEN }, label = { Text("Pen") })
      FilterChip(selected = selectedTool == NotebookTool.ERASER, onClick = { selectedTool = NotebookTool.ERASER }, label = { Text("Eraser") })
      OutlinedButton(enabled = undoStack.isNotEmpty(), onClick = { val action = undoStack.removeLastOrNull() ?: return@OutlinedButton; redoStack.add(action); scope.launch(Dispatchers.IO) { if (action.added) dao.deleteNotebookStroke(action.stroke.id) else dao.upsertNotebookStroke(action.stroke) } }) { Text("Undo") }
      OutlinedButton(enabled = redoStack.isNotEmpty(), onClick = { val action = redoStack.removeLastOrNull() ?: return@OutlinedButton; undoStack.add(action); scope.launch(Dispatchers.IO) { if (action.added) dao.upsertNotebookStroke(action.stroke) else dao.deleteNotebookStroke(action.stroke.id) } }) { Text("Redo") }
    }
    Text(if (palmRejection) "Stylus only — finger and palm touches are ignored." else "Touch drawing enabled.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text("Layers", style = MaterialTheme.typography.titleSmall)
      layers.forEach { layer -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        FilterChip(selected = activeLayer?.id == layer.id, enabled = layer.isVisible && !layer.isLocked, onClick = { selectedLayerId = layer.id; undoStack.clear(); redoStack.clear() }, label = { Text(layer.name) })
        Row { TextButton(onClick = { scope.launch(Dispatchers.IO) { dao.setNotebookLayerVisible(layer.id, !layer.isVisible) } }) { Text(if (layer.isVisible) "Hide" else "Show") }; TextButton(onClick = { scope.launch(Dispatchers.IO) { dao.setNotebookLayerLocked(layer.id, !layer.isLocked) } }) { Text(if (layer.isLocked) "Unlock" else "Lock") }; if (layers.size > 1) TextButton(onClick = { layerToDelete = layer }) { Text("Delete", color = MaterialTheme.colorScheme.error) } }
      } }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedTextField(value = newLayerName, onValueChange = { newLayerName = it.take(40) }, label = { Text("New layer") }, singleLine = true, modifier = Modifier.weight(1f)); Button(enabled = newLayerName.isNotBlank(), onClick = { val name = newLayerName.trim(); newLayerName = ""; val id = UUID.randomUUID().toString(); scope.launch(Dispatchers.IO) { dao.upsertNotebookLayer(NotebookLayerEntity(id = id, pageId = page.id, name = name, sortOrder = layers.size)) }; selectedLayerId = id; undoStack.clear(); redoStack.clear() }) { Text("Add") } }
    } }
    Card(Modifier.fillMaxWidth().weight(1f)) {
      if (activeLayer == null) Text("Select a visible, unlocked layer to write.", Modifier.padding(16.dp))
      else NotebookCanvas(strokes = canvasStrokes, palmRejection = palmRejection, selectedTool = selectedTool, onStrokeFinished = { stroke -> val entity = NotebookStrokeEntity(id = UUID.randomUUID().toString(), layerId = activeLayer.id, pointsJson = encodePoints(stroke), width = stroke.width, tool = stroke.tool.name); undoStack.add(NotebookHistoryAction(entity, true)); redoStack.clear(); scope.launch(Dispatchers.IO) { dao.upsertNotebookStroke(entity) } }, onEraseAt = { point -> val hit = decodedStrokes.lastOrNull { (_, stroke) -> stroke.isNear(point) }?.first; if (hit != null) { undoStack.add(NotebookHistoryAction(hit, false)); redoStack.clear(); scope.launch(Dispatchers.IO) { dao.deleteNotebookStroke(hit.id) } } }, modifier = Modifier.fillMaxSize())
    }
  }
}

private fun encodePoints(stroke: CanvasStroke): String = JSONArray().apply { stroke.points.forEach { put(JSONObject().put("x", it.x.toDouble()).put("y", it.y.toDouble())) } }.toString()
private fun decodeStroke(stroke: NotebookStrokeEntity): CanvasStroke? = runCatching { val array = JSONArray(stroke.pointsJson); val points = (0 until array.length()).map { index -> val point = array.getJSONObject(index); Offset(point.getDouble("x").toFloat(), point.getDouble("y").toFloat()) }; CanvasStroke(points, stroke.width, runCatching { NotebookTool.valueOf(stroke.tool) }.getOrDefault(NotebookTool.PEN)) }.getOrNull()
