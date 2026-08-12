package com.song.demos.presentation.addnewdemo.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun start(): File {
        stop()

        val file = File(context.filesDir, "demo_${System.currentTimeMillis()}.m4a")
        val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128_000)
            setAudioSamplingRate(44_100)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        recorder = mediaRecorder
        outputFile = file
        return file
    }

    fun stop(): File? {
        val mediaRecorder = recorder ?: return null
        runCatching { mediaRecorder.stop() }
        mediaRecorder.reset()
        mediaRecorder.release()
        recorder = null
        return outputFile
    }
}
