package de.murmelmeister.lobby.util.scoreboard;

public enum EntryName {

    ENTRY_0(0, "§0"), ENTRY_1(1, "§1"), ENTRY_2(2, "§2"), ENTRY_3(3, "§3"), ENTRY_4(4, "§4"), ENTRY_5(5, "§5"),
    ENTRY_6(6, "§6"), ENTRY_7(7, "§7"), ENTRY_8(8, "§8"), ENTRY_9(9, "§9"), ENTRY_10(10, "§a"), ENTRY_11(11, "§b"),
    ENTRY_12(12, "§c"), ENTRY_13(13, "§d"), ENTRY_14(14, "§e"), ENTRY_15(15, "§f");

    private final int entry;
    private final String entryName;

    EntryName(int entry, String entryName) {
        this.entry = entry;
        this.entryName = entryName;
    }

    public int getEntry() {
        return entry;
    }

    public String getEntryName() {
        return entryName;
    }
}
