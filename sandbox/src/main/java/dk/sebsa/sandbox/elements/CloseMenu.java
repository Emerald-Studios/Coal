package dk.sebsa.sandbox.elements;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.stoneui.CustomElement;
import dk.sebsa.coal.stoneui.State;

/**
 * @author sebs
 */
public class CloseMenu extends CustomElement {
    private final Application app;

    public CloseMenu(State state, Application app) {
        super(state);
        this.app = app;

        if(!state.exists("1")) state.set("1", false);
    }

    @Override
    protected void build() {
        List(() -> {
            Button(Text("Close"), (b) -> { state.set("1", !(Boolean) state.get("1")); dirty(); })
                    .size(90, 20)
                    .padding(5).centerText();

            if((Boolean) state.get("1")) Button(Text("Sure?"), (b) -> app.forceClose())
                    .size(90,20)
                    .padding(5);

            return null;
        });
    }
}
