package com.bol.mancala.model;

/**
 * Pit class keeps information about each Pit including the pit index and the number of stones inside that pit.
 */

public class Pit {
    private Integer id;
    private Integer stones;

    public Pit(Integer id, Integer stones) {
        this.id = id;
        this.stones = stones;
    }

    public boolean isEmpty() {
        return this.stones == 0;
    }

    public void clear() {
        this.stones = 0;
    }

    public void sow() {
        this.stones++;
    }

    public void addStones(Integer stones) {
        this.stones += stones;
    }

    public Integer getStones() {
        return stones;
    }

    public void setStones(Integer stones) {
        this.stones = stones;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", stones=" + stones +
                '}';
    }
}
