/*
 *   Copyright (C) 2021 SirOswaldo
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.kayteam.edoquest.util.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.kayteam.edoquest.util.inventory.action.*;

import java.util.HashMap;

public class InventoryBuilder {

    private final String title;
    private final int rows;
    private final HashMap<Integer, ItemStack> items = new HashMap<>();
    private final HashMap<Integer, LeftAction> leftActions =  new HashMap<>();
    private final HashMap<Integer, RightAction> rightActions =  new HashMap<>();
    private final HashMap<Integer, MiddleAction> middleActions =  new HashMap<>();
    private final HashMap<Integer, LeftShiftAction> leftShiftActions =  new HashMap<>();
    private final HashMap<Integer, RightShiftAction> rightShiftActions =  new HashMap<>();
    private CloseAction closeAction;

    public InventoryBuilder() {
        title = "Default Title";
        rows = 3;
    }
    public InventoryBuilder(String title) {
        this.title = title;
        rows = 3;
    }
    public InventoryBuilder(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }


    public String getTitle() {
        return title;
    }
    public int getRows() {
        return rows;
    }

    public void fillItem(ItemStack itemStack) {
        for (int i = 0; i < (rows * 9); i++) {
            if (!items.containsKey(i)) {
                items.put(i, itemStack);
            }
        }
    }

    public void addItem(int slot, ItemStack item) {
        items.put(slot, item);
    }
    public ItemStack getItem(int slot) {
        if (items.containsKey(slot)) {
            return items.get(slot);
        }
        return new ItemStack(Material.AIR);
    }
    public void removeItem(int slot) {
        items.remove(slot);
    }

    public void addLeftAction(int slot, LeftAction action) {
        leftActions.put(slot, action);
    }
    public LeftAction getLeftAction(int slot) {
        if (leftActions.containsKey(slot)) {
            return leftActions.get(slot);
        }
        return (player, slot1) -> {};
    }
    public void removeLeftAction(int slot) {
        leftActions.remove(slot);
    }

    public void addRightAction(int slot, RightAction action) {
        rightActions.put(slot, action);
    }
    public RightAction getRightAction(int slot) {
        if (rightActions.containsKey(slot)) {
            return rightActions.get(slot);
        }
        return (player, slot1) -> { };
    }
    public void removeRightAction(int slot) {
        rightActions.remove(slot);
    }

    public void addMiddleAction(int slot, MiddleAction action) {
        middleActions.put(slot, action);
    }
    public MiddleAction getMiddleAction(int slot) {
        if (middleActions.containsKey(slot)) {
            return middleActions.get(slot);
        }
        return (player, slot1) -> {};
    }
    public void removeMiddleAction(int slot) {
        middleActions.remove(slot);
    }

    public void addLeftShiftAction(int slot, LeftShiftAction action) {
        leftShiftActions.put(slot, action);
    }
    public LeftShiftAction getLeftShiftAction(int slot) {
        if (leftShiftActions.containsKey(slot)) {
            return leftShiftActions.get(slot);
        }
        return (player, slot1) -> {};
    }
    public void removeLeftShiftAction(int slot) {
        leftShiftActions.remove(slot);
    }

    public void addRightShiftAction(int slot, RightShiftAction action) {
        rightShiftActions.put(slot, action);
    }
    public RightShiftAction getRightShiftAction(int slot) {
        if (rightShiftActions.containsKey(slot)) {
            return rightShiftActions.get(slot);
        }
        return (player, slot1) -> {};
    }
    public void removeRightShiftAction(int slot) {
        rightShiftActions.remove(slot);
    }
    public void setCloseAction(CloseAction closeAction) {
        this.closeAction = closeAction;
    }
    public CloseAction getCloseAction() {
        if (closeAction != null) {
            return closeAction;
        } else {
            return player -> {};
        }
    }
}