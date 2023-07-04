package de.murmelmeister.citybuild.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ListUtil {
    private final Set<UUID> godMode;

    public ListUtil() {
        this.godMode = new HashSet<>();
    }

    public Set<UUID> getGodMode() {
        return godMode;
    }
}
