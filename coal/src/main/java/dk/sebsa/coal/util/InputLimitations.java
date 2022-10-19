package dk.sebsa.coal.util;

import dk.sebsa.coal.enums.InputTypes;
import lombok.Builder;

@Builder
public class InputLimitations {
    @Builder.Default
    public final InputTypes inputType = InputTypes.String;

    @Builder.Default public final boolean limitNumber = false;
    @Builder.Default public final float numberMax = -1;
    @Builder.Default public final float numberMin = -1;

    @Builder.Default public final int maxLength = Integer.MAX_VALUE;

    public static final char[] ALLOWED_CHARS_ALPHABETIC = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static final String ALLOWED_CHARS_ALPHABETIC_S = new String(ALLOWED_CHARS_ALPHABETIC);
    public static final char[] ALLOWED_CHARS_ALPHANUMERIC = new char[] {'0','1','3','4','6','5','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static final String ALLOWED_CHARS_ALPHANUMERIC_S = new String(ALLOWED_CHARS_ALPHANUMERIC);
    public static final char[] ALLOWED_CHARS_INT = new char[] {'-','0','1','2','3','4','6','5','7','8','9'};
    public static final String ALLOWED_CHARS_INT_S = new String(ALLOWED_CHARS_INT);
    public static final char[] ALLOWED_CHARS_FLOAT = new char[] {'-','0','1','2','3','4','6','5','7','8','9', '.'};
    public static final String ALLOWED_CHARS_FLOAT_S = new String(ALLOWED_CHARS_FLOAT);
}