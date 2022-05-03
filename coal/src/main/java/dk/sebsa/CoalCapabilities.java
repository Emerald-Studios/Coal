package dk.sebsa;

import lombok.Builder;

/**
 * @author sebs
 */
@Builder
public class CoalCapabilities {
    @Builder.Default public final boolean coalDebug = true;
    @Builder.Default public final boolean coalTrace = false;
    @Builder.Default public final boolean coalAudio = false;
    @Builder.Default public final boolean loadScreen = true;
    @Builder.Default public final boolean ignoreErrorShutdown = false;
    @Builder.Default public final boolean disableContinuousLogFlush = false;
    // THREAD
    // MULTIPLE APPS
    // ASSET
}
