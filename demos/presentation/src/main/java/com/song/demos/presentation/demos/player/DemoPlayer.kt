package com.song.demos.presentation.demos.player

import android.media.MediaPlayer

class DemoPlayer{

    private var mediaPlayer: MediaPlayer? = null

    val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying == true

    val currentPositionMillis: Int
        get() = mediaPlayer?.currentPosition ?: 0

    fun play(filePath: String, startPositionMillis: Int = 0, onComplete: () -> Unit) {
        stop()
        mediaPlayer = MediaPlayer().apply{
            setDataSource(filePath)
            setOnCompletionListener { onComplete() }
            prepare()
            if (startPositionMillis > 0) seekTo(startPositionMillis)
            start()
        }
    }

    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun stop() {
        mediaPlayer?.apply {
            runCatching { stop() }
            reset()
            release()
        }
        mediaPlayer = null
    }

}