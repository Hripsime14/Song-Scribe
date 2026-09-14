package com.song.demos.domain.repo

import com.song.demos.domain.repo.model.Demo

interface NewDemoRepo {
    suspend fun createDemo(demo: Demo)
}