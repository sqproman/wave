//
// Created by Roman on 27.10.2024.
//

#pragma once

#include "oboe/Oboe.h"
#include "AudioPlayer.h"

namespace wavesynthesizer {
    class AudioSource;

    class OboeAudioPlayer : public oboe::AudioStreamDataCallback, public AudioPlayer {
    public:
        static constexpr auto channelCount = oboe::ChannelCount::Mono;

        OboeAudioPlayer(std::shared_ptr<AudioSource> source, int samplingRate);

        ~OboeAudioPlayer();

        int32_t play() override;

        void stop() override;

        oboe::DataCallbackResult
        onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t framesCount) override;

    private:
        std::shared_ptr<AudioSource> _source;
        std::shared_ptr<oboe::AudioStream> _stream;

        int _samplingRate;

    };
}