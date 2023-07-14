package de.murmelmeister.citybuild.command.commands;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand extends CommandManager {
    public RenameCommand(Main main) {
        super(main);
    }

    /*
    /rename <displayName...>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RENAME))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_RENAME))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length == 0) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg).append(" ");
        }
        String messages = messageBuilder.toString();
        messages = messages.trim();

        if (player.getItemInHand().getType().equals(Material.AIR)) return true;

        if (player.hasPermission(config.getString(Configs.PERMISSION_NOT_COOLDOWN))) {
            createItem(player.getItemInHand(), messages);
            return true;
        }

        if (cooldown.getDuration(player, "Rename") <= System.currentTimeMillis())
            cooldown.removeCooldown(player, "Rename");

        if (cooldown.hasCooldown(player, "Rename")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player, "Rename").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return true;
        }

        cooldown.addCooldown(player, "Rename", config.getLong(Configs.TIME_RENAME_COOLDOWN));
        createItem(player.getItemInHand(), messages);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private ItemStack createItem(ItemStack itemStack, String displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(HexColor.format(displayName));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
