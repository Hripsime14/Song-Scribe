package com.song.demos.domain.repo

import com.song.demos.domain.repo.model.Demo
import kotlinx.coroutines.flow.Flow

interface DemoDetailsRepo {
    fun getDemoDetails(demoId: String): Flow<Demo?>
    suspend fun saveDemoDetailsChanged(demo: Demo)
}