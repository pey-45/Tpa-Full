package tpafull.managers;

public class PlayerManager {
    /*private final static HashMap<UUID, String> players = new HashMap<>();
    private static final File DATA_FILE = new File("config/tpafull/players.json");
    private static final Gson GSON = new Gson();

    public static void saveData() {
        if (!DATA_FILE.getParentFile().exists() && !DATA_FILE.getParentFile().mkdirs()) {
            System.err.println("Could not create config folder " + DATA_FILE.getParentFile().getAbsolutePath() + " to save player data");
            return;
        }

        try (Writer writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(players, writer);
        } catch (IOException e) {
            System.err.println("Failed to save player data in file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void loadData() {
        if (!DATA_FILE.exists()) {
            return;
        }

        try (Reader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<HashMap<UUID, String>>() {}.getType();
            HashMap<UUID, String> loadedData = GSON.fromJson(reader, type);
            if (loadedData != null) {
                players.putAll(loadedData);
            }
        } catch (IOException e) {
            System.err.println("Failed to load player data from file " + DATA_FILE.getAbsolutePath());
        }
    }

    public static void setName(ServerPlayerEntity player, String name) {
        players.put(player.getUuid(), name);
    }

    public static String getName(ServerPlayerEntity player) {
        return players.getOrDefault(player.getUuid(), null);
    }*/
}

