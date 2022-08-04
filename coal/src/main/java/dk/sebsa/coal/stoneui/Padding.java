package dk.sebsa.coal.stoneui;

/**
 * @author sebs
 */
public class Padding {
    public int top, bot, left, right;

    public Padding set(int i) {
        return set(i,i,i,i);
    }

    public Padding set(int top, int bot, int left, int right) {
        this.top = top;
        this.bot = bot;
        this.left = left;
        this.right = right;
        return this;
    }
}
