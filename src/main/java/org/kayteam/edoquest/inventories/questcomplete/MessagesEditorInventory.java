package org.kayteam.edoquest.inventories.questcomplete;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class MessagesEditorInventory extends InventoryBuilder {

    public MessagesEditorInventory(EdoQuest plugin, int page) {
        super(plugin.getInventories().getString("questComplete.messagesEditor.title"), 6);
        Yaml inventories = plugin.getInventories();
        for (int i = 0; i < 9; i++) addItem(i, () -> inventories.getItemStack("questComplete.messagesEditor.panel"));
        for (int i = 45; i < 54; i++) addItem(i, () -> inventories.getItemStack("questComplete.messagesEditor.panel"));
        // Return
        addItem(0, () -> inventories.getItemStack("questComplete.messagesEditor.return"));
        addLeftAction(0, (player, i) -> plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin)));
        // Close
        addItem(8, () -> inventories.getItemStack("questComplete.messagesEditor.close"));
        addLeftAction(8, (player, i) -> player.closeInventory());
        // Messages
        List<String> messageList = (List<String>) plugin.getSettings().getList("questComplete.messages.texts");
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < messageList.size()) {
                // Message Item
                addItem(i, () -> Yaml.replace(inventories.getItemStack("questComplete.messagesEditor.message"),
                        new String[][]{{"%message%", messageList.get(index)}}));
                // Move to Left
                addLeftAction(i, (player, i1) -> {
                    if(index > 0){
                        String previousMessage = messageList.get(index-1);
                        String nextMessage = messageList.get(index);
                        messageList.set(index, previousMessage);
                        messageList.set(index-1, nextMessage);
                        plugin.getSettings().set("questComplete.messages.text", messageList);
                        plugin.getSettings().saveFileConfiguration();
                        plugin.getInventoryManager().openInventory(player, new MessagesEditorInventory(plugin, page));
                    }
                });
                // Move to Right
                addRightAction(i, (player, i1) -> {
                    if(index+1 < (messageList).size()){
                        String previousMessage = messageList.get(index+1);
                        String nextMessage = messageList.get(index);
                        messageList.set(index, previousMessage);
                        messageList.set(index+1, nextMessage);
                        plugin.getSettings().set("questComplete.messages.text", messageList);
                        plugin.getSettings().saveFileConfiguration();
                        plugin.getInventoryManager().openInventory(player, new MessagesEditorInventory(plugin, page));
                    }
                });
                // Delete Message
                addLeftShiftAction(i, (player, i1) -> {
                    messageList.remove(index);
                    plugin.getSettings().set("questComplete.messages.text", messageList);
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new MessagesEditorInventory(plugin, page));
                });
            }
        }
        // Add Message
        addItem(49, () -> inventories.getItemStack("questComplete.messagesEditor.addMessage"));
        addLeftAction(49, (player, i) -> {
            player.closeInventory();
            Yaml messages = plugin.getMessages();
            messages.sendMessage(player, "questComplete.messageInput");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    List<String> messages = (List<String>) plugin.getSettings().getList("questComplete.messages.texts");
                    messages.add(input);
                    plugin.getSettings().set("questComplete.messages.text", messages);
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new MessagesEditorInventory(plugin, page));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new MessagesEditorInventory(plugin, page));
                }
            });
        });
        // PreviousPage
        if (page > 1) {
            addItem(45, () -> inventories.getItemStack("questComplete.messagesEditor.previousPage"));
            addLeftAction(45, (player, i) -> plugin.getInventoryManager().openInventory(player, new SoundSelectorInventory(plugin,page - 1)));
        }
        // NextPage
        if (messageList.size() > (page * (4 * 9))) {
            addItem(53, () -> inventories.getItemStack("questComplete.messagesEditor.nextPage"));
            addLeftAction(53, (player, i) -> plugin.getInventoryManager().openInventory(player, new SoundSelectorInventory(plugin, page + 1)));
        }
    }
}
