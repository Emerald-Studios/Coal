package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.stoneui.Element;
import dk.sebsa.coal.stoneui.Padding;

/**
 * @author sebs
 */
public class List extends Group {
    @Override
    protected void render() {
        Rect offset = new Rect();
        for (Element e : elements) {
            Padding p = e.getPadding();

            offset.addPosition(p.left, 0);

            offset.addPosition(0, p.top);
            e.draw(offset);
            offset.addPosition(0, e.getHeight());
            offset.addPosition(0, p.bot);

            offset.addPosition(-p.left, 0);
        }
    }
}
