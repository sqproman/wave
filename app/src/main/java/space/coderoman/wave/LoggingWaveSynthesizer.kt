package space.coderoman.wave

import android.util.Log

class LoggingWaveSynthesizer: WaveSynthesizer {
    private var isPlaying = false
    private val prefix = "LoggingWave"

    override suspend fun play() {
        Log.d(prefix, "play called")
        isPlaying = true
    }

    override suspend fun stop() {
        Log.d(prefix, "stop called")
        isPlaying = false
    }

    override suspend fun isPlaying(): Boolean {
        return isPlaying
    }

    override suspend fun setFrequency(frequencyInHz: Float) {
        Log.d(prefix, "Frequency set to $frequencyInHz")
    }

    override suspend fun setVolume(volumeInDb: Float) {
        Log.d(prefix, "Volume set to $volumeInDb")
    }

    override suspend fun setWave(wave: Wave) {
        Log.d(prefix, "Wave set to $wave")
    }
}
