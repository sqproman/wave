package space.coderoman.wave

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeWaveSynthesizer : WaveSynthesizer, DefaultLifecycleObserver {
    private val prefix = "NativeWaveSynthesizer"
    private var synthesizerHandle: Long = 0
    private val synthesizerMutex = Object()
    private external fun create(): Long
    private external fun delete(synthesizerHandle: Long)
    private external fun play(synthesizerHandle: Long)
    private external fun stop(synthesizerHandle: Long)
    private external fun isPlaying(synthesizerHandle: Long): Boolean
    private external fun setFrequency(synthesizerHandle: Long, frequencyInHz: Float)
    private external fun setVolume(synthesizerHandle: Long, amplitudeInDb: Float)
    private external fun setWave(synthesizerHandle: Long, wave: Int)

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        synchronized(synthesizerMutex) {
            Log.d(prefix, "onResume called")
            createNativeHandleIfNotExists()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        synchronized(synthesizerMutex) {
            Log.d(prefix, "onPause called")

            if (synthesizerHandle == 0L) {
                Log.e(prefix, "Attempting to destroy a null synthesizer")
                return
            }

            delete(synthesizerHandle)
            synthesizerHandle = 0L
        }
    }

    private fun createNativeHandleIfNotExists() {
        if (synthesizerHandle != 0L) {
            return
        }

        synthesizerHandle = create()
    }

    override suspend fun play() = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            play(synthesizerHandle)
        }
    }

    override suspend fun stop() = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            stop(synthesizerHandle)
        }
    }


    override suspend fun isPlaying(): Boolean = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            return@withContext isPlaying(synthesizerHandle)
        }
    }

    override suspend fun setFrequency(frequencyInHz: Float) = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            setFrequency(synthesizerHandle, frequencyInHz)
        }
    }

    override suspend fun setVolume(volumeInDb: Float) = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            setVolume(synthesizerHandle, volumeInDb)
        }
    }

    override suspend fun setWave(wave: Wave) = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandleIfNotExists()
            setWave(synthesizerHandle, wave.ordinal)
        }
    }

    companion object {
        init {
            System.loadLibrary("wavesynthesizer")
        }
    }
}

