package com.rajankumar.encyclopaedia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    KnowledgeNodeEntity::class,
    LessonEntity::class,
    QuestionEntity::class,
    QuestionTopicEntity::class,
    QuestionAttemptEntity::class,
    PlannerTaskEntity::class,
    NotebookPageEntity::class,
    NotebookLayerEntity::class,
    NotebookStrokeEntity::class
  ],
  version = DatabaseVersions.CURRENT,
  exportSchema = false
)
abstract class EncyclopaediaDatabase : RoomDatabase() {
  abstract fun dao(): EncyclopaediaDao

  companion object {
    @Volatile private var instance: EncyclopaediaDatabase? = null

    fun get(context: Context): EncyclopaediaDatabase = instance ?: synchronized(this) {
      instance ?: Room.databaseBuilder(
        context.applicationContext,
        EncyclopaediaDatabase::class.java,
        "encyclopaedia.db"
      ).addMigrations(*ALL_MIGRATIONS).build().also { instance = it }
    }
  }
}
