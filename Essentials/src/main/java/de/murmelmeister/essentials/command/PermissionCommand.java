package de.murmelmeister.essentials.command;

import com.velocitypowered.api.command.SimpleCommand;
import de.murmelmeister.murmelapi.permission.Permission;

import java.util.List;

public class PermissionCommand implements SimpleCommand {
    private final Permission permission;

    public PermissionCommand(Permission permission) {
        this.permission = permission;
    }

    @Override
    public void execute(Invocation invocation) {

    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }
}
