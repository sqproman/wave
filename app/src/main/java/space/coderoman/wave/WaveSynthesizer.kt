package space.coderoman.wave

import androidx.annotation.StringRes

enum class Wave {
   SINE{
       @StringRes
       override fun toResourceString(): Int {
           return R.string.sine
       }
   },
    TRIANGLE{
        @StringRes
        override fun toResourceString(): Int {
            return R.string.triandle
        }
    },
    SQUARE{
        @StringRes
        override fun toResourceString(): Int {
            return R.string.square
        }
    },
    SAW{
        @StringRes
        override fun toResourceString(): Int {
            return R.string.sawtooth
        }
    };
    @StringRes
    abstract fun toResourceString(): Int
}

interface WaveSynthesizer {
    suspend fun play()
    suspend fun stop()
    suspend fun isPlaying(): Boolean
    suspend fun setFrequency(frequencyInHz: Float)
    suspend fun setVolume(volumeInDb: Float)
    suspend fun setWave(wave: Wave)
}