# Current-data integrity check

The Backup screen exposes an explicit integrity check for the live local database. The check creates an in-memory `BackupSnapshot` from Room and runs the same integrity rules used by backup creation and restore inspection.

The result separates blocking errors from warnings, reports the number of records inspected, and lists blocking findings before warning-only findings. Running the check does not mutate study data and does not create a backup file.

This keeps one integrity contract across three paths: current-data diagnostics, backup validation, and restore safety review.
