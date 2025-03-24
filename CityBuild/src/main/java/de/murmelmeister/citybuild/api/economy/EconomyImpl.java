package de.murmelmeister.citybuild.api.economy;

public final class EconomyImpl implements Economy {
    private final int userId;
    private double money;
    private double bankMoney;

    public EconomyImpl(int userId, double money, double bankMoney) {
        this.userId = userId;
        this.money = money;
        this.bankMoney = bankMoney;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public double getBankMoney() {
        return bankMoney;
    }

    @Override
    public void setBankMoney(double bankMoney) {
        this.bankMoney = bankMoney;
    }
}
