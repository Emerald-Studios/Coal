package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.stoneui.Element;
import dk.sebsa.coal.stoneui.Padding;

/**
 * @author sebs
 */
public class HList extends Group {
    @Override
    protected void render() {
        Rect offset = new Rect();
        for (Element e : elements) {
            Padding p = e.getPadding();

            offset.addPosition(0, p.top);

            offset.addPosition(p.left, 0);
            e.draw(offset);
            offset.addPosition(e.getWidth(), 0);
            offset.addPosition(p.right, 0);

            offset.addPosition(0, -p.top);
        }
    }
}
