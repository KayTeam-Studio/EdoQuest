package org.kayteam.edoquest.prestige;

import org.jetbrains.annotations.NotNull;
import org.kayteam.edoquest.prestige.requirement.KillsRequirement;

public class Prestige implements Comparable<Prestige> {

    public Prestige(String name) {
        this.name = name;
    }

    private final String name;
    public String getName() {
        return name;
    }

    private String displayName = "";
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    private String prestigeRank = "";
    public void setPrestigeRank(String prestigeRank) {
        this.prestigeRank = prestigeRank;
    }
    public String getPrestigeRank() {
        return prestigeRank;
    }

    private final KillsRequirement killsRequirement = new KillsRequirement();
    public KillsRequirement getKillsRequirement() {
        return killsRequirement;
    }

    private int position = 0;
    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(@NotNull Prestige p) {
        return Integer.compare(position, p.getPosition());
    }
}