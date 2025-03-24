package de.murmelmeister.citybuild.api.economy;

public sealed interface Economy permits EconomyImpl {
    int getUserId();

    double getMoney();

    void setMoney(double money);

    double getBankMoney();

    void setBankMoney(double bankMoney);
}
