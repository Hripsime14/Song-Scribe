package com.song.demos.data.repo

import com.song.database.dao.DemoDao
import com.song.database.entity.DemoWithRecordings
import com.song.demos.data.mapper.toDemoEntity
import com.song.demos.data.mapper.toRecordingEntities
import com.song.demos.domain.repo.NewDemoRepo
import com.song.demos.domain.repo.model.Demo

class NewDemoRepoImpl(
    private val demoDao: DemoDao
) : NewDemoRepo {

    override suspend fun createDemo(demo: Demo) {
        demoDao.upsertDemoWithRecordings(
            DemoWithRecordings(
                demo = demo.toDemoEntity(),
                recordings = demo.toRecordingEntities()
            )
        )
    }
}