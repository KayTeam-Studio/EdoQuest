package org.kayteam.edoquest.commands;

import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.util.command.SimpleCommand;

public class EdoQuestCommand extends SimpleCommand {

    private final EdoQuest plugin;

    public EdoQuestCommand(EdoQuest plugin) {
        super(plugin, "EdoQuest");
        this.plugin = plugin;
    }



}