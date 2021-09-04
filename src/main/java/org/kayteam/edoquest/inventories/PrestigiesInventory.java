package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.prestigeeditor.PrestigeEditorInventory;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class PrestigiesInventory extends InventoryBuilder {

    public PrestigiesInventory(EdoQuest plugin, int page) {
        super(plugin.getInventories().getString("prestigies.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("prestigies.panel"), new int[] {1, 6});
        // Return
        addItem(0, () -> inventories.getItemStack("prestigies.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EdoQuestInventory(plugin)));
        // Close
        addItem(8, () -> inventories.getItemStack("prestigies.close"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // Prestigies
        List<Prestige> prestigies = plugin.getPrestigeManager().getPrestigeList();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < prestigies.size()) {
                Prestige prestige = prestigies.get(index);
                addItem(i, () -> Yaml.replace(inventories.getItemStack("prestigies.prestige"), new String[][] {
                        {"%name%", prestige.getName()},
                        {"%position%", prestige.getPosition() + ""}
                }));
                addLeftAction(i, (player, slot) -> {
                    int position = plugin.getPrestigeManager().getListIndex(prestige);
                    if (position > 0) {
                        prestige.setPosition(position - 1);
                        plugin.getPrestigeManager().movePrestige(prestige, position - 1);
                        plugin.getPrestigeManager().sortPrestigies();
                        plugin.getPrestigeManager().savePrestige(prestige.getName());
                        plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, page));
                    }
                });
                addRightAction(i, (player, slot) -> {
                    int position = plugin.getPrestigeManager().getListIndex(prestige);
                    if (position + 1 != plugin.getPrestigeManager().getPrestigeList().size()) {
                        prestige.setPosition(position + 1);
                        plugin.getPrestigeManager().movePrestige(prestige, position + 1);
                        plugin.getPrestigeManager().sortPrestigies();
                        plugin.getPrestigeManager().savePrestige(prestige.getName());
                        plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, page));
                    }
                });
                addLeftShiftAction(i, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige)));
            }
        }
        // createPrestige
        addItem(49, () -> inventories.getItemStack("prestigies.createPrestige"));
        addLeftAction(49, (player, slot) -> {
            player.closeInventory();
            Yaml messages = plugin.getMessages();
            messages.sendMessage(player, "createPrestige.inputName");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    if (!input.contains(" ")) {
                        if (input.length() < 33) {
                            if (plugin.getPrestigeManager().getPrestige(input) == null) {
                                Prestige prestige = new Prestige(input);
                                plugin.getPrestigeManager().addPrestige(prestige);
                                plugin.getPrestigeManager().savePrestige(input);
                                plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, page));
                                return true;
                            } else {
                                messages.sendMessage(player, "createPrestige.alreadyExist", new String[][]{{"%name%", input}});
                            }
                        } else {
                            messages.sendMessage(player, "createPrestige.tooLong");
                        }
                    } else {
                        messages.sendMessage(player, "createPrestige.containSpace");
                    }
                    return false;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, page));
                }
            });
        });
        // PreviousPage
        if (page > 1) {
            addItem(45, () -> inventories.getItemStack("prestigies.previousPage"));
            addLeftAction(45, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin,page - 1)));
        }
        // NextPage
        if (prestigies.size() > (page * (4 * 9))) {
            addItem(53, () -> inventories.getItemStack("prestigies.nextPage"));
            addLeftAction(53, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, page + 1)));
        }
    }

}