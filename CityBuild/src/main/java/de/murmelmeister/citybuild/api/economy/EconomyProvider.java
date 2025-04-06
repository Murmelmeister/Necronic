package de.murmelmeister.citybuild.api.economy;

import de.murmelmeister.murmelapi.database.Database;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public final class EconomyProvider implements Economy {
    public static final Pattern MONEY_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    private static final String TABLE_NAME = "CB_Economy";
    private final Database database;

    public EconomyProvider(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "UserID INT PRIMARY KEY, Money DOUBLE, BankMoney DOUBLE");
    }

    @Override
    public boolean existUser(int userId) {
        return database.existsCallable(Procedure.ECONOMY_GET_USER.getName(), userId);
    }

    @Override
    public void createUser(int userId, double money, double bankMoney) {
        database.updateCallable(Procedure.ECONOMY_CREATE_USER.getName(), userId, money, bankMoney);
    }

    @Override
    public void deleteUser(int userId) {
        database.updateCallable(Procedure.ECONOMY_DELETE_USER.getName(), userId);
    }

    @Override
    public double getMoney(int userId) {
        return database.queryCallable(Procedure.ECONOMY_GET_USER.getName(), 0.0D, resultSet -> resultSet.getDouble("Money"), userId);
    }

    @Override
    public double getBankMoney(int userId) {
        return database.queryCallable(Procedure.ECONOMY_GET_USER.getName(), 0.0D, resultSet -> resultSet.getDouble("BankMoney"), userId);
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
        database.updateCallable(Procedure.ECONOMY_UPDATE_MONEY.getName(), userId, amount);
    }

    @Override
    public void setBankMoney(int userId, double amount) {
        database.updateCallable(Procedure.ECONOMY_UPDATE_BANK.getName(), userId, amount);
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
