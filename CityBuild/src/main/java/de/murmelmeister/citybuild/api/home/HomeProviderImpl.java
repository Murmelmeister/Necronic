package de.murmelmeister.citybuild.api.home;

import de.murmelmeister.murmelapi.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HomeProviderImpl implements HomeProvider {
    private static final String TABLE_NAME = "CB_Home";
    private final Database database;

    private final Map<HomeKeys, Home> cache = new ConcurrentHashMap<>();

    public HomeProviderImpl(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
        loadData();
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "UserID INT, HomeName VARCHAR(100), PRIMARY KEY (UserID, HomeName), " +
                                         "WorldName VARCHAR(100), WorldType VARCHAR(100), " +
                                         "X DOUBLE, Y DOUBLE, Z DOUBLE, Yaw DOUBLE, Pitch DOUBLE");
    }

    private void loadData() {
        cache.clear();
        database.queryProcess(resultSet -> {
            int userId = resultSet.getInt("UserID");
            String homeName = resultSet.getString("HomeName");
            String worldName = resultSet.getString("WorldName");
            String worldType = resultSet.getString("WorldType");
            double x = resultSet.getDouble("X");
            double y = resultSet.getDouble("Y");
            double z = resultSet.getDouble("Z");
            double yaw = resultSet.getDouble("Yaw");
            double pitch = resultSet.getDouble("Pitch");

            HomeKeys keys = new HomeKeys(userId, homeName);
            Home home = new HomeImpl(keys, worldName, worldType, x, y, z, yaw, pitch);
            cache.put(keys, home);
        }, Procedure.HOME_ALL.getName());
    }

    @Override
    public Home getHome(int userId, String homeName) {
        return cache.get(new HomeKeys(userId, homeName));
    }

    @Override
    public boolean existsHome(int userId, String homeName) {
        return cache.containsKey(new HomeKeys(userId, homeName));
    }

    @Override
    public void addHome(Home home) {
        cache.put(home.getKeys(), home);
        database.asyncUpdate(Procedure.HOME_ADD.getName(), home.getKeys().userId(), home.getKeys().homeName(),
                home.getWorldName(), home.getWorldType(), home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());
    }

    @Override
    public void removeHome(Home home) {
        cache.remove(home.getKeys());
        database.asyncUpdate(Procedure.HOME_REMOVE.getName(), home.getKeys().userId(), home.getKeys().homeName());
    }

    @Override
    public List<String> getHomeNames(int userId) {
        List<String> homeNames = new ArrayList<>();
        for (Home home : cache.values())
            if (home.getKeys().userId() == userId)
                homeNames.add(home.getKeys().homeName());
        return homeNames;
    }

    /*public boolean hasHome(int userId, String homeName) {
        return database.exists(Procedure.HOME_GET.getName(), userId, homeName);
    }

    public void addHome(int userId, String homeName, Location location) {
        if (homeName.length() > 100)
            throw new IllegalArgumentException("Home name cannot be longer than 100 characters");
        if (hasHome(userId, homeName)) return;
        String worldName = location.getWorld().getName();
        String environment = location.getWorld().getEnvironment().name();
        double x = location.getBlockX() + 0.5D;
        double y = location.getBlockY() + 0.25D;
        double z = location.getBlockZ() + 0.5D;
        double yaw = Math.round(location.getYaw() / 45.0F) * 45;
        double pitch = Math.round(location.getPitch() / 45.0F) * 45;
        database.callUpdate(Procedure.HOME_ADD.getName(), userId, homeName, worldName, environment, x, y, z, yaw, pitch);
    }

    public void removeHome(int userId, String homeName) {
        database.callUpdate(Procedure.HOME_REMOVE.getName(), userId, homeName);
    }

    public Location getHome(Server server, int userId, String homeName) {
        Map<String, Object> homeData = database.queryMap(new HashMap<>(), Object.class, Procedure.HOME_GET.getName(), userId, homeName);
        if (homeData == null || homeData.isEmpty()) return null;
        String worldName = (String) homeData.get("WorldName");
        double x = (double) homeData.get("X");
        double y = (double) homeData.get("Y");
        double z = (double) homeData.get("Z");
        double yaw = (double) homeData.get("Yaw");
        double pitch = (double) homeData.get("Pitch");

        World world = server.getWorld(worldName);
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public List<String> getHomes(int userId) {
        return database.queryList(new ArrayList<>(), "HomeName", String.class, Procedure.HOME_GET_ALL.getName(), userId);
    }*/

    private enum Procedure {
        HOME_ADD("Home_Add", "uid INT, hname VARCHAR(100), wname VARCHAR(100), wtype VARCHAR(100), wx DOUBLE, wy DOUBLE, wz DOUBLE, wyaw DOUBLE, wp DOUBLE",
                "INSERT INTO [TABLE] VALUES (uid, hname, wname, wtype, wx, wy, wz, wyaw, wp);"),
        HOME_REMOVE("Home_Remove", "uid INT, hname VARCHAR(100)", "DELETE FROM [TABLE] WHERE UserID=uid AND HomeName=hname;"),
        HOME_GET("Home_Get", "uid INT, hname VARCHAR(100)", "SELECT * FROM [TABLE] WHERE UserID=uid AND HomeName=hname;"),
        HOME_GET_ALL("Home_GetAll", "uid INT", "SELECT * FROM [TABLE] WHERE UserID=uid;"),
        HOME_ALL("Home_All", "", "SELECT * FROM [TABLE];");
        private static final Procedure[] VALUES = values();

        private final String name;
        private final String query;

        Procedure(final String name, final String input, final String query) {
            this.name = name;
            this.query = Database.getProcedureQuery(name, input, query);
        }

        public String getName() {
            return name;
        }

        public String getQuery() {
            return query.replace("[TABLE]", TABLE_NAME);
        }

        public static void loadAll(Database database) {
            for (Procedure procedure : VALUES) database.update(procedure.getQuery());
        }
    }
}
