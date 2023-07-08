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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SignCommand extends CommandManager {
    public SignCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_SIGN))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_SIGN)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
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
            createItem(player, messages);
            return true;
        }

        if (cooldown.getDuration(player, "Sign") <= System.currentTimeMillis())
            cooldown.removeCooldown(player, "Sign");

        if (cooldown.hasCooldown(player, "Sign")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player, "Sign").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return true;
        }

        cooldown.addCooldown(player, "Sign", config.getLong(Configs.TIME_SIGN_COOLDOWN));
        createItem(player, messages);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private void createItem(Player player, String lore) {
        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        String created = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        List<String> loreList = new ArrayList<>();
        loreList.add(HexColor.format(lore));
        loreList.add(" ");
        loreList.add(HexColor.format(message.getString(Messages.COMMAND_SIGN_CREATE).replace("[NAME]", player.getName()).replace("[DATE]", created.replace(" ", message.getString(Messages.COMMAND_SIGN_DATE)))));
        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
    }
}
