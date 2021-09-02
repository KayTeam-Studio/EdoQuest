package org.kayteam.edoquest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.kayteam.edoquest.prestige.Prestige;

public class QuestCompleteEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;

    private final Player player;
    private final Prestige prestige;

    public QuestCompleteEvent(Player player, Prestige prestige) {
        this.player = player;
        this.prestige = prestige;
    }

    public Player getPlayer() {
        return player;
    }

    public Prestige getPrestige() {
        return prestige;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
