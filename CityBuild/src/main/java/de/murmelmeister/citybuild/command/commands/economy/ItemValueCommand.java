package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemValueCommand extends CommandManager {
    public ItemValueCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ITEM_VALUE_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_ITEM_VALUE_COMMAND))) return true;

        if (args.length >= 2) {
            switch (args[0]) {
                case "get":
                    getItemValue(sender, args);
                    break;
                case "set":
                    setItemValue(sender, args);
                    break;
                default:
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                    break;
            }
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return tabComplete(Arrays.asList("get", "set"), args);
        if (args.length == 2) return tabComplete(itemValue.getItems(), args);
        return Collections.emptyList();
    }

    /*
    /itemValue get <material>
     */
    private void getItemValue(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ITEM_VALUE_GET))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_ITEM_VALUE_GET))) return;

        String materialName = args[1].toUpperCase();
        if (!(itemValue.existName(materialName))) {
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_MATERIAL_NOT_EXIST).replace("[MATERIAL]", materialName));
            return;
        }
        Material material = Material.valueOf(materialName);

        if (config.getBoolean(Configs.MATERIAL_CASE))
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_GET).replace("[ITEM]", material.name().toLowerCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(material)))
                    .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        else
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_GET).replace("[ITEM]", material.name().toUpperCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(material)))
                    .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
    }

    /*
    /itemValue set <material> <price>
     */
    private void setItemValue(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ITEM_VALUE_SET))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_ITEM_VALUE_SET))) return;

        String materialName = args[1].toUpperCase();
        if (!(itemValue.existName(materialName))) {
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_MATERIAL_NOT_EXIST).replace("[MATERIAL]", materialName));
            return;
        }
        Material material = Material.valueOf(materialName);

        try {
            BigDecimal price = new BigDecimal(args[2]);
            itemValue.setValue(material, price);
            if (config.getBoolean(Configs.MATERIAL_CASE))
                sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_SET).replace("[ITEM]", material.name().toLowerCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(material)))
                        .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
            else
                sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_SET).replace("[ITEM]", material.name().toUpperCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(material)))
                        .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        } catch (NumberFormatException exception) {
            sendMessage(sender, message.getString(Messages.NO_NUMBER));
        }
    }
}
