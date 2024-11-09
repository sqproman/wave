//
// Created by Roman on 06.10.2024.
//

#include "Log.h"
#include "WaveSynthesizer.h"
#include "OboeAudioPlayer.h"
#include "WavetableOscillator.h"

namespace wavesynthesizer {

    WaveSynthesizer::WaveSynthesizer()
            : _oscillator{std::make_shared<A4Oscillator>(samplingRate)},
              _audioPlayer{
                      std::make_unique<OboeAudioPlayer>(
                              _oscillator, samplingRate)} {}

    WaveSynthesizer::~WaveSynthesizer() = default;

    bool WaveSynthesizer::isPlaying() {
        LOGD("isPlaying() called");
        return _isPlaying;
    }

    void WaveSynthesizer::play() {
        LOGD("play() called");

        const auto result = _audioPlayer->play();
        if (result == 0) {
            _isPlaying = true;
        } else {
            LOGD("Could not start playback.");
        }
    }

    void WaveSynthesizer::setFrequency(float frequencyInHz) {
        LOGD("frequency set to %.2f Hz.", frequencyInHz);
    }

    void WaveSynthesizer::setVolume(float volumeInDb) {
        LOGD("Volume set to %.2f dBFS", volumeInDb);
    }

    void WaveSynthesizer::setWave(wavesynthesizer::Wave wave) {
        LOGD("wave set to %d", static_cast<int>(wave));
    }

    void WaveSynthesizer::stop() {
        LOGD("stop called");
        _audioPlayer->stop();
        _isPlaying = false;
    }


}
