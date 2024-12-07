package tpafull.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class HomeManager {
    private final static HashMap<UUID, BlockPos> homes = new HashMap<>();
    private static final File DATA_FILE = new File("config/tpafull/homes.json");
    private static final Gson GSON = new Gson();


    public static void saveData() {
        if (!DATA_FILE.getParentFile().exists() && !DATA_FILE.getParentFile().mkdirs()) {
            System.err.println("Could not create config folder " + DATA_FILE.getParentFile().getAbsolutePath() + " to save home data");
            return;
        }

        try (Writer writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(homes, writer);
        } catch (IOException e) {
            System.err.println("Failed to save home data in file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void loadData() {
        if (!DATA_FILE.exists()) {
            return;
        }

        try (Reader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<HashMap<UUID, BlockPos>>() {}.getType();
            HashMap<UUID, BlockPos> loadedData = GSON.fromJson(reader, type);
            if (loadedData != null) {
                homes.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load home data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void setHome(ServerPlayerEntity player, BlockPos pos) {
        homes.put(player.getUuid(), pos);
    }

    public static BlockPos getHome(ServerPlayerEntity player) {
        return homes.getOrDefault(player.getUuid(), null);
    }

    public static void removeHome(ServerPlayerEntity player) {
        homes.remove(player.getUuid());
    }
}
