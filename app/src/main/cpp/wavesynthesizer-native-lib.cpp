// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("wave");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("wave")
//      }
//    }


#include "jni.h" // from android ndk headers
#include "memory" // contains std::make_unique
#include "Log.h"
#include "WaveSynthesizer.h"



// Java_package_name_KotlinClassName_functionName
extern "C" {
JNIEXPORT jlong JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_create(
        JNIEnv *env, // jvm interface access
        jobject obj) {
    auto synthesizer = std::make_unique<wavesynthesizer::WaveSynthesizer>();

    if (not synthesizer) {
        LOGD("Failed to create the synthesizer");
        // resets pointer to nullptr
        synthesizer.reset(nullptr);
    }

    // casts address  of allocated object to long and returns it
    // synthesizer release the ownership (so that it is not destroyed after returning)
    return reinterpret_cast<jlong>(synthesizer.release());
}

JNIEXPORT void JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_delete(
        JNIEnv *env,
        jobject obj,
        jlong synthesizerHandle
) {
    auto *synthesizer =
            reinterpret_cast<wavesynthesizer::WaveSynthesizer *>(
                    synthesizerHandle
            );

    if (not synthesizer) {
        LOGD("Attempt to destroy an initialized synthesizer");
        return;
    }

    delete synthesizer;
}

JNIEXPORT void JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_play(
        JNIEnv *env,
        jobject obj,
        jlong synthesizerHandle) {
    auto *synthesizer = reinterpret_cast<wavesynthesizer::WaveSynthesizer *>(
            synthesizerHandle
    );

    if (synthesizer) {
        synthesizer->play();
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by calling create()"
        );
    }
}

JNIEXPORT void JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_stop(
        JNIEnv *env,
        jobject obj,
        jlong synthesizerHandle) {
    auto *synthesizer =
            reinterpret_cast<wavesynthesizer::WaveSynthesizer *>(
                    synthesizerHandle
            );

    if (synthesizer) {
        synthesizer->stop();
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by calling create()"
        );
    }
}

JNIEXPORT jboolean JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_isPlaying(
        JNIEnv *env,
        jobject obj,
        jlong synthesizerHandle) {
    auto *synthesizer =
            reinterpret_cast<wavesynthesizer::WaveSynthesizer *> (
                    synthesizerHandle
            );

    if (not synthesizer) {
        LOGD(
                "Synthesizer not created. Pleas, create the synthesizer first by calling create()"
        );
        return false;
    }

    return synthesizer->isPlaying();
}

JNIEXPORT void JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_setFrequency(
        JNIEnv *env,
        jobject obj,
        jlong sythesizerHandle,
        jfloat frequencyInHz) {
    auto *synthesizer =
            reinterpret_cast<wavesynthesizer::WaveSynthesizer *>(
                    sythesizerHandle
            );
    const auto nativeFrequency = static_cast<float>(frequencyInHz);

    if (synthesizer) {
        synthesizer->setFrequency(nativeFrequency);
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by calling create()"
        );
    }
}

JNIEXPORT void JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_setVolume(
        JNIEnv *env,
        jobject obj,
        jlong synthesizerHandle,
        jfloat volumeInDb) {
    auto *synthesizer =
            reinterpret_cast<wavesynthesizer::WaveSynthesizer *>(
                    synthesizerHandle);
    const auto nativeVolume = static_cast<float>(volumeInDb);

    if (synthesizer) {
        synthesizer->setVolume(nativeVolume);
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}

JNIEXPORT void JNICALL
Java_space_coderoman_wave_NativeWaveSynthesizer_setWave(
        JNIEnv *env,
        jobject obj,
        jlong synthesizerHandle,
        jint wave) {
    auto *synthesizer =
            reinterpret_cast<wavesynthesizer::WaveSynthesizer *>(
                    synthesizerHandle);
    const auto nativeWave = static_cast<wavesynthesizer::Wave>(wave);

    if (synthesizer) {
        synthesizer->setWave(nativeWave);
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}
} // extern "C"

