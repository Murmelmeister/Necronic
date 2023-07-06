package de.murmelmeister.citybuild.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ListUtil {
    private final Set<UUID> godMode;
    private final Set<UUID> live;

    public ListUtil() {
        this.godMode = new HashSet<>();
        this.live = new HashSet<>();
    }

    public Set<UUID> getGodMode() {
        return godMode;
    }

    public Set<UUID> getLive() {
        return live;
    }
}
