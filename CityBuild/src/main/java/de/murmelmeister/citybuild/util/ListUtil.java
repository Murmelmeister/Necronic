package de.murmelmeister.citybuild.util;

import java.util.*;

public class ListUtil {
    private final Set<UUID> godMode;
    private final Set<UUID> live;
    private final Map<UUID, UUID> tpa;
    private final Map<UUID, UUID> tpaHere;

    public ListUtil() {
        this.godMode = new HashSet<>();
        this.live = new HashSet<>();
        this.tpa = new HashMap<>();
        this.tpaHere = new HashMap<>();
    }

    public Set<UUID> getGodMode() {
        return godMode;
    }

    public Set<UUID> getLive() {
        return live;
    }

    public Map<UUID, UUID> getTpa() {
        return tpa;
    }

    public Map<UUID, UUID> getTpaHere() {
        return tpaHere;
    }
}
