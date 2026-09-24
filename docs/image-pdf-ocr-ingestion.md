# Image and PDF OCR ingestion

Image and PDF imports use the bundled ML Kit Latin recognizer, so recognition works offline after the app is installed. Source images and rendered PDF pages are transient inputs only; the pipeline returns recognized text and review state and does not copy source media into application storage.

PDFs are processed sequentially from page 1 through the renderer's full page count. Each page bitmap is rendered, recognized, and recycled before the next page is opened, keeping memory bounded to roughly one rendered page at a time. There is no application-level page-count cap in this pipeline.

Recognition failures are retained as page-level errors. Successfully recognized pages remain available, but any failed page marks the extraction for review. Combined text is always assembled in page-number order and then passed through the existing English-only OCR review/parser boundary.

`QuestionImportScreen` uses `OcrDocumentImporter` for both image and PDF selection. Extraction notices are surfaced directly in the review UI, and the existing manual verification and duplicate-safe persistence flow remains the only path that saves a question. The legacy `MlKitOcrEngine` and `PdfOcrEngine` implementations were removed so there is one OCR ingestion path to maintain.
