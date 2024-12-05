package tpafull.playerdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class AutoTpaManager {
    private final static HashMap<UUID, Set<UUID>> autoTpas = new HashMap<>();
    private static final File DATA_FILE = new File("config/tpafull/autotpas.json");
    private static final Gson GSON = new Gson();


    public static void saveData() {
        if (!DATA_FILE.getParentFile().exists() && !DATA_FILE.getParentFile().mkdirs()) {
            System.err.println("Could not create config folder " + DATA_FILE.getParentFile().getAbsolutePath() + " to save AutoTpa data");
            return;
        }

        try (Writer writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(autoTpas, writer);
        } catch (IOException e) {
            System.err.println("Failed to save AutoTpa data in file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void loadData() {
        if (!DATA_FILE.exists()) {
            return;
        }

        try (Reader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<HashMap<UUID, Set<UUID>>>() {}.getType();
            HashMap<UUID, Set<UUID>> loadedData = GSON.fromJson(reader, type);
            if (loadedData != null) {
                autoTpas.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load AutoTpa data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void setAutoTpa(ServerPlayerEntity sender, UUID target) {
        Set<UUID> currentPlayerAutoTpas = autoTpas.computeIfAbsent(sender.getUuid(), k -> new HashSet<>());
        currentPlayerAutoTpas.add(target);
    }

    public static Set<UUID> getAutoTpa(ServerPlayerEntity player) {
        return autoTpas.getOrDefault(player.getUuid(), Collections.emptySet());
    }

    public static void removeAutoTpa(ServerPlayerEntity player) {
        autoTpas.remove(player.getUuid());
    }
}