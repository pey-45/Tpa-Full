package tpafull.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TpaBlockManager {
    private final static HashMap<String, Set<String>> tpaBlocks = new HashMap<>();
    private static final File DATA_FILE = new File("config/tpafull/tpa_blocks.json");
    private static final Gson GSON = new Gson();


    public static void saveData() {
        if (!DATA_FILE.getParentFile().exists() && !DATA_FILE.getParentFile().mkdirs()) {
            System.err.println("Could not create config folder " + DATA_FILE.getParentFile().getAbsolutePath() + " to save TpaBlock data");
            return;
        }

        try (Writer writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(tpaBlocks, writer);
        } catch (IOException e) {
            System.err.println("Failed to save TpaBlock data in file " + DATA_FILE.getAbsolutePath());
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
                tpaBlocks.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load TpaBlock data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static boolean block(ServerPlayerEntity blocker, String blocked) {
        Set<String> currentBlockerBlocks = tpaBlocks.computeIfAbsent(blocker.getName().getString(), k -> new HashSet<>());
        return currentBlockerBlocks.add(blocked);
    }

    public static boolean unblock(ServerPlayerEntity blocker, String blocked) {
        Set<String> currentBlockerBlocks = tpaBlocks.computeIfAbsent(blocker.getName().getString(), k -> new HashSet<>());
        return currentBlockerBlocks.remove(blocked);
    }

    public static Set<String> getBlocks(ServerPlayerEntity blocker) {
        Set<String> res = tpaBlocks.get(blocker.getName().getString());
        return res;
    }
}