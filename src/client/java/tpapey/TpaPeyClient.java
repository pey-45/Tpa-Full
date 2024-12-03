package tpapey;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


public class TpaPeyClient implements ClientModInitializer {
    private static KeyBinding tpaKeyBinding;

    @Override
    public void onInitializeClient() {
        tpaKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tpapey.tpa",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.tpapey"
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

