package org.kayteam.edoquest.prestige;

import org.jetbrains.annotations.NotNull;
import org.kayteam.edoquest.prestige.requirement.KillsRequirement;

import java.util.ArrayList;
import java.util.List;

public class Prestige implements Comparable<Prestige> {

    private final String name;
    private int position = 1;
    private String displayName = "";
    private String prestigeRank = "";
    private final KillsRequirement killsRequirement = new KillsRequirement();
    private List<String> commands = new ArrayList<>();

    public Prestige(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setPrestigeRank(String prestigeRank) {
        this.prestigeRank = prestigeRank;
    }
    public String getPrestigeRank() {
        return prestigeRank;
    }

    public KillsRequirement getKillsRequirement() {
        return killsRequirement;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public int compareTo(@NotNull Prestige p) {
        return Integer.compare(position, p.getPosition());
    }

}