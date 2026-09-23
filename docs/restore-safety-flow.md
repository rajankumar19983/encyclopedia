# Restore safety flow

Restore is intentionally a two-stage operation.

1. Decode the selected restore point without mutating Room data.
2. Run backup compatibility and integrity inspection.
3. Block restore when compatibility or integrity errors are present.
4. Show warning-only findings and require explicit acknowledgement.
5. Create a fresh safety backup of the current database.
6. Decode and validate the selected backup again immediately before applying it.
7. Replace local study data only after all gates pass.

The restore review UI is driven by `BackupInspection`, so the same severity rules used by tests and backup validation determine what the user can execute.
