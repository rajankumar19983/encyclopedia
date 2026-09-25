package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.feature.knowledge.AiApiKeyDialog
import com.rajankumar.encyclopaedia.feature.knowledge.LocalAiApiKeyStore
import kotlinx.coroutines.launch

@Composable
fun AiQuestionFlowScreen(onDone: () -> Unit) {
  val context = LocalContext.current
  val database = EncyclopaediaDatabase.get(context)
  val dao = database.dao()
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val existingQuestions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  val keyStore = remember(context) { LocalAiApiKeyStore(context) }
  val factory = remember(context) { AiQuestionViewModelFactory(context) }
  val viewModel: AiQuestionViewModel = viewModel(factory = factory)
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  var targetTopicId by remember { mutableStateOf<String?>(null) }
  var showApiKeyDialog by remember { mutableStateOf(false) }
  var hasApiKey by remember { mutableStateOf(keyStore.hasApiKey()) }
  var isSaving by remember { mutableStateOf(false) }
  var saveError by remember { mutableStateOf<String?>(null) }

  val proposal = state.proposal
  val approvalCheck = remember(proposal, existingQuestions) {
    proposal?.let { checkAiQuestionApproval(it, existingQuestions) }
  }

  if (proposal == null) {
    AiQuestionBuilderScreen(
      state = state,
      topics = topics,
      targetTopicId = targetTopicId,
      hasApiKey = hasApiKey,
      onBack = onDone,
      onTopicChange = viewModel::setTopic,
      onCountChange = viewModel::setCount,
      onDifficultyChange = viewModel::setDifficulty,
      onTargetTopicChange = { targetTopicId = it },
      onManageApiKey = { showApiKeyDialog = true },
      onGenerate = {
        val target = topics.firstOrNull { it.id == targetTopicId }
        viewModel.generate(target?.id, target?.name)
      },
    )
  } else {
    AiQuestionReviewScreen(
      proposal = proposal,
      destinationLabel = state.destinationLabel,
      duplicateConflicts = approvalCheck?.duplicateConflicts.orEmpty(),
      isSaving = isSaving,
      isRegenerating = state.isGenerating,
      saveError = saveError,
      generationError = state.errorMessage,
      onProposalChange = {
        saveError = null
        viewModel.updateProposal(it)
      },
      onApprove = {
        if (approvalCheck?.isApprovable != true) {
          saveError = "Resolve validation and duplicate warnings before approval."
        } else if (!isSaving && !state.isGenerating) {
          isSaving = true
          saveError = null
          scope.launch {
            try {
              AiQuestionApprovalService(database).approve(proposal, state.destinationTopicId)
              viewModel.clearProposal()
              onDone()
            } catch (error: Exception) {
              saveError = error.message ?: "Could not save approved AI questions."
            } finally {
              isSaving = false
            }
          }
        }
      },
      onRegenerate = {
        saveError = null
        viewModel.regenerate()
      },
      onDiscard = {
        viewModel.clearProposal()
        onDone()
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
        viewModel.clearError()
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
