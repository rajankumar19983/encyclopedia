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
  pageToDelete?.let { page -> AlertDialog(onDismissRequest = { pageToDelete = null }, title = { Text("Delete notebook page?") }, text = { Text("${page.title} and every layer and handwritten stroke on it will be permanently deleted.") }, confirmButton = { TextButton(onClick = { pageToDelete = null; scope.launch(Dispatchers.IO) { dao.deleteNotebookPage(page.id) } }) { Text("Delete") } }, dismissButton = { TextButton(onClick = { pageToDelete = null }) { Text("Cancel") } }) }
  selectedPage?.let { page -> NotebookPageCanvas(page, onBack = { selectedPage = null }); return }
  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    item { Text("Notebook", style = MaterialTheme.typography.headlineMedium); Text("Handwritten study pages are stored locally and included in your backups.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { Text("New page", style = MaterialTheme.typography.titleMedium); OutlinedTextField(title, { title = it.take(100) }, label = { Text("Page title") }, modifier = Modifier.fillMaxWidth(), singleLine = true); Button(enabled = title.isNotBlank(), onClick = { val pageTitle=title.trim(); title=""; scope.launch(Dispatchers.IO) { val id=UUID.randomUUID().toString(); dao.upsertNotebookPage(NotebookPageEntity(id=id,title=pageTitle)); dao.upsertNotebookLayer(NotebookLayerEntity(id=UUID.randomUUID().toString(),pageId=id,name="Writing")) } }) { Text("Create page") } } } }
    if (pages.isEmpty()) item { Text("No notebook pages yet. Create one above to start writing.") } else items(pages,key={it.id}) { page -> Card(Modifier.fillMaxWidth().clickable { selectedPage=page }) { Row(Modifier.fillMaxWidth().padding(16.dp),horizontalArrangement=Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { Text(page.title,style=MaterialTheme.typography.titleMedium); Text(page.background.lowercase().replaceFirstChar{it.uppercase()}+" page",color=MaterialTheme.colorScheme.onSurfaceVariant) }; TextButton(onClick={pageToDelete=page}) { Text("Delete",color=MaterialTheme.colorScheme.error) } } } }
  }
}

@Composable
private fun NotebookPageCanvas(page: NotebookPageEntity, onBack: () -> Unit) {
  val dao=EncyclopaediaDatabase.get(LocalContext.current).dao(); val scope=rememberCoroutineScope()
  val layers by dao.observeNotebookLayers(page.id).collectAsStateWithLifecycle(emptyList())
  val pageStrokes by dao.observeNotebookPageStrokes(page.id).collectAsStateWithLifecycle(emptyList())
  var selectedLayerId by remember { mutableStateOf<String?>(null) }
  val selectedLayer=layers.firstOrNull{it.id==selectedLayerId} ?: layers.firstOrNull{it.isVisible&&!it.isLocked}
  val writableLayer=selectedLayer?.takeIf{it.isVisible&&!it.isLocked}
  var palmRejection by remember{mutableStateOf(true)}; var selectedTool by remember{mutableStateOf(NotebookTool.PEN)}; var newLayerName by remember{mutableStateOf("")}; var layerToDelete by remember{mutableStateOf<NotebookLayerEntity?>(null)}
  val undoStack=remember{mutableStateListOf<NotebookHistoryAction>()}; val redoStack=remember{mutableStateListOf<NotebookHistoryAction>()}
  val visibleLayerIds=remember(layers){layers.filter{it.isVisible}.map{it.id}.toSet()}
  val decodedPageStrokes=remember(pageStrokes){pageStrokes.mapNotNull{e->decodeStroke(e)?.let{e to it}}}
  val canvasStrokes=remember(decodedPageStrokes,visibleLayerIds){decodedPageStrokes.filter{it.first.layerId in visibleLayerIds}.map{it.second}}
  val selectedDecoded=remember(decodedPageStrokes,selectedLayer?.id){decodedPageStrokes.filter{it.first.layerId==selectedLayer?.id}}

  layerToDelete?.let{layer->AlertDialog(onDismissRequest={layerToDelete=null},title={Text("Delete layer?")},text={Text("${layer.name} and every stroke on it will be permanently deleted.")},confirmButton={TextButton(onClick={layerToDelete=null;undoStack.clear();redoStack.clear();scope.launch(Dispatchers.IO){dao.deleteNotebookLayer(layer.id)}}){Text("Delete")}},dismissButton={TextButton(onClick={layerToDelete=null}){Text("Cancel")}})}
  Column(Modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){TextButton(onClick=onBack){Text("← Pages")};Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Text("Palm rejection");Switch(palmRejection,{palmRejection=it})}}
    Text(page.title,style=MaterialTheme.typography.headlineSmall)
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(selectedTool==NotebookTool.PEN,{selectedTool=NotebookTool.PEN},label={Text("Pen")});FilterChip(selectedTool==NotebookTool.ERASER,{selectedTool=NotebookTool.ERASER},label={Text("Eraser")});OutlinedButton(enabled=undoStack.isNotEmpty(),onClick={val a=undoStack.removeLastOrNull()?:return@OutlinedButton;redoStack.add(a);scope.launch(Dispatchers.IO){if(a.added)dao.deleteNotebookStroke(a.stroke.id)else dao.upsertNotebookStroke(a.stroke)}}){Text("Undo")};OutlinedButton(enabled=redoStack.isNotEmpty(),onClick={val a=redoStack.removeLastOrNull()?:return@OutlinedButton;undoStack.add(a);scope.launch(Dispatchers.IO){if(a.added)dao.upsertNotebookStroke(a.stroke)else dao.deleteNotebookStroke(a.stroke.id)}}){Text("Redo")}}
    Text(if(palmRejection)"Stylus only — finger and palm touches are ignored." else "Touch drawing enabled.",color=MaterialTheme.colorScheme.onSurfaceVariant)
    Card(Modifier.fillMaxWidth()){Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text("Layers",style=MaterialTheme.typography.titleSmall);layers.forEach{layer->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){FilterChip(selected=selectedLayer?.id==layer.id,onClick={selectedLayerId=layer.id;undoStack.clear();redoStack.clear()},label={Text(layer.name)});Row{TextButton(onClick={scope.launch(Dispatchers.IO){dao.setNotebookLayerVisible(layer.id,!layer.isVisible)}}){Text(if(layer.isVisible)"Hide" else "Show")};TextButton(onClick={scope.launch(Dispatchers.IO){dao.setNotebookLayerLocked(layer.id,!layer.isLocked)}}){Text(if(layer.isLocked)"Unlock" else "Lock")};if(layers.size>1)TextButton(onClick={layerToDelete=layer}){Text("Delete",color=MaterialTheme.colorScheme.error)}}}};Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedTextField(newLayerName,{newLayerName=it.take(40)},label={Text("New layer")},singleLine=true,modifier=Modifier.weight(1f));Button(enabled=newLayerName.isNotBlank(),onClick={val name=newLayerName.trim();newLayerName="";val id=UUID.randomUUID().toString();scope.launch(Dispatchers.IO){dao.upsertNotebookLayer(NotebookLayerEntity(id=id,pageId=page.id,name=name,sortOrder=layers.size))};selectedLayerId=id;undoStack.clear();redoStack.clear()}){Text("Add")}}}}
    Card(Modifier.fillMaxWidth().weight(1f)){NotebookCanvas(strokes=canvasStrokes,palmRejection=palmRejection,selectedTool=selectedTool,onStrokeFinished={stroke->val layer=writableLayer?:return@NotebookCanvas;val entity=NotebookStrokeEntity(id=UUID.randomUUID().toString(),layerId=layer.id,pointsJson=encodePoints(stroke),width=stroke.width,tool=stroke.tool.name);undoStack.add(NotebookHistoryAction(entity,true));redoStack.clear();scope.launch(Dispatchers.IO){dao.upsertNotebookStroke(entity)}},onEraseAt={point->val layer=writableLayer?:return@NotebookCanvas;val hit=selectedDecoded.lastOrNull{(_,s)->s.isNear(point)}?.first;if(hit!=null){undoStack.add(NotebookHistoryAction(hit,false));redoStack.clear();scope.launch(Dispatchers.IO){dao.deleteNotebookStroke(hit.id)}}},modifier=Modifier.fillMaxSize())}
    if(writableLayer==null) Text("Selected layer is hidden or locked. It remains visible when applicable, but cannot be edited.",color=MaterialTheme.colorScheme.onSurfaceVariant)
  }
}

private fun encodePoints(stroke:CanvasStroke):String=JSONArray().apply{stroke.points.forEach{put(JSONObject().put("x",it.x.toDouble()).put("y",it.y.toDouble()))}}.toString()
private fun decodeStroke(stroke:NotebookStrokeEntity):CanvasStroke?=runCatching{val a=JSONArray(stroke.pointsJson);val p=(0 until a.length()).map{i->val o=a.getJSONObject(i);Offset(o.getDouble("x").toFloat(),o.getDouble("y").toFloat())};CanvasStroke(p,stroke.width,runCatching{NotebookTool.valueOf(stroke.tool)}.getOrDefault(NotebookTool.PEN))}.getOrNull()
