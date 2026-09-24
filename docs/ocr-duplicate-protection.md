# OCR duplicate protection

OCR imports use deterministic normalized fingerprints to prevent accidental duplicate questions from being persisted. Fingerprints include normalized question text and option content, so harmless differences in capitalization, spacing, or punctuation do not bypass duplicate detection while questions with materially different options remain distinct.

The review queue audits two duplicate sources: repeated questions inside the current OCR batch and matches supplied from the existing local study bank. Duplicate items remain reviewable/editable but the persistence plan is blocked until conflicts are resolved.

Database-facing code should map existing questions to `ExistingQuestionFingerprint` and pass them to `persistencePlan` / `persistableUniqueDrafts`. This keeps duplicate matching deterministic and independent of UI state or OCR source images.
