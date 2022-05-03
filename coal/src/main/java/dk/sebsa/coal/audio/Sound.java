package dk.sebsa.coal.audio;

import dk.sebsa.Coal;
import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import lombok.Getter;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author sebs
 */
public class Sound extends Asset {
    @Getter private int bufferID;
    private ShortBuffer pcm = null;
    private ByteBuffer vorbis = null;

    public Sound(AssetLocation location) {
        super(location);
    }

    @Override
    public void destroy() {
        if(!Coal.getCapabilities().coalAudio) return;
        alDeleteBuffers(bufferID);
    }
    private void trace(Object o) { if(Coal.TRACE) log(o); }

    @Override
    protected void load() {
        if(!Coal.getCapabilities().coalAudio) return;

        this.bufferID = alGenBuffers();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(32 * 1024, info);

            // Copy to buffer
            alBufferData(bufferID, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        }
    }

    private ShortBuffer readVorbis(int bufferSize, STBVorbisInfo info) {
        trace("ReadVorbis");
        try (MemoryStack stack = MemoryStack.stackPush()) {
            vorbis = location.asBuffer(bufferSize);
            IntBuffer error = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }
            trace("Decoder Not NULL");

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            pcm = MemoryUtil.memAllocShort(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }
}
