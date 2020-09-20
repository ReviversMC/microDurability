package dzwdz.microdurability;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class EntryPoint implements ModInitializer {
    @Override
    public void onInitialize() {
        HudRenderCallback.EVENT.register(new Renderer());
    }
}
