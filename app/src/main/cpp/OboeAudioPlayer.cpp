//
// Created by Roman on 08.11.2024.
//

#include "OboeAudioPlayer.h"
#include "utility"
#include "AudioSource.h"

using namespace oboe;

namespace wavesynthesizer {
    OboeAudioPlayer::OboeAudioPlayer(std::shared_ptr<AudioSource> source, int samplingRate)
            : _source(std::move(source)), _samplingRate(samplingRate) {}

    OboeAudioPlayer::~OboeAudioPlayer() {
        OboeAudioPlayer::stop();
    }

    int32_t OboeAudioPlayer::play() {
        AudioStreamBuilder builder;

        const auto result =
                builder.setPerformanceMode(PerformanceMode::LowLatency)
                        ->setDirection(Direction::Output)
                        ->setSampleRate(_samplingRate)
                        ->setDataCallback(this)
                        ->setSharingMode(SharingMode::Exclusive)
                        ->setFormat(AudioFormat::Float)
                        ->setChannelCount(channelCount)
                        ->setSampleRateConversionQuality(
                                SampleRateConversionQuality::Best
                        )
                        ->openStream(_stream);

        if (result != Result::OK) {
            return static_cast<int32_t>(result);
        }

        const auto playResult = _stream->requestStart();

        return static_cast<int32_t>(playResult);
    }

    void OboeAudioPlayer::stop() {
        if (_stream) {
            _stream->stop();
            _stream->close();
            _stream.reset();
        }

        _source->onPlaybackStopped();
    }

    DataCallbackResult
    OboeAudioPlayer::onAudioReady(oboe::AudioStream *audioStream, void *audioData,
                                  int32_t framesCount) {
        auto *floatData = reinterpret_cast<float *>(audioData);

        for (auto frame = 0; frame < framesCount; ++frame) {
            const auto sample = _source->getSample();
            for (auto channel = 0; channel < channelCount; ++channel) {
                floatData[frame * channelCount + channel] = sample;
            }
        }

        return oboe::DataCallbackResult::Continue;
    }
}
