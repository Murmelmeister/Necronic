package de.murmelmeister.citybuild.api.economy;

public sealed interface EconomyProvider permits EconomyProviderImpl {
    Economy getEconomy(int userId);

    boolean existUser(int userId);

    void addUser(Economy economy);

    void removeUser(int userId);

    void updateUser(Economy economy);

    double getMoney(int userId);

    double getBankMoney(int userId);

    String getFormattedMoney(int userId, String pattern);

    String getFormattedBankMoney(int userId, String pattern);

    void setMoney(int userId, double amount);

    void setBankMoney(int userId, double amount);

    void addMoney(int userId, double amount);

    void addBankMoney(int userId, double amount);

    void removeMoney(int userId, double amount);

    void removeBankMoney(int userId, double amount);

    void resetMoney(int userId, double money);

    void resetBankMoney(int userId, double money);

    boolean hasEnoughMoney(int userId, double money);

    boolean hasEnoughBankMoney(int userId, double money);

    void transferMoney(int userId, int targetId, double money);

    boolean checkAndTransferMoney(int userId, int targetId, double money);

    boolean transferMoneyToBank(int userId, double money);

    boolean transferBankMoneyToPlayer(int userId, double money);
}
