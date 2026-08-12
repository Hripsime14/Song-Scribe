package com.song.demos.data.repo

import com.song.database.dao.DemoDao
import com.song.demos.data.mapper.toDemo
import com.song.demos.domain.repo.DemosRepo
import com.song.demos.domain.repo.model.Demo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DemosRepoImpl(
    private val demoDao: DemoDao
) : DemosRepo {

    override fun observeDemos(): Flow<List<Demo>> {
        return demoDao.observeDemos().map { demos ->
            demos.map { it.toDemo() }
        }
    }

    override fun searchDemos(query: String): Flow<List<Demo>> {
        return demoDao.searchDemos(query).map { demos ->
            demos.map { it.toDemo() }
        }
    }

    override suspend fun deleteDemo(demoId: String) {
        demoDao.deleteDemo(demoId)
    }
}