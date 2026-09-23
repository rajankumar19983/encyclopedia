# OCR review workflow

Recognized text must enter the import workflow through `prepareOcrImportReview`. That boundary applies English-only sanitization before parsing, attaches optional exam metadata, calculates review readiness, and creates editable review items.

Every draft remains unreviewed initially. Editing a draft resets its reviewed state. Review can only be granted when save validation passes and no Devanagari remains in question or option text.

Persistence code must consume `persistableDrafts()` rather than the raw parsed draft list. It returns no drafts until the entire queue is explicitly reviewed and valid, preventing partially reviewed OCR output from being silently saved.

The save contract accepts 2–6 options. Seven or more options require correction before approval.
