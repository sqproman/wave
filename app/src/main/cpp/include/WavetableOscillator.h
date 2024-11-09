//
// Created by Roman on 08.11.2024.
//

#pragma once

#include "AudioSource.h"

namespace wavesynthesizer {
    class A4Oscillator : public AudioSource {
    public:
        explicit A4Oscillator(float sampleRate);

        float getSample() override;

        void onPlaybackStopped() override;

    private:
        float _phase{0.f};
        float _phaseIncrement{0.f};
    };

}