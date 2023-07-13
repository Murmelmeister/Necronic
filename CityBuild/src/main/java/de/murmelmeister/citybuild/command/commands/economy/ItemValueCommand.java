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
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ITEM_VALUE_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ITEM_VALUE_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

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
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ITEM_VALUE_GET))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ITEM_VALUE_GET)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

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

    private void setItemValue(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ITEM_VALUE_SET))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ITEM_VALUE_SET)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        String materialName = args[1].toUpperCase();
        BigDecimal price = new BigDecimal(args[2]);
        if (!(itemValue.existName(materialName))) {
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_MATERIAL_NOT_EXIST).replace("[MATERIAL]", materialName));
            return;
        }
        Material material = Material.valueOf(materialName);

        itemValue.setValue(material, price);
        if (config.getBoolean(Configs.MATERIAL_CASE))
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_SET).replace("[ITEM]", material.name().toLowerCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(material)))
                    .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        else
            sendMessage(sender, message.getString(Messages.COMMAND_ITEM_VALUE_SET).replace("[ITEM]", material.name().toUpperCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(material)))
                    .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
    }
}
