package org.kayteam.edoquest.inventories.prestigeeditor;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class CommandsInventory extends InventoryBuilder {

    public CommandsInventory(EdoQuest plugin, Prestige prestige, int page) {
        super(plugin.getInventories().getString("prestige.commands.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        // Panel
        fillItem(() -> inventories.getItemStack("prestige.commands.panel"), new int[] {1, 6});
        // Return
        addItem(0, () -> inventories.getItemStack("prestige.commands.return"));
        addLeftAction(0, ((player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige))));
        // Close
        addItem(8, () -> inventories.getItemStack("prestige.commands.close"));
        addLeftAction(8, ((player, slot) -> player.closeInventory()));
        // Commands
        List<String> commands = prestige.getCommands();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < commands.size()) {
                // Message Item
                addItem(i, () -> Yaml.replace(inventories.getItemStack("prestige.commands.command"), new String[][]{{"%command%", commands.get(index)}}));
                // Move to Left
                addLeftAction(i, (player, i1) -> {
                    if(index > 0){
                        String previousCommand = commands.get(index - 1);
                        String nextCommand = commands.get(index);
                        commands.set(index, previousCommand);
                        commands.set(index - 1, nextCommand);
                        prestige.setCommands(commands);
                        plugin.getPrestigeManager().savePrestige(prestige.getName());
                        plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                    }
                });
                // Move to Right
                addRightAction(i, (player, slot) -> {
                    if(index + 1 < (commands).size()){
                        String previousCommand = commands.get(index + 1);
                        String nextCommand = commands.get(index);
                        commands.set(index, previousCommand);
                        commands.set(index + 1, nextCommand);
                        prestige.setCommands(commands);
                        plugin.getPrestigeManager().savePrestige(prestige.getName());
                        plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                    }
                });
                // Edit
                addLeftShiftAction(i, (player, slot) -> {
                    player.closeInventory();
                    Yaml messages = plugin.getMessages();
                    messages.sendMessage(player, "prestige.commands.commandEditInput");
                    plugin.getInputManager().addInput(player, new ChatInput() {
                        @Override
                        public boolean onChatInput(Player player, String input) {
                            commands.set(index, input);
                            prestige.setCommands(commands);
                            plugin.getPrestigeManager().savePrestige(prestige.getName());
                            plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                            return true;
                        }

                        @Override
                        public void onPlayerSneak(Player player) {
                            plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                        }
                    });
                });
                // Delete Message
                addRightShiftAction(i, (player, slot) -> {
                    commands.remove(index);
                    prestige.setCommands(commands);
                    plugin.getPrestigeManager().savePrestige(prestige.getName());
                    plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                });
            }
        }
        // Add Command
        addItem(49, () -> inventories.getItemStack("prestige.commands.addCommand"));
        addLeftAction(49, (player, i) -> {
            player.closeInventory();
            Yaml messages = plugin.getMessages();
            messages.sendMessage(player, "prestige.commands.commandAddInput");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    commands.add(input);
                    prestige.setCommands(commands);
                    plugin.getPrestigeManager().savePrestige(prestige.getName());
                    plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page));
                }
            });
        });
        // PreviousPage
        if (page > 1) {
            addItem(45, () -> inventories.getItemStack("prestige.commands.previousPage"));
            addLeftAction(45, (player, slot) -> plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige,page - 1)));
        }
        // NextPage
        if (commands.size() > (page * (4 * 9))) {
            addItem(53, () -> inventories.getItemStack("prestige.commands.nextPage"));
            addLeftAction(53, (player, slot) -> plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, page + 1)));
        }

    }

}