package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class MigrationManifestTest {
  @Test fun manifestTracksEverySchemaVersion() = assertEquals((1..DatabaseVersions.CURRENT).toList(), migrationManifest.map { it.version })
}
