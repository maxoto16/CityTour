package com.example.citytour.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MonumentDetailViewModel : ViewModel() {
    var isPlaying by mutableStateOf(false)
        private set
    var isPaused by mutableStateOf(false)
        private set
    var currentPosition by mutableStateOf(0)
        private set
    var duration by mutableStateOf(1)
        private set
    var audioVolume by mutableStateOf(1.0f)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var mediaPlayer: MediaPlayer? = null
    private var positionJob: Job? = null
    private var currentAudioResId: Int? = null

    fun initAudio(context: Context, audioResId: Int) {
        if (mediaPlayer != null && currentAudioResId == audioResId) return

        releasePlayer()
        currentAudioResId = audioResId

        try {
            this@MonumentDetailViewModel.isLoading = true
            mediaPlayer = MediaPlayer().apply {
                val afd = context.resources.openRawResourceFd(audioResId)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                this@MonumentDetailViewModel.duration = duration.coerceAtLeast(1)
                this@MonumentDetailViewModel.isLoading = false
                this@MonumentDetailViewModel.errorMessage = null

                setOnCompletionListener {
                    this@MonumentDetailViewModel.isPlaying = false
                    this@MonumentDetailViewModel.isPaused = false
                    this@MonumentDetailViewModel.currentPosition = 0
                    seekTo(0)
                    stopPositionTicker()
                }
                setOnErrorListener { _, what, extra ->
                    this@MonumentDetailViewModel.isLoading = false
                    this@MonumentDetailViewModel.isPlaying = false
                    this@MonumentDetailViewModel.errorMessage = "No se pudo reproducir el audio local ($what, $extra)"
                    stopPositionTicker()
                    true
                }
            }
        } catch (e: Exception) {
            this@MonumentDetailViewModel.isLoading = false
            this@MonumentDetailViewModel.errorMessage = e.localizedMessage ?: "Error al inicializar reproductor"
        }
    }

    fun play() {
        mediaPlayer?.let { mp ->
            try {
                if (isPaused || !isPlaying) {
                    mp.start()
                    isPlaying = true
                    isPaused = false
                    startPositionTicker()
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun pause() {
        mediaPlayer?.let { mp ->
            try {
                if (isPlaying) {
                    mp.pause()
                    isPlaying = false
                    isPaused = true
                    stopPositionTicker()
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun stop() {
        mediaPlayer?.let { mp ->
            try {
                mp.pause()
                mp.seekTo(0)
                isPlaying = false
                isPaused = false
                currentPosition = 0
                stopPositionTicker()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        currentPosition = position
    }

    fun setVolume(newVolume: Float) {
        audioVolume = newVolume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(audioVolume, audioVolume)
    }

    private fun startPositionTicker() {
        positionJob?.cancel()
        positionJob = viewModelScope.launch {
            while (isPlaying) {
                mediaPlayer?.let { mp ->
                    try {
                        currentPosition = mp.currentPosition
                    } catch (e: Exception) {
                        // Ignore
                    }
                }
                delay(300L)
            }
        }
    }

    private fun stopPositionTicker() {
        positionJob?.cancel()
        positionJob = null
    }

    private fun releasePlayer() {
        stopPositionTicker()
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            // Ignore
        }
        mediaPlayer = null
        isPlaying = false
        isPaused = false
        currentPosition = 0
        duration = 1
        errorMessage = null
        isLoading = false
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
