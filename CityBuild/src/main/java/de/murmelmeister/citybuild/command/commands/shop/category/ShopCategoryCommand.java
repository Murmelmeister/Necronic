package de.murmelmeister.citybuild.command.commands.shop.category;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ShopCategoryCommand extends CommandManager {
    public ShopCategoryCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!isEnable(sender, Configs.COMMAND_ENABLE_SHOP_CATEGORY)) return true;
        if (!hasPermission(sender, Configs.PERMISSION_SHOP_CATEGORY)) return true;

        /*
         * 0            1
         * shopCategory [0]
         * shopCategory import
         */
        if (args.length == 1 && args[0].equals("import")) {
            shopCategory.importCSV(logger, config);
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_IMPORT).replace("[FILE]", config.getString(Configs.IMPORT_DATA_SHOP_CATEGORIES)));
            return true;
        }

        switch (args[0]) {
            case "add" -> addCategory(sender, command, args);
            case "remove" -> removeCategory(sender, command, args);
            case "edit" -> editCategory(sender, command, args);
            default ->
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return tabComplete(Arrays.asList("add", "remove", "edit", "import"), args);
        if (args.length == 2 && (args[0].equals("remove") || args[0].equals("edit")))
            return tabComplete(shopCategory.getInternNames(), args);
        if (args.length == 3 && args[0].equals("add"))
            return tabComplete(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()), args);
        if (args.length == 3 && args[0].equals("edit"))
            return tabComplete(Arrays.asList("icon", "displayname", "description"), args);
        if (args.length == 4 && args[0].equals("edit") && args[2].equals("icon"))
            return tabComplete(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()), args);
        return Collections.emptyList();
    }

    /*
     * 0            1   2            3             4          5
     * shopCategory [0] [1]          [2]           [3]        [4]
     * shopCategory add <internName> <material> <displayName> [description]
     */
    private void addCategory(CommandSender sender, Command command, String[] args) {
        if (args.length < 4) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        String internName = args[1];

        if (shopCategory.existsCategory(internName)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_EXIST));
            return;
        }

        String materialName = args[2];
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            sendMessage(sender, message.getString(Messages.INVALID_ITEM));
            return;
        }

        int i = 3;
        StringBuilder displayNameBuilder = new StringBuilder();
        if (!args[i].startsWith("\"")) {
            sendMessage(sender, message.getString(Messages.COMMAND_USE_INVERT_COMMAS));
            return;
        }

        while (i < args.length) {
            displayNameBuilder.append(args[i]).append(" ");
            if (args[i].endsWith("\"")) break;
            i++;
        }
        String displayName = displayNameBuilder.toString().trim();
        displayName = displayName.substring(1, displayName.length() - 1);
        i++;

        String description = null;
        if (i < args.length) {
            StringBuilder descriptionBuilder = new StringBuilder();
            while (i < args.length) {
                descriptionBuilder.append(args[i]).append(" ");
                i++;
            }
            description = descriptionBuilder.toString().trim();
        }

        shopCategory.addCategory(internName, displayName, material, description);
        sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_ADD));
    }

    /*
     * 0            1      2
     * shopCategory [0]    [1]
     * shopCategory remove <internName>
     */
    private void removeCategory(CommandSender sender, Command command, String[] args) {
        if (args.length < 2) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        String internName = args[1];

        if (!shopCategory.existsCategory(internName)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
            return;
        }

        UUID categoryId = shopCategory.getCategoryId(internName);
        shopCategory.removeCategory(categoryId);
        sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_REMOVE));
    }

    /*
     * 0            1    2            3                              4
     * shopCategory [0]  [1]          [2]                            [3]
     * shopCategory edit <internName> <displayName|icon|description> <value>
     */
    private void editCategory(CommandSender sender, Command command, String[] args) {
        if (args.length < 4) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        String internName = args[1];

        if (!shopCategory.existsCategory(internName)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
            return;
        }

        UUID categoryId = shopCategory.getCategoryId(internName);

        switch (args[2]) {
            case "icon" -> {
                Material material = Material.getMaterial(args[3]);
                if (material == null) {
                    sendMessage(sender, message.getString(Messages.INVALID_ITEM));
                    return;
                }

                shopCategory.updateIcon(categoryId, material);
                sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_EDIT));
            }
            case "displayname" -> {
                StringBuilder builder = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    builder.append(args[i]).append(" ");
                String displayName = builder.toString().trim();

                shopCategory.updateDisplayName(categoryId, displayName);
                sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_EDIT));
            }
            case "description" -> {
                StringBuilder builder = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    builder.append(args[i]).append(" ");
                String description = builder.toString().trim();

                shopCategory.updateDescription(categoryId, description);
                sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_EDIT));
            }
            default ->
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
    }
}
