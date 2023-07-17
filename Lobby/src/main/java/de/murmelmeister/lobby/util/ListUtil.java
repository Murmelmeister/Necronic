package de.murmelmeister.lobby.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListUtil {
    private final List<UUID> build;

    public ListUtil() {
        this.build = new ArrayList<>();
    }

    public List<UUID> getBuild() {
        return build;
    }
}
