package de.murmelmeister.citybuild.api.economy;

public sealed interface Economy permits EconomyProvider {
    /**
     * Checks whether a user with the specified user ID exists in the database.
     *
     * @param userId The unique ID of the user to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    boolean existUser(int userId);

    /**
     * Creates a new user with specified initial monetary values.
     *
     * @param userId     The unique identifier for the user to be created.
     * @param money      The initial amount of money to assign to the user.
     * @param bankMoney  The initial amount of bank money to assign to the user.
     */
    void createUser(int userId, double money, double bankMoney);

    /**
     * Deletes a user from the economy system identified by their user ID.
     *
     * @param userId The unique identifier of the user to be deleted.
     */
    void deleteUser(int userId);

    /**
     * Retrieves the current money balance of a user identified by the given userId.
     *
     * @param userId The unique identifier of the user whose money balance is being retrieved.
     * @return The amount of money currently available to the user. If the user does not exist, returns 0.0.
     */
    double getMoney(int userId);

    /**
     * Retrieves the amount of money stored in the bank for a specific user.
     *
     * @param userId the unique identifier of the user whose bank money is to be retrieved
     * @return the amount of money the user has in the bank as a double
     */
    double getBankMoney(int userId);

    /**
     * Formats the money amount of a specific user according to the provided pattern.
     *
     * @param userId the ID of the user whose money amount is to be formatted
     * @param pattern the formatting pattern to apply to the money amount
     * @return the formatted money amount as a string
     */
    String getFormattedMoney(int userId, String pattern);

    /**
     * Formats the bank money of a user based on the given pattern.
     *
     * @param userId  The unique identifier of the user whose bank money is being formatted.
     * @param pattern The pattern to format the bank money, following {@link java.text.DecimalFormat} rules.
     * @return A string representation of the user's bank money formatted according to the specified pattern.
     */
    String getFormattedBankMoney(int userId, String pattern);

    /**
     * Sets the amount of money a user has. This will overwrite the user's current balance.
     *
     * @param userId The unique identifier for the user whose money is being updated.
     * @param amount The new amount of money to set for the user.
     */
    void setMoney(int userId, double amount);

    /**
     * Sets the specified amount of bank money for a user.
     *
     * @param userId The unique identifier of the user whose bank money will be updated.
     * @param amount The new amount of bank money to set for the user.
     */
    void setBankMoney(int userId, double amount);

    /**
     * Adds a specified amount to the user's current balance.
     *
     * @param userId The unique identifier of the user whose balance will be updated.
     * @param amount The amount of money to be added to the user's balance. Should be positive.
     */
    void addMoney(int userId, double amount);

    /**
     * Adds a specified amount of money to the bank account of a user.
     *
     * @param userId The unique identifier of the user whose bank account is to be updated.
     * @param amount The amount of money to add to the user's bank account. Must be a positive value.
     */
    void addBankMoney(int userId, double amount);

    /**
     * Decreases the in-hand money of the specified user by the given amount.
     * If the user's current in-hand money is less than the specified amount, the operation will result in a negative value.
     *
     * @param userId The unique identifier of the user whose in-hand money will be reduced.
     * @param amount The amount to be deducted from the user's in-hand money.
     */
    void removeMoney(int userId, double amount);

    /**
     * Deducts a specified amount of money from the user's bank account.
     *
     * @param userId The unique identifier of the user whose bank money is to be deducted.
     * @param amount The amount of money to be removed from the user's bank account.
     */
    void removeBankMoney(int userId, double amount);

    /**
     * Resets the money balance of the specified user to a given amount.
     *
     * @param userId The unique identifier of the user whose money balance is being reset.
     * @param money The new amount of money to set as the user's balance.
     */
    void resetMoney(int userId, double money);

    /**
     * Resets the bank money of a specific user to the specified amount.
     *
     * @param userId The ID of the user whose bank money is being reset.
     * @param money The new amount of money to set as the user's bank money.
     */
    void resetBankMoney(int userId, double money);

    /**
     * Checks if a user has enough money to cover the specified amount.
     *
     * @param userId The unique identifier of the user whose balance is to be checked.
     * @param money  The amount of money to check against the user's balance.
     * @return {@code true} if the user has enough money, {@code false} otherwise.
     */
    boolean hasEnoughMoney(int userId, double money);

    /**
     * Checks if the user has at least the specified amount of money in their bank account.
     *
     * @param userId The unique identifier of the user whose bank money is to be checked.
     * @param money The amount of money to verify against the user's bank balance.
     * @return true if the user's bank account has enough money, false otherwise.
     */
    boolean hasEnoughBankMoney(int userId, double money);

    /**
     * Transfers a specified amount of money from one user to another.
     *
     * @param userId   The ID of the user sending the money.
     * @param targetId The ID of the user receiving the money.
     * @param money    The amount of money to be transferred.
     */
    void transferMoney(int userId, int targetId, double money);

    /**
     * Checks if the user has enough money to transfer the specified amount to
     * another user and performs the transfer if possible.
     *
     * @param userId   The ID of the user who is transferring the money.
     * @param targetId The ID of the target user who will receive the money.
     * @param money    The amount of money to be transferred.
     * @return true if the money was successfully transferred, false if the user
     *         does not have enough money for the transfer.
     */
    boolean checkAndTransferMoney(int userId, int targetId, double money);

    /**
     * Transfers the specified amount of money from the user's account balance to their bank account.
     *
     * @param userId The unique identifier of the user performing the transfer.
     * @param money The amount of money to transfer from the user's account to the bank.
     * @return {@code true} if the transfer was successful; {@code false} if the user does not have enough money.
     */
    boolean transferMoneyToBank(int userId, double money);

    /**
     * Transfers a specified amount of money from a user's bank account to their regular account.
     * If the user has enough money in their bank account, the transfer is performed successfully.
     *
     * @param userId The unique identifier of the user performing the transfer.
     * @param money The amount of money to be transferred from the bank account to the regular account.
     * @return true if the transfer is successful; false if the user does not have enough money in their bank account.
     */
    boolean transferBankMoneyToPlayer(int userId, double money);
}
