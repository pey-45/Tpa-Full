package tpafull.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TpaAutoManager {
    private final static HashMap<String, Set<String>> allowedAutoTpa = new HashMap<>();
    private static final File DATA_FILE = new File("config/tpafull/auto_tpa_allowed.json");
    private static final Gson GSON = new Gson();


    public static void saveData() {
        if (!DATA_FILE.getParentFile().exists() && !DATA_FILE.getParentFile().mkdirs()) {
            System.err.println("Could not create config folder " + DATA_FILE.getParentFile().getAbsolutePath() + " to save AutoTpa data");
            return;
        }

        try (Writer writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(allowedAutoTpa, writer);
        } catch (IOException e) {
            System.err.println("Failed to save AutoTpa data in file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void loadData() {
        if (!DATA_FILE.exists()) {
            return;
        }

        try (Reader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<HashMap<String, Set<String>>>() {}.getType();
            HashMap<String, Set<String>> loadedData = GSON.fromJson(reader, type);
            if (loadedData != null) {
                allowedAutoTpa.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load AutoTpa data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static boolean add(ServerPlayerEntity sender, String target) {
        Set<String> currentPlayerAutoTpas = allowedAutoTpa.computeIfAbsent(sender.getName().getString(), k -> new HashSet<>());
        return currentPlayerAutoTpas.add(target);
    }

    public static boolean remove(ServerPlayerEntity sender, String target) {
        Set<String> currentPlayerAutoTpas = allowedAutoTpa.computeIfAbsent(sender.getName().getString(), k -> new HashSet<>());
        return currentPlayerAutoTpas.remove(target);
    }

    public static Set<String> getAllowed(ServerPlayerEntity blocker) {
        return allowedAutoTpa.get(blocker.getName().getString());
    }
}