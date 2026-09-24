package com.rajankumar.encyclopaedia.feature.importer

fun OcrExtractionResult.metadataAudit(): OcrMetadataAudit = auditOcrMetadata(orderedPages)
