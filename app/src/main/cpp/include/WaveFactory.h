//
// Created by Roman on 09.11.2024.
//

#pragma once

#include <vector>

namespace wavesynthesizer {
    enum class Wave;

    class WaveFactory {
    public:
        std::vector<float> getWave(Wave wave);

        std::vector<float> sineWave();

        std::vector<float> triangleWave();

        std::vector<float> squareWave();

        std::vector<float> sawWave();

    private:
        std::vector<float> _sineWave;
        std::vector<float> _triangleWave;
        std::vector<float> _squareWave;
        std::vector<float> _sawWave;
    };
}