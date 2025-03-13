package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.database.Database;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public final class Economy {
    public static final Pattern MONEY_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    private static final String TABLE_NAME = "CB_Economy";
    private final Database database;
    private final ConfigFile configFile;

    public Economy(Database database, ConfigFile configFile) {
        this.database = database;
        this.configFile = configFile;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "UserID INT PRIMARY KEY, Money DOUBLE, BankMoney DOUBLE");
    }

    public boolean existUser(int userId) {
        return database.exists(Procedure.ECONOMY_GET_USER.getName(), userId);
    }

    public void createUser(int userId) {
        if (existUser(userId)) return;
        database.callUpdate(Procedure.ECONOMY_CREATE_USER.getName(), userId, configFile.getDouble(Configs.ECONOMY_DEFAULT_MONEY), 0);
    }

    public void deleteUser(int userId) {
        database.callUpdate(Procedure.ECONOMY_DELETE_USER.getName(), userId);
    }

    public double getMoney(int userId) {
        return database.query(0.0D, "Money", double.class, Procedure.ECONOMY_GET_USER.getName(), userId);
    }

    public double getBankMoney(int userId) {
        return database.query(0.0D, "BankMoney", double.class, Procedure.ECONOMY_GET_USER.getName(), userId);
    }

    public String getFormattedMoney(int userId) {
        return new DecimalFormat(configFile.getString(Configs.PATTERN_DECIMAL)).format(getMoney(userId));
    }

    public String getFormattedBankMoney(int userId) {
        return new DecimalFormat(configFile.getString(Configs.PATTERN_DECIMAL)).format(getBankMoney(userId));
    }

    public void setMoney(int userId, double amount) {
        database.callUpdate(Procedure.ECONOMY_UPDATE_MONEY.getName(), userId, amount);
    }

    public void setBankMoney(int userId, double amount) {
        database.callUpdate(Procedure.ECONOMY_UPDATE_BANK.getName(), userId, amount);
    }

    public void addMoney(int userId, double amount) {
        double current = getMoney(userId);
        current += amount;
        setMoney(userId, current);
    }

    public void addBankMoney(int userId, double amount) {
        double current = getBankMoney(userId);
        current += amount;
        setBankMoney(userId, current);
    }

    public void removeMoney(int userId, double amount) {
        double current = getMoney(userId);
        current -= amount;
        setMoney(userId, current);
    }

    public void removeBankMoney(int userId, double amount) {
        double current = getBankMoney(userId);
        current -= amount;
        setBankMoney(userId, current);
    }

    public void resetMoney(int userId) {
        setMoney(userId, configFile.getDouble(Configs.ECONOMY_DEFAULT_MONEY));
    }

    public void resetBankMoney(int userId) {
        setBankMoney(userId, 0);
    }

    public boolean hasEnoughMoney(int userId, double money) {
        return money <= getMoney(userId);
    }

    public boolean hasEnoughBankMoney(int userId, double money) {
        return money <= getBankMoney(userId);
    }

    public void transferMoney(int userId, int targetId, double money) {
        removeMoney(userId, money);
        addMoney(targetId, money);
    }

    public boolean checkAndTransferMoney(int userId, int targetId, double money) {
        if (hasEnoughMoney(userId, money)) {
            transferMoney(userId, targetId, money);
            return true;
        } else return false;
    }

    public boolean transferMoneyToBank(int userId, double money) {
        if (hasEnoughMoney(userId, money)) {
            removeMoney(userId, money);
            addBankMoney(userId, money);
            return true;
        } else return false;
    }

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
        ECONOMY_GET_USER("CB_Economy_GetUser", "uid INT", "SELECT * FROM [TABLE] WHERE UserID=uid;");
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
