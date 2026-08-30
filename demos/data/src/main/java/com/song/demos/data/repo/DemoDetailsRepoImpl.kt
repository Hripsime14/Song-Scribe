package com.song.demos.data.repo

import com.song.database.dao.DemoDao
import com.song.demos.data.mapper.toDemo
import com.song.demos.domain.repo.DemoDetailsRepo
import com.song.demos.domain.repo.model.Demo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DemoDetailsRepoImpl(
    private val demoDao: DemoDao
): DemoDetailsRepo {
    override fun getDemoDetails(demoId: String): Flow<Demo?> {
        return demoDao.observeDemo(demoId).map { it?.toDemo() }
    }
}