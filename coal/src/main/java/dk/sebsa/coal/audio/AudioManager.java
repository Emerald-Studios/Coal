package dk.sebsa.coal.audio;

import dk.sebsa.Coal;
import lombok.Getter;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author sebs
 */
public class AudioManager {
    private static void log(Object o) { if(Coal.TRACE) Coal.logger.log(0, "AudioManager"); }
    private static void trace(Object o) { if(Coal.TRACE) log(o); }
    private record SoundRequest(Sound sound, int gain) { }
    private static final List<AudioSource> sources = new ArrayList<>();
    private static final List<AudioSource> loanedSources = new ArrayList<>();
    private static final List<AudioSource> allSources = new ArrayList<>();
    private static final List<SoundRequest> toPlay = Collections.synchronizedList(new ArrayList<SoundRequest>());

    private static long device;
    @Getter private static ALCCapabilities deviceCaps;
    @Getter private static ALCapabilities capabilities;
    @Getter private static long context;

    public static void playSound(Sound sound, int gain) {
        toPlay.add(new SoundRequest(sound, gain));  // The sound are played on another thread
    }

    private static void expandList(int a) {
        log("Expanding AudioSource pool by " + a);
        for(int i = 0; i < a; i++) {
            AudioSource e = new AudioSource();
            sources.add(e);
            allSources.add(e);
        }
    }

    public static void init() {
        log("Init");
        trace("ALC Open Device");
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }

        trace("ALC Context & Capabilities");
        deviceCaps = ALC.createCapabilities(device);
        alcMakeContextCurrent(context);

        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        } alcMakeContextCurrent(context);
        trace("AL Capabilites");
        capabilities = AL.createCapabilities(deviceCaps);
        AL.setCurrentProcess(capabilities);
    }

    public static void update() {
        for(int i = 0; i < loanedSources.size(); i++) {
            if(!loanedSources.get(i).isPlaying()) {
                sources.add(loanedSources.get(i));
                loanedSources.remove(i);
                i--;
            }
        }

        if((-sources.size() + toPlay.size()) > 0) expandList((-sources.size() + toPlay.size()));
        for(SoundRequest request : toPlay) {
            AudioSource source = sources.get(0);
            sources.remove(0);
            loanedSources.add(source);

            source.setGain(request.gain);
            source.setBuffer(request.sound.getBufferID());
            source.play();
        }
        toPlay.clear();
    }

    public static void cleanup() {
        log("Cleanup");
        for(AudioSource s : allSources) {
            s.destroy();
        }
        trace("Destroy ALC");
        ALC.destroy();
    }

    public static int statusAll() { return allSources.size(); }
    public static int statusUsed() { return loanedSources.size(); }
}
