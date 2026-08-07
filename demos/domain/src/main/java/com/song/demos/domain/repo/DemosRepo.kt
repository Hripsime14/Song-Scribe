package com.song.demos.domain.repo

import com.song.demos.domain.repo.model.Demo
import kotlinx.coroutines.flow.Flow

interface DemosRepo {
    fun observeDemos(): Flow<List<Demo>>
    fun searchDemos(query: String): Flow<List<Demo>>
}