package org.kayteam.edoquest.inventories.prestigeeditor;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class KillsRequirementInventory extends InventoryBuilder {

    public KillsRequirementInventory(EdoQuest plugin, Prestige prestige, int page) {
        super(plugin.getInventories().getString("killsRequirement.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("killsRequirement.panel"), new int[]{1, 6});
        // Return :D
        addItem(0, () -> inventories.getItemStack("killsRequirement.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin,prestige)));
        // Close :D
        addItem(8, () -> inventories.getItemStack("killsRequirement.close"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // Entities
        List<EntityType> entityTypes = prestige.getKillsRequirement().getEntities();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < entityTypes.size()) {
                addItem(i, () -> Yaml.replace(inventories.getItemStack("killsRequirement.entity"), new String[][] {
                        {"%entityType%", entityTypes.get(index).toString()},
                        {"%killsAmount%", prestige.getKillsRequirement().getAmount(entityTypes.get(index)) + ""}
                }));
                addLeftAction(i, (player, slot) -> {
                    player.closeInventory();
                    Yaml messages = plugin.getMessages();
                    messages.sendMessage(player, "killsRequirements.input");
                    plugin.getInputManager().addInput(player, new ChatInput() {
                        @Override
                        public boolean onChatInput(Player player, String input) {
                            try {
                                int amount = Integer.parseInt(input);
                                if (amount > 0) {
                                    prestige.getKillsRequirement().addEntity(entityTypes.get(index), amount);
                                    plugin.getPrestigeManager().savePrestige(prestige.getName());
                                    plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, page));
                                    return true;
                                } else {
                                    messages.sendMessage(player, "killsRequirements.invalidNumber");
                                }
                            } catch (NumberFormatException e) {
                                messages.sendMessage(player, "killsRequirements.invalidNumber");
                            }
                            return false;
                        }

                        @Override
                        public void onPlayerSneak(Player player) {
                            plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, page));
                        }
                    });
                });
                addRightAction(i, (player, slot) -> {
                    prestige.getKillsRequirement().removeEntity(entityTypes.get(index));
                    plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, page));
                });
            }
        }
        // Add Entity
        addItem(49, () -> inventories.getItemStack("killsRequirement.addEntity"));
        addLeftAction(49, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EntityChooserInventory(plugin, prestige, 1)));
        // PreviousPage
        if (page > 1) {
            addItem(45, () -> inventories.getItemStack("killsRequirement.previousPage"));
            addLeftAction(45, (player, slot) -> plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige,page - 1)));
        }
        // NextPage
        if (entityTypes.size() > (page * (4 * 9))) {
            addItem(53, () -> inventories.getItemStack("killsRequirement.nextPage"));
            addLeftAction(53, (player, slot) -> plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, page + 1)));
        }
    }

}