package tpapey.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;


public class DefaultTpaManager {
    private static final HashMap<UUID, UUID> playersDefaultTpa = new HashMap<>();
    private static final File DATA_FILE = new File("config/tpa_defaults.json");
    private static final Gson GSON = new Gson();

    public static void saveData() {
        if (!DATA_FILE.getParentFile().exists() && !DATA_FILE.getParentFile().mkdirs()) {
            System.err.println("Could not create config folder " + DATA_FILE.getParentFile().getAbsolutePath() + " to save default tpa data");
            return;
        }


        try (Writer writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(playersDefaultTpa, writer);
        } catch (IOException e) {
            System.err.println("Failed to save default tpa data in file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void loadData() {
        if (!DATA_FILE.exists()) {
            return;
        }

        try (Reader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<HashMap<UUID, UUID>>() {}.getType();
            HashMap<UUID, UUID> loadedData = GSON.fromJson(reader, type);
            if (loadedData != null) {
                playersDefaultTpa.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load default tpa data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void setDefaultTpa(ServerPlayerEntity player, ServerPlayerEntity defaultTpa) {
        playersDefaultTpa.put(player.getUuid(), defaultTpa.getUuid());
    }

    public static ServerPlayerEntity getDefaultTpa(ServerPlayerEntity player) {
        return player.getServerWorld().getServer().getPlayerManager().getPlayer(playersDefaultTpa.getOrDefault(player.getUuid(), null));
    }

    public static void removeDefaultTpa(ServerPlayerEntity player) {
        playersDefaultTpa.remove(player.getUuid());
    }
}
