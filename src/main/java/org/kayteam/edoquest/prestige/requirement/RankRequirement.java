package org.kayteam.edoquest.prestige.requirement;

public class RankRequirement extends Requirement{

    public RankRequirement() {
        super("rank");
    }

    private String rank;
    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getRank() {
        return rank;
    }

}