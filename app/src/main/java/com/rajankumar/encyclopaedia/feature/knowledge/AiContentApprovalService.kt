package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao

class AiContentApprovalService(
  private val dao: EncyclopaediaDao
) {
  suspend fun approve(
    proposal: AiContentProposal,
    parentNodeId: String? = null
  ): ApprovedKnowledgeBatch {
    val validation = AiContentValidator.validate(proposal)
    require(validation.isValid) {
      "AI content proposal is invalid: ${validation.errors.joinToString()}"
    }
    val batch = AiContentEntityMapper.map(proposal, parentNodeId)
    dao.saveApprovedKnowledge(batch.nodes, batch.lessons)
    return batch
  }
}
