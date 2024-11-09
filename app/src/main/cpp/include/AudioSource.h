//
// Created by Roman on 27.10.2024.
//

#pragma once

namespace wavesynthesizer {
    class AudioSource {
    public:
        virtual ~AudioSource() = default;

        virtual float getSample() = 0;

        virtual void onPlaybackStopped() = 0;
    };
}