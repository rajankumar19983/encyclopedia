# OCR import hardening

OCR imports are review-first and local-only. Before MCQ parsing, recognized text is filtered so Devanagari characters do not enter question text, options, or explanations. The original image is not required for persistence after recognition/review.

The supported MCQ option range is two through six options. Five- and six-option questions are valid inputs; larger option counts are treated as suspicious structure and must not be silently accepted.

Known exam names may be detected from printed English page text as optional metadata. Missing metadata does not invalidate a question.

This layer is deterministic: the same recognized text produces the same sanitized text and parsed drafts. Handwritten-content rejection remains an image/OCR confidence concern and must not be inferred from script filtering alone.
