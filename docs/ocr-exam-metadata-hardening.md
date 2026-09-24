# OCR exam metadata hardening

Exam attribution extracted from scans is treated as reviewable metadata, never as unquestioned truth. The extractor recognizes both common abbreviations and full names for DSSSB, BPSC, CTET, UGC NET, GATE, NIELIT, KVS and NVS. It also detects four-digit years, common numeric exam dates and shift/session labels.

Dates are normalized to `YYYY-MM-DD`, and numeric or Roman-numeral shifts are normalized to `SHIFT 1`, `SHIFT 2` or `SHIFT 3`. Confidence is based on how much independent metadata evidence was found. Unknown exam names are deliberately left unset rather than guessed.

This metadata remains separate from the English-only MCQ text sanitizer. It can be displayed during review without polluting question or option content, and low-confidence/unknown attribution can be explicitly surfaced for manual verification.
