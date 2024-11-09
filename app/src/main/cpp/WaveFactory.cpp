//
// Created by Roman on 09.11.2024.
//

#include "WaveFactory.h"
#include <cmath>
#include <vector>
#include "Wave.h"
#include "MathConstants.h"


// https://thewolfsound.com/android-synthesizer-6-wavetable-synthesis-in-c-plus-plus/

namespace wavesynthesizer {
    static constexpr auto WAVETABLE_LENGTH = 256;

    std::vector<float> generateSineWave() {
        auto sineWave = std::vector<float>(WAVETABLE_LENGTH);

        for (auto i = 0; i < WAVETABLE_LENGTH; ++i) {
            sineWave[i] =
                    std::sinf(2 * PI * static_cast<float>(i) / WAVETABLE_LENGTH);
        }

        return sineWave;
    }

    std::vector<float> WaveFactory::getWave(Wave wave) {
        switch (wave) {
            case Wave::SINE:
                return sineWave();
            default:
                return {WAVETABLE_LENGTH, 0.f};
        }
    }

    template<typename F>
    std::vector<float> generateWaveOnce(std::vector<float> &waveTable,
                                        F &&generator) {
        if (waveTable.empty()) {
            waveTable = generator();
        }

        return waveTable;
    }

    std::vector<float> WaveFactory::sineWave() {
        return generateWaveOnce(_sineWave, &generateSineWave);
    }
}