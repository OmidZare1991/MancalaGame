package com.bol.mancala.repository;

import com.bol.mancala.model.MancalaGame;

public interface MancalaGameRepository {

    void update(MancalaGame game);

    MancalaGame get(String id);
}
