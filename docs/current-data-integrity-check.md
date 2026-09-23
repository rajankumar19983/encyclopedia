# Current-data integrity check

The Backup screen exposes an explicit integrity check for the live local database. The check creates an in-memory `BackupSnapshot` from Room and runs the same integrity rules used by backup creation and restore inspection.

The result separates blocking errors from warnings, reports the number of records inspected, and lists blocking findings before warning-only findings. Each finding now has a readable category and a concrete next-step recommendation. Running the check does not mutate study data and does not create a backup file.

## Restore policy

Integrity warnings are reviewable but do not block restore. Structural errors are blocking. The snapshot restore gate and `IntegrityReport.canRestore()` intentionally use the same severity policy so diagnostics and actual restore behavior cannot disagree.

Snapshot record counting also has a single source in `BackupSnapshot.recordCount`, which is reused by reports and summaries.

This keeps one integrity contract across current-data diagnostics, backup validation, and restore safety review.
