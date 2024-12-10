package tpafull.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

public class HomeManager {
    private final static HashMap<String, GlobalPos> homes = new HashMap<>();
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
            Type type = new TypeToken<HashMap<String, GlobalPos>>() {}.getType();
            HashMap<String, GlobalPos> loadedData = GSON.fromJson(reader, type);
            if (loadedData != null) {
                homes.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load home data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void setHome(ServerPlayerEntity player, GlobalPos pos) {
        homes.put(player.getName().getString(), pos);
    }

    public static boolean hasNoHome(ServerPlayerEntity player) {
        return !homes.containsKey(player.getName().getString());
    }

    public static ServerWorld getHomeWorld(ServerPlayerEntity player) {
        GlobalPos globalPos = homes.get(player.getName().getString());
        if (globalPos == null) {
            return null;
        }

        RegistryKey<World> dimensionKey = globalPos.dimension();

        return Objects.requireNonNull(player.getServer()).getWorld(dimensionKey);
    }

    public static double getX(ServerPlayerEntity player) {
        return homes.get(player.getName().getString()).pos().getX();
    }

    public static double getY(ServerPlayerEntity player) {
        return homes.get(player.getName().getString()).pos().getY();
    }

    public static double getZ(ServerPlayerEntity player) {
        return homes.get(player.getName().getString()).pos().getZ();
    }

    public static void removeHome(ServerPlayerEntity player) {
        homes.remove(player.getName().getString());
    }
}
