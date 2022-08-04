package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.stoneui.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 */
public class Group extends Element<Group> {
    public List<Element> elements = new ArrayList<>();
    public void addToGroup(Element e) {
        elements.add(e);
    }

    @Override
    public boolean handleEvent(Event e) {
        for(Element element : elements) {
            if(element.handleEvent(e)) return true;
        } return false;
    }

    @Override
    protected void render() {
        Rect offset = new Rect();
        for(int j = 0; j < elements.size(); j++) {
            elements.get(j).draw(offset);
        }
    }
}
