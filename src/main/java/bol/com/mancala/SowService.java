package bol.com.mancala;

import org.springframework.stereotype.Component;

@Component
public class SowService {

    public void sowRight(MancalaGame game, boolean isLastStone) {

        // the pitIndex to start sowing from
        int currentPitIndex = game.getCurrentPitIndex() % MancalaConstants.TOTAL_PITS + 1;

        Players player = game.getPlayerTurn();

        //no stones are put in the opponent's big pit
        if ((currentPitIndex == MancalaConstants.RIGHT_BIG_PIT_ID && player == Players.PLAYER_B) ||
                (currentPitIndex == MancalaConstants.LEFT_BIG_PIT_ID && player == Players.PLAYER_A)) {

            currentPitIndex = currentPitIndex % MancalaConstants.TOTAL_PITS + 1;
        }
        game.setCurrentPitIndex(currentPitIndex);
        Pit pit = game.getPit(currentPitIndex);

        if (!isLastStone || currentPitIndex == MancalaConstants.RIGHT_BIG_PIT_ID ||
                currentPitIndex == MancalaConstants.LEFT_BIG_PIT_ID) {
            pit.sow();
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status

        Pit oppositePit = game.getPit(MancalaConstants.TOTAL_PITS - currentPitIndex);

        //when the last stone lands in an own empty pit,the player captures his own stones and all stones in the opposite pit
        //and puts them in his big pit
        if (pit.isEmpty() && !oppositePit.isEmpty()) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer bigPitIndex = currentPitIndex < MancalaConstants.RIGHT_BIG_PIT_ID ? MancalaConstants.RIGHT_BIG_PIT_ID : MancalaConstants.LEFT_BIG_PIT_ID;
            Pit bigPit = game.getPit(bigPitIndex);
            bigPit.addStones(oppositeStones + 1);
            return;
        }
        pit.sow();
    }
}
