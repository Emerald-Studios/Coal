package dk.sebsa.coal.audio;

import static org.lwjgl.openal.AL10.*;

/**
 * @author sebs
 */
public class AudioSource {
    private final int sourceID;

    public AudioSource() {
        this.sourceID = alGenSources();
    }

    public void setGain(float gain) {
        alSourcef(sourceID, AL_GAIN, gain);
    }

    public void setProperty(int param, float value) {
        alSourcef(sourceID, param, value);
    }

    protected void setBuffer(int bufferId) {
        stop();
        alSourcei(sourceID, AL_BUFFER, bufferId);
    }

    public void setLoop(boolean loop) {
        alSourcei(sourceID, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public void play() {
        alSourcePlay(sourceID);
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(sourceID);
    }

    public void stop() {
        alSourceStop(sourceID);
    }

    public void destroy() {
        stop();
        alDeleteSources(sourceID);
    }
}
