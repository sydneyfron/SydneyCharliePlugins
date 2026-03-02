/*
 * Copyright (c) 2026 Hexant, LLC
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package sydney.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;

/**
 * This class is an incomplete starter implementation of the Basic Strategy.
 * <p>It is table-driven, missing most of the rules and all validation.
 * @author Ron.Coleman
 */
public class BasicStrategy {
    // These help make table formatting compact to look like the pocket card.
    public final static Play P = Play.SPLIT;
    public final static Play H = Play.HIT;
    public final static Play S = Play.STAY;
    public final static Play D = Play.DOUBLE_DOWN;

    /** Rules for section 1; see Instructional Services (2000) pocket card */
    Play[][] section1Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* 21 */ { S, S, S, S, S, S, S, S, S, S },
            /* 20 */ { S, S, S, S, S, S, S, S, S, S },
            /* 19 */ { S, S, S, S, S, S, S, S, S, S },
            /* 18 */ { S, S, S, S, S, S, S, S, S, S },
            /* 17 */ { S, S, S, S, S, S, S, S, S, S },
            /* 16 */ { S, S, S, S, S, H, H, H, H, H },
            /* 15 */ { S, S, S, S, S, H, H, H, H, H },
            /* 14 */ { S, S, S, S, S, H, H, H, H, H },
            /* 13 */ { S, S, S, S, S, H, H, H, H, H },
            /* 12 */ { H, H, S, S, S, H, H, H, H, H },

    };

    /**
     * Gets the play for player's hand vs. dealer up-card.
     * @param hand Hand player hand
     * @param upCard Dealer up-card
     * @return Play based on basic strategy
     */
    public Play getPlay(Hand hand, Card upCard) {
        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        if(hand.isPair()) {
            // TODO: return doSection4(hand,upCard)
        }
        else if(hand.size() == 2 && (card1.getRank() == Card.ACE || card2.getRank() == Card.ACE)) {
            // TODO: return doSection3(hand,upCard)
        }
        else if(hand.getValue() >=5 && hand.getValue() < 12) {
            // TODO: return doSection2(hand,upCard)
        }
        else if(hand.getValue() >= 12)
            return doSection1(hand,upCard);

        return Play.NONE;
    }

    /**
     * Does section 1 processing of the basic strategy, 12-21 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection1(Hand hand, Card upCard) {
        int value = hand.getValue();

        // Section 1 currently only supports hands >= 20 (see above).
        if(value < 12)
            return Play.NONE;

        // TODO: Complete getting the row in the table.

        // Subtract 21 since the player's hand starts at 21 and we're working
        // our way down through section 1 from index 0.
        int rowIndex = 21 - value;

        Play[] row = section1Rules[rowIndex];

        // TODO: Complete getting the column in the table.

        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;

        if(upCard.isFace())
            colIndex = 10 - 2;

            // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;

        // At this row, col we should have the correct play defined.
        Play play = row[colIndex];

        return play;
    }

    /**
     * Validates a hand and up-card.
     * @param hand Hand
     * @param upCard Up-card
     * @return True if both are valid, false otherwise
     */
    boolean isValid(Hand hand, Card upCard) {
        return isValid(hand) && isValid(upCard);
    }

    /**
     * Validates a hand.
     * @param hand Hand
     * @return True if valid, false otherwise
     */
    boolean isValid(Hand hand) {
        // TODO: Complete.
        return true;
    }

    /**
     * Validates a card
     * @param card Card
     * @return True if valid, false otherwise
     */
    boolean isValid(Card card) {
        // TODO: Complete.
        return true;
    }
}