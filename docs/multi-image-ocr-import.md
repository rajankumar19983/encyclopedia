# Multi-image OCR import

The question importer supports selecting multiple scanned images in one operation. Images are OCR'd sequentially in the user's selection order so memory remains bounded and question text spanning several scans is assembled deterministically before parsing.

Progress is reported after each image. A failed image is retained as a source-level OCR failure while successfully recognized images remain available for review; the batch is never silently treated as clean when any selected image fails.

Source images remain transient and are not copied into application storage. The combined OCR text continues through the same English-only sanitization, manual verification, duplicate protection, and explicit-save workflow as single-document imports.
