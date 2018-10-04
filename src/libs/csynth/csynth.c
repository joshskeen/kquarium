#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <portaudio.h>

#define SAMPLE_RATE   (44100)
#define FRAMES_PER_BUFFER  (64)

#ifndef M_PI
#define M_PI  (3.14159265)
#endif

typedef struct {
    int size;
    int left_phase;
    int right_phase;
    char message[20];
    float *sine;
}
        synthData;


static int audioCallback(const void *inputBuffer, void *outputBuffer,
                         unsigned long framesPerBuffer,
                         const PaStreamCallbackTimeInfo *timeInfo,
                         PaStreamCallbackFlags statusFlags,
                         void *userData) {
    synthData *data = (synthData *) userData;
    float *out = (float *) outputBuffer;
    unsigned long i;

    (void) timeInfo;
    (void) statusFlags;
    (void) inputBuffer;
    for (i = 0; i < framesPerBuffer; i++) {
        *out++ = data->sine[data->left_phase];
        *out++ = data->sine[data->right_phase];
        data->left_phase += 1;
        if (data->left_phase >= data->size) data->left_phase -= data->size;
        data->right_phase += 3;
        if (data->right_phase >= data->size) data->right_phase -= data->size;
    }
    return paContinue;
}

static void StreamFinished(void *userData) {
    synthData *data = (synthData *) userData;
}

PaError play(int tableSize, long millis) {
    PaStreamParameters outputParameters;
    PaStream *stream;
    PaError err;
    synthData data;
    int i;
    data.size = tableSize;
    data.sine = malloc(sizeof(float) * tableSize);

    for (i = 0; i < tableSize; i++) {
        float d = (float) sin(((double) i / (double) tableSize) * M_PI * 2.);
        data.sine[i] = d;
    }

    data.left_phase = data.right_phase = 0;
    err = Pa_Initialize();
    if (err != paNoError) goto error;

    outputParameters.device = Pa_GetDefaultOutputDevice();
    if (outputParameters.device == paNoDevice) {
        goto error;
    }
    outputParameters.channelCount = 2;
    outputParameters.sampleFormat = paFloat32;
    outputParameters.suggestedLatency = Pa_GetDeviceInfo(outputParameters.device)->defaultLowOutputLatency;
    outputParameters.hostApiSpecificStreamInfo = NULL;

    err = Pa_OpenStream(
            &stream,
            NULL,
            &outputParameters,
            SAMPLE_RATE,
            FRAMES_PER_BUFFER,
            paClipOff,
            audioCallback,
            &data);

    free(data.sine);

    if (err != paNoError) goto error;

    err = Pa_SetStreamFinishedCallback(stream, &StreamFinished);
    if (err != paNoError) goto error;

    err = Pa_StartStream(stream);

    if (err != paNoError) goto error;
    Pa_Sleep(millis);

    err = Pa_StopStream(stream);
    if (err != paNoError) goto error;

    err = Pa_CloseStream(stream);
    if (err != paNoError) goto error;

    Pa_Terminate();
    return err;
    error:
    Pa_Terminate();

    return err;
}