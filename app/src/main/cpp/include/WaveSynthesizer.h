//
// Created by Roman on 06.10.2024.
//

#pragma once

#include "memory"
#include "Wave.h"

namespace wavesynthesizer {
    class AudioSource;

    class AudioPlayer;

    constexpr auto samplingRate = 48000;


    class WaveSynthesizer {
    public:
        WaveSynthesizer();

        ~WaveSynthesizer();

        void play();

        void stop();

        bool isPlaying();

        void setFrequency(float frequencyInHz);

        void setVolume(float volumeInDb);

        void setWave(Wave wave);

    private:
        bool _isPlaying = false;
        std::shared_ptr<AudioSource> _oscillator;
        std::unique_ptr<AudioPlayer> _audioPlayer;
    };
}