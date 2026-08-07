package com.song.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.song.database.entity.DemoEntity
import com.song.database.entity.DemoWithRecordings
import com.song.database.entity.RecordingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DemoDao {

    @Transaction
    @Query("SELECT * FROM demo ORDER BY createdAtMillis DESC")
    fun observeDemos(): Flow<List<DemoWithRecordings>>

    @Transaction
    @Query(
        """
        SELECT * FROM demo
        WHERE :query = ''
           OR title LIKE '%' || :query || '%'
           OR genres LIKE '%' || :query || '%'
           OR id IN (
                SELECT demoId FROM recording
                WHERE title LIKE '%' || :query || '%'
           )
        ORDER BY createdAtMillis DESC
        """
    )
    fun searchDemos(query: String): Flow<List<DemoWithRecordings>>

    @Transaction
    @Query("SELECT * FROM demo WHERE id = :id")
    fun observeDemo(id: String): Flow<DemoWithRecordings?>

    @Query("SELECT COUNT(*) FROM demo")
    fun observeDemoCount(): Flow<Int>

    @Upsert
    suspend fun upsertDemo(demo: DemoEntity)

    @Upsert
    suspend fun upsertRecordings(recordings: List<RecordingEntity>)

    @Transaction
    suspend fun upsertDemoWithRecordings(demoWithRecordings: DemoWithRecordings) {
        upsertDemo(demoWithRecordings.demo)
        upsertRecordings(demoWithRecordings.recordings)
    }

    @Query("DELETE FROM demo WHERE id = :id")
    suspend fun deleteDemo(id: String)

    @Query("DELETE FROM recording WHERE id = :id")
    suspend fun deleteRecording(id: String)
}
