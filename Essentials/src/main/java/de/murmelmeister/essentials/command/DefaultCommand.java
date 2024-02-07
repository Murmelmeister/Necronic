package de.murmelmeister.essentials.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.permission.PermissionConfig;
import de.murmelmeister.murmelapi.util.StringUtils;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCommand implements SimpleCommand {
    private final Permission permission;
    private final String[] mcServers = {"ByteMiner", "Necronic"};

    public DefaultCommand(Permission permission) {
        this.permission = permission;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();
        Group group = permission.getGroup();

        if (!(source.hasPermission("essentials.command.default"))) {
            source.sendMessage(Component.text("§cYou don't have the right permission to do that."));
            return;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "ByteMiner":
                    byteMiner(source, group);
                    break;
                case "Necronic":
                    necronic(source, group);
                    break;
                default:
                    syntax(source);
                    break;
            }
        } else syntax(source);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1)
            return Arrays.stream(mcServers).filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        return Collections.emptyList();
    }

    private void byteMiner(CommandSource source, Group group) {
        String defaultPermission = permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION);

        String owner = "owner";
        String admin = "admin";
        String dev = "dev";
        String mod = "mod";
        String builder = "builder";
        String helper = "helper";
        String promotion = "promotion";
        String byteRank = "byte";

        group.create(owner, "#e23d00O#da2e1cw#d31f38n#cb0f53e#c3006fr #454545» #c3006f", "", "#00e246", "#e23d00O#da2e1cw#d31f38n#cb0f53e#c3006fr#454545〡", "", "#c3006f", "§4Owner§8〡", "", "&4", "0000", "Owner", "#c3006f");
        group.create(admin, "#df0036A#d40143d#ca024fm#bf025ci#b40368n #454545» #b40368", "", "&r", "#df0036A#d40143d#ca024fm#bf025ci#b40368n#454545〡", "", "#b40368", "§cAdmin§8〡", "", "&c", "0001", "Admin", "#b40368");
        group.create(dev, "#2000a2D#2000b8e#1f00cev#1c0fdde#113addl#0766ddo#0283dep#019adee#00b0dfr #454545» #00b0df", "", "&r", "#2000a2D#2000b8e#1f00cev#1c0fdde#113addl#0766ddo#0283dep#019adee#00b0dfr#454545〡", "", "#00b0df", "§bDev§8〡", "", "&b", "0002", "Developer", "#00b0df");
        group.create(mod, "#005625M#0d6120o#1a6c1cd#267617e#338113r#408c0ea#4d9709t#59a105o#66ac00r #454545» #66ac00", "", "&r", "#005625M#0d6120o#1a6c1cd#267617e#338113r#408c0ea#4d9709t#59a105o#66ac00r#454545〡", "", "#66ac00", "§2Mod§8〡", "", "&2", "0003", "Moderator", "#66ac00");
        group.create(builder, "#2b8619B#3f8c1au#54931bi#68991cl#7c9f1cd#91a61de#a5ac1er #454545» #a5ac1e", "", "&r", "#2b8619B#3f8c1au#54931bi#68991cl#7c9f1cd#91a61de#a5ac1er#454545〡", "", "#a5ac1e", "§aBuilder§8〡", "", "&a", "0004", "Builder", "#a5ac1e");
        group.create(helper, "#e78708H#e18f07e#dc9805l#d6a004p#d1a902e#cbb101r #454545» #cbb101", "", "&r", "#e78708H#e18f07e#dc9805l#d6a004p#d1a902e#cbb101r#454545〡", "", "#cbb101", "§eHelper§8〡", "", "&e", "0005", "Helper", "#cbb101");
        group.create(promotion, "#8700feP#8700fer#9100ebo#9100ebm#9a00d9o#a400c6t#a400b7i#a500a8o#a50099n #454545» #a50099", "", "&r", "#8700feP#8700fer#9100ebo#9100ebm#9a00d9o#a400c6t#a400b7i#a500a8o#a50099n#454545〡", "", "#a50099", "§5Promotion§8〡", "", "&5", "0006", "Promotion", "#a50099");
        group.create(byteRank, "#a54a00B#b06300y#bb7b00t#c69400e #454545» #c69400", "", "&r", "#a54a00B#b06300y#bb7b00t#c69400e#454545〡", "", "#c69400", "§6Byte§8〡", "", "&6", "0007", "Byte", "#c69400");

        for (String name : group.groups()) {
            group.addPermission(name, defaultPermission + name, -1);
            switch (name) {
                case "owner":
                    group.addParent(owner, admin, -1);
                    group.addPermission(owner, "-" + defaultPermission + admin, -1);
                    break;
                case "admin":
                    group.addParent(admin, dev, -1);
                    group.addPermission(admin, "-" + defaultPermission + dev, -1);
                    break;
                case "dev":
                    group.addParent(dev, mod, -1);
                    group.addPermission(dev, "-" + defaultPermission + mod, -1);
                    break;
                case "mod":
                    group.addParent(mod, builder, -1);
                    group.addPermission(mod, "-" + defaultPermission + builder, -1);
                    break;
                case "builder":
                    group.addParent(builder, helper, -1);
                    group.addPermission(builder, "-" + defaultPermission + helper, -1);
                    break;
                case "helper":
                    group.addParent(helper, promotion, -1);
                    group.addPermission(helper, "-" + defaultPermission + promotion, -1);
                    break;
                case "promotion":
                    group.addParent(promotion, byteRank, -1);
                    group.addPermission(promotion, "-" + defaultPermission + byteRank, -1);
                    break;
                case "byte":
                    group.addParent(byteRank, permission.defaultGroup(), -1);
                    group.addPermission(byteRank, "-" + defaultPermission + permission.defaultGroup(), -1);
                    break;
            }
        }
        source.sendMessage(Component.text("§3Finished."));
    }

    private void necronic(CommandSource source, Group group) {
        String defaultPermission = permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION);

        String owner = "owner";
        String admin = "admin";
        String famous1 = "famous+";
        String gameDesigner = "gameDesigner";
        String mod = "mod";
        String helper = "helper";
        String testTeam = "testTeam";
        String famous2 = "famous";
        String special = "special";
        String necro = "necro";

        group.create(owner, "#d03f09O#c8340bw#c1290en#b91d10e#b11212r &7× #b11212", "", "&r", "#d03f09O#c8340bw#c1290en#b91d10e#b11212r &7× ", "", "#b11212", "&4Owner &7× ", "", "&4", "0000", "Owner", "#b11212");
        group.create(admin, "#f90b76A#e70d5dd#d50f44m#c3102bi#b11212n &7× #b11212", "", "&r", "#f90b76A#e70d5dd#d50f44m#c3102bi#b11212n &7× ", "", "#b11212", "&cAdmin &7× ", "", "&c", "0001", "Admin", "#b11212");
        group.create(famous1, "#7701c8F#8607c7a#950ec6m#a111bbo#ac11a4u#b7118ds#6d0dc7+ &7× #b7118d", "", "&r", "#7701c8F#8607c7a#950ec6m#a111bbo#ac11a4u#b7118ds#6d0dc7+ &7× ", "", "#b7118d", "&dFamous+ &7× ", "", "&d", "0002", "Famous+", "#b7118d");
        group.create(gameDesigner, "#351eaaG#3a1cb1a#3f1ab8m#4517bee#5016c3D#6714c4e#7d13c5s#9411c6i#9111beg#8110b2n#7110a7e#610f9br &7× #610f9b", "", "&r", "#351eaaG#3a1cb1a#3f1ab8m#4517bee#5016c3D#6714c4e#7d13c5s#9411c6i#9111beg#8110b2n#7110a7e#610f9br &7× ", "", "#610f9b", "&5GameDesigner &7× ", "", "&5", "0003", "GameDesigner", "#610f9b");
        group.create(mod, "#4caf17M#5fa31bo#72981fd#859727e#9bab35r#b1bf43a#8fb23bt#529328o#147415r &7× #147415", "", "&r", "#4caf17M#5fa31bo#72981fd#859727e#9bab35r#b1bf43a#8fb23bt#529328o#147415r &7× ", "", "#147415", "&2Mod &7× ", "", "&2", "0004", "Moderator", "#147415");
        group.create(helper, "#c18012H#be931be#bba624l#c6be29p#e1db29e#fbf929r &7× #fbf929", "", "&r", "#c18012H#be931be#bba624l#c6be29p#e1db29e#fbf929r &7× ", "", "#fbf929", "&eHelper &7× ", "", "&e", "0005", "Helper", "#fbf929");
        group.create(testTeam, "#d8d8d8T#cbcbcbe#bfbfbfs#b2b2b2t#a0a0a0T#878787e#6f6f6fa#565656m &7× #565656", "", "&r", "#d8d8d8T#cbcbcbe#bfbfbfs#b2b2b2t#a0a0a0T#878787e#6f6f6fa#565656m &7× ", "", "#565656", "&8TestTeam &7× ", "", "&8", "0006", "TestTeam", "#565656");
        group.create(famous2, "#7701c8F#8607c7a#950ec6m#a111bbo#ac11a4u#b7118ds &7× #b7118d", "", "&r", "#7701c8F#8607c7a#950ec6m#a111bbo#ac11a4u#b7118ds &7× ", "", "#b7118d", "&dFamous &7× ", "", "&d", "0007", "Famous", "#b7118d");
        group.create(special, "#87ce05S#71c30cp#5bb714e#45ac1bc#308f23i#1b732aa#065632l &7× #065632", "", "&r", "#87ce05S#71c30cp#5bb714e#45ac1bc#308f23i#1b732aa#065632l &7× ", "", "#065632", "&aSpecial &7× ", "", "&a", "0008", "Special", "#065632");
        group.create(necro, "#ce8303N#bd9206e#aca108c#cfd004r#f2ff00o &7× #f2ff00", "", "&r", "#ce8303N#bd9206e#aca108c#cfd004r#f2ff00o &7× ", "", "#f2ff00", "&eNecro &7× ", "", "&e", "0009", "Necro", "#f2ff00");

        for (String name : group.groups()) {
            group.addPermission(name, defaultPermission + name, -1);
            switch (name) {
                case "owner":
                    group.addPermission(owner, "-" + defaultPermission + admin, -1);
                    group.addPermission(owner, "-" + defaultPermission + famous1, -1);
                    group.addPermission(owner, "-" + defaultPermission + gameDesigner, -1);
                    group.addPermission(owner, "-" + defaultPermission + mod, -1);
                    group.addPermission(owner, "-" + defaultPermission + helper, -1);
                    group.addPermission(owner, "-" + defaultPermission + testTeam, -1);
                    group.addPermission(owner, "-" + defaultPermission + famous2, -1);
                    group.addPermission(owner, "-" + defaultPermission + special, -1);
                    group.addPermission(owner, "-" + defaultPermission + necro, -1);
                    group.addPermission(owner, "-" + defaultPermission + permission.defaultGroup(), -1);
                    group.addPermission(owner, "*", -1);
                    break;
                case "admin":
                    group.addParent(admin, famous1, -1);
                    group.addPermission(admin, "-" + defaultPermission + famous1, -1);
                    group.addPermission(admin, "*", -1);
                    break;
                case "famous+":
                    group.addParent(famous1, gameDesigner, -1);
                    group.addPermission(famous1, "-" + defaultPermission + gameDesigner, -1);
                    break;
                case "gameDesigner":
                    group.addParent(gameDesigner, mod, -1);
                    group.addPermission(gameDesigner, "-" + defaultPermission + mod, -1);
                    break;
                case "mod":
                    group.addParent(mod, helper, -1);
                    group.addPermission(mod, "-" + defaultPermission + helper, -1);
                    break;
                case "helper":
                    group.addParent(helper, testTeam, -1);
                    group.addPermission(helper, "-" + defaultPermission + testTeam, -1);
                    break;
                case "testTeam":
                    group.addParent(testTeam, famous2, -1);
                    group.addPermission(testTeam, "-" + defaultPermission + famous2, -1);
                    break;
                case "famous":
                    group.addParent(famous2, special, -1);
                    group.addPermission(famous2, "-" + defaultPermission + special, -1);
                    break;
                case "special":
                    group.addParent(special, necro, -1);
                    group.addPermission(special, "-" + defaultPermission + necro, -1);
                    break;
                case "necro":
                    group.addParent(necro, permission.defaultGroup(), -1);
                    group.addPermission(necro, "-" + defaultPermission + permission.defaultGroup(), -1);
                    break;
            }
        }
        source.sendMessage(Component.text("§3Finished."));
    }

    private void syntax(CommandSource source) {
        source.sendMessage(Component.text("§7Syntax: §c/default <mc-server>"));
        source.sendMessage(Component.text("§3Minecraft-Servers: "));
        for (String mcServer : mcServers) source.sendMessage(Component.text("§r- §e" + mcServer));
    }
}
