package org.kayteam.edoquest.commands;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.EdoQuestInventory;
import org.kayteam.kayteamapi.command.SimpleCommand;
import org.kayteam.kayteamapi.yaml.Yaml;

public class EdoQuestCommand extends SimpleCommand {

    private final EdoQuest plugin;

    public EdoQuestCommand(EdoQuest plugin) {
        super(plugin, "EdoQuest");
        this.plugin = plugin;
    }

    @Override
    public void onPlayerExecute(Player player, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("edoquest.admin")) {
            plugin.getInventoryManager().openInventory(player, new EdoQuestInventory(plugin, player));
        } else {
            messages.sendMessage(player, "edoQuest.noPermission");
        }
    }
}