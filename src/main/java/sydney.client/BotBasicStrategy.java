package sydney.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;

public class BotBasicStrategy extends BasicStrategy {
    @Override
    public Play getPlay(Hand myHand, Card upCard) {

        Play play = super.getPlay(myHand, upCard);
        if (play != Play.SPLIT) {
            return play;
        }
        // not a split, good to go
        // we need to handle a pair of 2s as a special case
        if (myHand.getValue() == 4) {
            return Play.HIT;
        }
        // basically we are redirecting because botBasicStrategy cannot split
        if (myHand.getValue() <= 8) {
            return doSection2(myHand, upCard);
        }
        else {
            return doSection1(myHand, upCard);
        }
    }
}
