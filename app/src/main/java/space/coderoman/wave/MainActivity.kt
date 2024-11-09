package space.coderoman.wave

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import space.coderoman.wave.ui.theme.WaveTheme

class MainActivity : ComponentActivity() {
    private val synthesizer = NativeWaveSynthesizer()
    private val synthesizerViewModel: WaveSynthesizerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        lifecycle.addObserver(synthesizer)

        synthesizerViewModel.waveSynthesizer = synthesizer
        setContent {
            WaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WaveApp(
                        modifier = Modifier.padding(innerPadding),
                        synthesizerViewModel
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        synthesizerViewModel.applyParameters()
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.removeObserver(synthesizer)
    }
}

@Composable
fun WaveApp(modifier: Modifier = Modifier, synthesizerViewModel: WaveSynthesizerViewModel) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        WaveSelectionPanel(modifier, synthesizerViewModel)
        ControlsPanel(modifier, synthesizerViewModel)
    }
}

@Composable
fun WaveSelectionPanel(modifier: Modifier, synthesizerViewModel: WaveSynthesizerViewModel) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.wave))
            WaveSelectionButtons(modifier, synthesizerViewModel)
        }
    }
}

@Composable
fun ControlsPanel(modifier: Modifier, synthesizerViewModel: WaveSynthesizerViewModel) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(0.7f)
                .padding(11.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PitchControl(modifier, synthesizerViewModel)
            PlayControl(modifier, synthesizerViewModel)
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VolumeControl(modifier, synthesizerViewModel)
        }
    }

}

@Composable
private fun VolumeControl(modifier: Modifier, synthesizerViewModel: WaveSynthesizerViewModel) {
    val volume = synthesizerViewModel.volume.observeAsState()

    VolumeControlContent(
        modifier = modifier,
        volume = volume.value!!,
        volumeRange = -60F..0F,
        onValueChange = { synthesizerViewModel.setVolume(it) }
    )
}

@Composable
private fun VolumeControlContent(
    modifier: Modifier,
    volume: Float,
    volumeRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sliderHeight = screenHeight / 4

    Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .offset(y = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Slider(
            value = volume,
            onValueChange = onValueChange,
            modifier = modifier
                .width(sliderHeight.dp)
                .rotate(270f),
            valueRange = volumeRange
        )
    }
    Icon(imageVector = Icons.AutoMirrored.Filled.VolumeDown, contentDescription = null)
}

@Composable
private fun PlayControl(modifier: Modifier, synthesizerViewModel: WaveSynthesizerViewModel) {

    val playButtonLabel = synthesizerViewModel.playButtonLabel.observeAsState()
    PlayControlContent(
        modifier = modifier,
        onClick = { synthesizerViewModel.playClicked() },
        buttonLabel = stringResource(playButtonLabel.value!!)
    )

}

@Composable
private fun PlayControlContent(
    modifier: Modifier,
    onClick: () -> Unit,
    buttonLabel: String
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(buttonLabel)
    }
}

@Composable
private fun PitchControl(
    modifier: Modifier,
    synthesizerViewModel: WaveSynthesizerViewModel
) {
    val frequency = synthesizerViewModel.frequency.observeAsState()
    val sliderPosition = rememberSaveable {
        mutableFloatStateOf(
            synthesizerViewModel.sliderPositionFromFrequencyInHz(frequency.value!!)
        )
    }

    PitchControlContent(
        modifier = modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = sliderPosition.floatValue,
        onValueChange = {
            sliderPosition.floatValue = it
            synthesizerViewModel.setFrequencySliderPosition(it)
        },
        valueRange = 0F..1F,
        frequencyValueLabel = stringResource(
            R.string.frequency_value, frequency.value!!
        )
    )
}

@Composable
private fun PitchControlContent(
    modifier: Modifier,
    pitchControlLabel: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    frequencyValueLabel: String
) {
    Text(pitchControlLabel, modifier = modifier)
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(modifier = modifier, text = frequencyValueLabel)
    }
}

@Composable
private fun WaveSelectionButtons(
    modifier: Modifier, synthesizerViewModel: WaveSynthesizerViewModel
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (wave in Wave.entries) {
            WaveButton(
                modifier = modifier
                    .requiredWidth(140.dp),
                onClick = {
                    synthesizerViewModel.setWave(wave)
                },
                label = stringResource(wave.toResourceString())
            )
        }
    }
}

@Composable
private fun WaveButton(
    modifier: Modifier,
    onClick: () -> Unit,
    label: String
) {
    Button(modifier = modifier, onClick = onClick) {
        Text(label)
    }
}
