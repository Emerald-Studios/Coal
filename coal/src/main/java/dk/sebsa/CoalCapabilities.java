package dk.sebsa;

import lombok.Builder;

/**
 * @author sebs
 */
@Builder
public class CoalCapabilities {
    @Builder.Default public final boolean coalTrace = false;
    @Builder.Default public final boolean coalDebug = true;
    @Builder.Default public final boolean coalTC = true;
    @Builder.Default public final boolean coalLoadScreen = true;

    @Builder.Default public final boolean coalAudio = false;
    @Builder.Default public final boolean coalSprite2D = false;
    @Builder.Default public final boolean coalPhysics2D = false;
    @Builder.Default public final boolean ignoreErrorShutdown = false;
    @Builder.Default public final boolean disableContinuousLogFlush = false;

    @Builder.Default public final boolean tcAudioClean = true;
    @Builder.Default public final boolean tcFBOClean = true;
    @Builder.Default @Deprecated public final boolean tcColorPrune = false;
    // THREAD
    // MULTIPLE APPS
    // ASSET
}
