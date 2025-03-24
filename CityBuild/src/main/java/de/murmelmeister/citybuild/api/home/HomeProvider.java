package de.murmelmeister.citybuild.api.home;

import java.util.List;

public sealed interface HomeProvider permits HomeProviderImpl {
    Home getHome(int userId, String homeName);

    boolean existsHome(int userId, String homeName);

    void addHome(Home home);

    void removeHome(Home home);

    List<String> getHomeNames(int userId);
}
