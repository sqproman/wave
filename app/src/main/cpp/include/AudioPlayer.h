//
// Created by Roman on 22.10.2024.
//

#pragma once

#include <cstdint>

namespace wavesynthesizer {
    class AudioPlayer {
    public:
        virtual ~AudioPlayer() = default;

        virtual int32_t play() = 0;

        virtual void stop() = 0;
    };
}