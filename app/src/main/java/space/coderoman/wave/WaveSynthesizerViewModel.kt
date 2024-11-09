package space.coderoman.wave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.ln

class WaveSynthesizerViewModel: ViewModel() {
    var waveSynthesizer: WaveSynthesizer? = null
        set(value) {
            field = value
            applyParameters()
        }

    private val _frequency = MutableLiveData(300f)
    val frequency: LiveData<Float>
        get() {
            return _frequency
        }

    fun setFrequencySliderPosition(frequencySliderPosition: Float) {
        val frequencyInHz = frequencyInHzFromSliderPosition(frequencySliderPosition)
        _frequency.value = frequencyInHz
        viewModelScope.launch {
            waveSynthesizer?.setFrequency(frequencyInHz)
        }
    }

    private val _volume = MutableLiveData(-24f)
    val volume: LiveData<Float>
        get() {
            return _volume
        }
    val volumeRange = (-60f)..0f

    fun setVolume(volumeInDb: Float) {
        _volume.value = volumeInDb
        viewModelScope.launch {
            waveSynthesizer?.setVolume(volumeInDb)
        }
    }

    private var wave = Wave.SINE

    fun setWave(newWave: Wave) {
        wave = newWave
        viewModelScope.launch {
            waveSynthesizer?.setWave(newWave)
        }
    }

    private val _playButtonLabel = MutableLiveData(R.string.play)
    val playButtonLabel: LiveData<Int>
        get() {
            return _playButtonLabel
        }

    fun playClicked() {
        viewModelScope.launch {
            if (waveSynthesizer?.isPlaying() == true) {
                waveSynthesizer?.stop()
            } else {
                waveSynthesizer?.play()
            }
            updatePlayButtonLabel()
        }
    }

    private fun updatePlayButtonLabel() {
        viewModelScope.launch {
            if (waveSynthesizer?.isPlaying() == true) {
                _playButtonLabel.value = R.string.stop
            } else {
                _playButtonLabel.value = R.string.play
            }
        }
    }

    fun applyParameters() {
        viewModelScope.launch {
            waveSynthesizer?.setFrequency(frequency.value!!)
            waveSynthesizer?.setVolume(volume.value!!)
            waveSynthesizer?.setWave(wave)
            updatePlayButtonLabel()
        }
    }

    private val frequencyRange = 40f..3000f
    private fun frequencyInHzFromSliderPosition(sliderPosition: Float): Float {
        val rangePosition = linearToExponential(sliderPosition)

        return valueFromRangePosition(frequencyRange, rangePosition)
    }

    fun sliderPositionFromFrequencyInHz(frequencyInHz: Float): Float {
        val rangePosition = rangePositionFromValue(frequencyRange, frequencyInHz)
        return exponentialToLinear(rangePosition)
    }

    companion object LinearToExponential {
        private const val MINIMUM_VALUE = 0.0001f
        fun linearToExponential(value: Float): Float {
            assert(value in 0f..1f)

            if (value < MINIMUM_VALUE) {
                return 0f
            }

            return exp(ln(MINIMUM_VALUE) - ln(MINIMUM_VALUE) * value)
        }

        fun valueFromRangePosition(range: ClosedFloatingPointRange<Float>, rangePosition: Float) =
            range.start + (range.endInclusive - range.start) * rangePosition

        fun rangePositionFromValue(range: ClosedFloatingPointRange<Float>, value: Float): Float {
            assert(value in range)

            return (value - range.start) / (range.endInclusive - range.start)
        }

        fun exponentialToLinear(rangePosition: Float): Float {
            assert(rangePosition in 0f..1f)

            if (rangePosition < MINIMUM_VALUE) {
                return rangePosition
            }

            return (ln(rangePosition) - ln(MINIMUM_VALUE)) / (-ln(MINIMUM_VALUE))
        }
    }
}