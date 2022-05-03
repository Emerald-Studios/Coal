package dk.sebsa.coal.audio;

import dk.sebsa.coal.math.Vector3f;

import static org.lwjgl.openal.AL10.*;

/**
 * @author sebs
 */
public class AudioListener {
    public AudioListener() {
        this(new Vector3f(0, 0, 0));
    }

    public AudioListener(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0, 0, 0);

    }

    public void setPosition(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }
}