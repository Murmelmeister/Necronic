package de.murmelmeister.citybuild.api.economy;

import de.murmelmeister.murmelapi.database.Database;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public final class EconomyProviderImpl implements EconomyProvider {
    public static final Pattern MONEY_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    private static final String TABLE_NAME = "CB_Economy";
    private final Database database;

    private final Map<Integer, Economy> cache = new ConcurrentHashMap<>();

    public EconomyProviderImpl(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
        loadData();
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "UserID INT PRIMARY KEY, Money DOUBLE, BankMoney DOUBLE");
    }

    private void loadData() {
        cache.clear();
        database.queryProcess(resultSet -> {
            int userId = resultSet.getInt("UserID");
            double money = resultSet.getDouble("Money");
            double bankMoney = resultSet.getDouble("BankMoney");
            cache.put(userId, new EconomyImpl(userId, money, bankMoney));
        }, Procedure.ECONOMY_GET_ALL.getName());
    }

    @Override
    public Economy getEconomy(int userId) {
        return cache.get(userId);
    }

    @Override
    public boolean existUser(int userId) {
        return cache.containsKey(userId);
    }

    @Override
    public void addUser(Economy economy) {
        cache.put(economy.getUserId(), economy);
        database.asyncUpdate(Procedure.ECONOMY_CREATE_USER.getName(), economy.getUserId(), economy.getMoney(), economy.getBankMoney());
    }

    @Override
    public void removeUser(int userId) {
        cache.remove(userId);
        database.asyncUpdate(Procedure.ECONOMY_DELETE_USER.getName(), userId);
    }

    @Override
    public void updateUser(Economy economy) {
        cache.put(economy.getUserId(), economy);
        database.asyncUpdate(Procedure.ECONOMY_UPDATE_ALL.getName(), economy.getUserId(), economy.getMoney(), economy.getBankMoney());
    }

    /*public boolean existUser(int userId) {
        return database.exists(Procedure.ECONOMY_GET_USER.getName(), userId);
    }

    public void createUser(int userId, double money) {
        if (existUser(userId)) return;
        database.callUpdate(Procedure.ECONOMY_CREATE_USER.getName(), userId, money, 0);
    }

    public void deleteUser(int userId) {
        database.callUpdate(Procedure.ECONOMY_DELETE_USER.getName(), userId);
    }*/

    @Override
    public double getMoney(int userId) {
        return getEconomy(userId).getMoney();
    }

    @Override
    public double getBankMoney(int userId) {
        return getEconomy(userId).getBankMoney();
    }

    @Override
    public String getFormattedMoney(int userId, String pattern) {
        return new DecimalFormat(pattern).format(getMoney(userId));
    }

    @Override
    public String getFormattedBankMoney(int userId, String pattern) {
        return new DecimalFormat(pattern).format(getBankMoney(userId));
    }

    @Override
    public void setMoney(int userId, double amount) {
        getEconomy(userId).setMoney(amount);
    }

    @Override
    public void setBankMoney(int userId, double amount) {
        getEconomy(userId).setBankMoney(amount);
    }

    @Override
    public void addMoney(int userId, double amount) {
        double current = getMoney(userId);
        current += amount;
        setMoney(userId, current);
    }

    @Override
    public void addBankMoney(int userId, double amount) {
        double current = getBankMoney(userId);
        current += amount;
        setBankMoney(userId, current);
    }

    @Override
    public void removeMoney(int userId, double amount) {
        double current = getMoney(userId);
        current -= amount;
        setMoney(userId, current);
    }

    @Override
    public void removeBankMoney(int userId, double amount) {
        double current = getBankMoney(userId);
        current -= amount;
        setBankMoney(userId, current);
    }

    @Override
    public void resetMoney(int userId, double money) {
        setMoney(userId, money);
    }

    @Override
    public void resetBankMoney(int userId, double money) {
        setBankMoney(userId, money);
    }

    @Override
    public boolean hasEnoughMoney(int userId, double money) {
        return money <= getMoney(userId);
    }

    @Override
    public boolean hasEnoughBankMoney(int userId, double money) {
        return money <= getBankMoney(userId);
    }

    @Override
    public void transferMoney(int userId, int targetId, double money) {
        removeMoney(userId, money);
        addMoney(targetId, money);
    }

    @Override
    public boolean checkAndTransferMoney(int userId, int targetId, double money) {
        if (hasEnoughMoney(userId, money)) {
            transferMoney(userId, targetId, money);
            return true;
        } else return false;
    }

    @Override
    public boolean transferMoneyToBank(int userId, double money) {
        if (hasEnoughMoney(userId, money)) {
            removeMoney(userId, money);
            addBankMoney(userId, money);
            return true;
        } else return false;
    }

    @Override
    public boolean transferBankMoneyToPlayer(int userId, double money) {
        if (hasEnoughBankMoney(userId, money)) {
            removeBankMoney(userId, money);
            addMoney(userId, money);
            return true;
        } else return false;
    }

    private enum Procedure {
        ECONOMY_CREATE_USER("CB_Economy_CreateUser", "uid INT, current DOUBLE, bank DOUBLE", "INSERT INTO [TABLE] VALUES (uid, current, bank);"),
        ECONOMY_DELETE_USER("CB_Economy_DeleteUser", "uid INT", "DELETE FROM [TABLE] WHERE UserID=uid;"),
        ECONOMY_UPDATE_MONEY("CB_Economy_Update_Money", "uid INT, current DOUBLE", "UPDATE [TABLE] SET Money=current WHERE UserID=uid;"),
        ECONOMY_UPDATE_BANK("CB_Economy_Update_BankMoney", "uid INT, bank DOUBLE", "UPDATE [TABLE] SET BankMoney=bank WHERE UserID=uid;"),
        ECONOMY_GET_USER("CB_Economy_GetUser", "uid INT", "SELECT * FROM [TABLE] WHERE UserID=uid;"),
        ECONOMY_GET_ALL("CB_Economy_GetAll", "", "SELECT * FROM [TABLE];"),
        ECONOMY_UPDATE_ALL("CB_Economy_UpdateAll", "uid INT, current DOUBLE, bank DOUBLE", "UPDATE [TABLE] SET Money=current, BankMoney=bank WHERE UserID=uid;");
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
