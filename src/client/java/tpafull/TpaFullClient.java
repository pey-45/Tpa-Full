package tpafull;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class TpaFullClient implements ClientModInitializer {
    private static KeyBinding tpaKeyBinding;

    @Override
    public void onInitializeClient() {
        tpaKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tpafull.tpa",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.tpafull"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpaKeyBinding.wasPressed()) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("tpa");
                }
            }
        });
    }
}

