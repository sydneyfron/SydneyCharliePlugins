package charlie.sidebet.rule;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.ISideBetRule;
import org.apache.log4j.Logger;

/**
 * This class implements side bet rules for Super 7, Royal Match, and Exactly 13.
 * It returns the highest payout among the matching rules.
 * @author Ron.Coleman
 */
public class SideBetRule implements ISideBetRule {
    private final Logger LOG = Logger.getLogger(SideBetRule.class);

    // Payout constants (Multiplier of the bet)
    private final Double PAYOFF_SUPER7 = 3.0;
    private final Double PAYOFF_ROYAL_MATCH = 25.0;
    private final Double PAYOFF_EXACTLY_13 = 1.0;

    @Override
    public double apply(Hand hand) {
        Double bet = hand.getHid().getSideAmt();

        if (bet <= 0) {
            return 0.0;
        }

        LOG.info("Side bet rule applying to hand: " + hand);

        double maxPayout = -1.0; // Default to a loss (-1 * bet)

        // Rule 1: Super 7 (First card is a 7)
        Card card1 = hand.getCard(0);
        if (card1.getRank() == 7) {
            maxPayout = Math.max(maxPayout, PAYOFF_SUPER7);
            LOG.info("Super 7 match found.");
        }

        // The following rules require at least two cards
        if (hand.size() >= 2) {
            Card card2 = hand.getCard(1);

            // Rule 2: Royal Match (Suited King + Queen)
            if (card1.getSuit() == card2.getSuit()) {
                boolean isK1 = card1.getRank() == Card.KING;
                boolean isQ1 = card1.getRank() == Card.QUEEN;
                boolean isK2 = card2.getRank() == Card.KING;
                boolean isQ2 = card2.getRank() == Card.QUEEN;

                if ((isK1 && isQ2) || (isQ1 && isK2)) {
                    maxPayout = Math.max(maxPayout, PAYOFF_ROYAL_MATCH);
                    LOG.info("Royal Match match found.");
                }
            }

            // Rule 3: Exactly 13 (Sum is 13, Aces = 1)
            // Note: Card.getRank() typically returns 1 for Ace in most engines.
            // If card1.value() is used, it often returns 11 for Ace;
            // the prompt specifies Aces must count as 1.
            int val1 = (card1.getRank() > 10) ? 10 : card1.getRank();
            int val2 = (card2.getRank() > 10) ? 10 : card2.getRank();

            if (val1 + val2 == 13) {
                maxPayout = Math.max(maxPayout, PAYOFF_EXACTLY_13);
                LOG.info("Exactly 13 match found.");
            }
        }

        // If maxPayout is still -1.0, no rules matched
        if (maxPayout > 0) {
            return bet * maxPayout;
        }

        LOG.info("Side bet rule no match");
        return -bet;
    }
}