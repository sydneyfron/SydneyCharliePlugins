package sydney.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;

public class BotShoe01 extends Shoe {

    @Override
    public void init() {
        // Clear the shoe so it only contains the cards we manually add
        cards.clear();

        // GAME 1: Huey Hit/Win | You Hit/Win | Dewey Stay/Win
        // Upcard: 7. Huey(16) hits to 18. You(15) hits to 19. Dewey(20) stays.
        // Dealer: K+7 (17).
        cards.add(new Card(13, Card.Suit.SPADES));   // R1: Huey K
        cards.add(new Card(10, Card.Suit.HEARTS));   // R1: You 10
        cards.add(new Card(10, Card.Suit.CLUBS));    // R1: Dewey 10
        cards.add(new Card(13, Card.Suit.DIAMONDS)); // R1: Dealer Hole K

        cards.add(new Card(6, Card.Suit.SPADES));    // R2: Huey 6
        cards.add(new Card(5, Card.Suit.HEARTS));    // R2: You 5
        cards.add(new Card(10, Card.Suit.SPADES));   // R2: Dewey 10
        cards.add(new Card(7, Card.Suit.CLUBS));     // R2: Dealer Upcard 7

        cards.add(new Card(2, Card.Suit.SPADES));    // Huey Hits: 2 (gets 18, stays)
        cards.add(new Card(4, Card.Suit.HEARTS));    // You Hits: 4 (gets 19, stays)
        // Dewey & Dealer both stay

        // GAME 2: Huey Double/Lose | You Stay/Lose | Dewey Stay/Lose
        // Upcard: 5. Huey(11) doubles to 13. You(15) stays. Dewey(14) stays.
        // Dealer: A+5 (soft 16), hits 5 -> 21.
        cards.add(new Card(6, Card.Suit.SPADES));    // R1: Huey 6
        cards.add(new Card(10, Card.Suit.HEARTS));   // R1: You 10
        cards.add(new Card(10, Card.Suit.CLUBS));    // R1: Dewey 10
        cards.add(new Card(1, Card.Suit.DIAMONDS));  // R1: Dealer Hole Ace (1)

        cards.add(new Card(5, Card.Suit.SPADES));    // R2: Huey 5 (Total 11)
        cards.add(new Card(5, Card.Suit.HEARTS));    // R2: You 5 (Total 15)
        cards.add(new Card(4, Card.Suit.CLUBS));     // R2: Dewey 4 (Total 14)
        cards.add(new Card(5, Card.Suit.SPADES));    // R2: Dealer Upcard 5

        cards.add(new Card(2, Card.Suit.SPADES));    // Huey Doubles: gets 2 (13)
        // You and Dewey stay
        cards.add(new Card(5, Card.Suit.CLUBS));     // Dealer Hits: 5 (gets 21)

        // GAME 3: Huey Hit/Win | You Stay/Win | Dewey Double/Win
        // Upcard: 7. Huey(16) hits 5 -> 21. You(19) stays. Dewey(11) doubles 9 -> 20.
        // Dealer: 7+7 (14), hits 3 -> 17.
        cards.add(new Card(10, Card.Suit.SPADES));   // R1: Huey 10
        cards.add(new Card(10, Card.Suit.HEARTS));   // R1: You 10
        cards.add(new Card(8, Card.Suit.CLUBS));     // R1: Dewey 8
        cards.add(new Card(7, Card.Suit.DIAMONDS));  // R1: Dealer Hole 7

        cards.add(new Card(6, Card.Suit.SPADES));    // R2: Huey 6
        cards.add(new Card(9, Card.Suit.HEARTS));    // R2: You 9
        cards.add(new Card(3, Card.Suit.CLUBS));     // R2: Dewey 3
        cards.add(new Card(7, Card.Suit.SPADES));    // R2: Dealer Upcard 7

        cards.add(new Card(5, Card.Suit.SPADES));    // Huey Hits: 5 (21)
        cards.add(new Card(9, Card.Suit.CLUBS));     // Dewey Doubles: gets 9 (20)
        cards.add(new Card(3, Card.Suit.DIAMONDS));  // Dealer Hits: 3 (17)

        // GAME 4: Huey Hit/Break | You Stay/Lose | Dewey Hit/Win
        // Upcard: 8. Huey(15) hits K and busts. You(17) stays. Dewey(16) hits 3 -> 19.
        // Dealer: 6+8 (14), hits 4 -> 18.
        cards.add(new Card(10, Card.Suit.SPADES));   // R1: Huey 10
        cards.add(new Card(10, Card.Suit.HEARTS));   // R1: You 10
        cards.add(new Card(10, Card.Suit.CLUBS));    // R1: Dewey 10
        cards.add(new Card(6, Card.Suit.DIAMONDS));  // R1: Dealer Hole 6

        cards.add(new Card(5, Card.Suit.SPADES));    // R2: Huey 5 (15)
        cards.add(new Card(7, Card.Suit.HEARTS));    // R2: You 7 (17)
        cards.add(new Card(6, Card.Suit.CLUBS));     // R2: Dewey 6 (16)
        cards.add(new Card(8, Card.Suit.SPADES));    // R2: Dealer Upcard 8

        cards.add(new Card(13, Card.Suit.SPADES));   // Huey Hits: King (Busts 25)
        cards.add(new Card(3, Card.Suit.CLUBS));     // Dewey Hits: 3 (19)
        cards.add(new Card(4, Card.Suit.DIAMONDS));  // Dealer Hits: 4 (18)

        // GAME 5: Huey Win | You Win | Dewey Hit/Push
        // Upcard: 4. Huey(21) wins. You(21) wins. Dewey(8) hits K -> 18 push.
        // Dealer: 6+4 (10), hits 8 -> 18.
        cards.add(new Card(10, Card.Suit.SPADES));   // R1: Huey 10
        cards.add(new Card(10, Card.Suit.HEARTS));   // R1: You 10
        cards.add(new Card(5, Card.Suit.CLUBS));     // R1: Dewey 5
        cards.add(new Card(6, Card.Suit.DIAMONDS));  // R1: Dealer Hole 6

        cards.add(new Card(Card.ACE, Card.Suit.SPADES));    // R2: Huey ACE (21)
        cards.add(new Card(Card.ACE, Card.Suit.HEARTS));   // R2: You ACE (21)
        cards.add(new Card(3, Card.Suit.CLUBS));     // R2: Dewey 3 (8)
        cards.add(new Card(4, Card.Suit.SPADES));    // R2: Dealer Upcard 4

        // Huey and You stay based on basic strategy vs 4 upcard
        cards.add(new Card(13, Card.Suit.CLUBS));    // Dewey Hits: King (18)
        cards.add(new Card(8, Card.Suit.DIAMONDS));  // Dealer Hits: 8 (18)

        // GAME 6: Huey Split intercepted to Hit (Push) | You Win | Dewey Break
        // Upcard: 7. Huey(2+2=4) hits 3 -> 7, hits 10 -> 17. You(21) wins. Dewey(16) hits K -> Bust.
        // Dealer: 10+7 (17).
        cards.add(new Card(2, Card.Suit.SPADES));    // R1: Huey 2
        cards.add(new Card(10, Card.Suit.HEARTS));   // R1: You 10
        cards.add(new Card(10, Card.Suit.CLUBS));    // R1: Dewey 10
        cards.add(new Card(10, Card.Suit.DIAMONDS)); // R1: Dealer Hole 10

        cards.add(new Card(2, Card.Suit.CLUBS));     // R2: Huey 2 (4)
        cards.add(new Card(Card.ACE, Card.Suit.HEARTS));   // R2: You Ace (21)
        cards.add(new Card(6, Card.Suit.SPADES));    // R2: Dewey 6 (16)
        cards.add(new Card(7, Card.Suit.CLUBS));     // R2: Dealer Upcard 7

        cards.add(new Card(3, Card.Suit.SPADES));    // Huey Hits: 3 (7)
        cards.add(new Card(10, Card.Suit.SPADES));   // Huey Hits: 10 (17)
        cards.add(new Card(13, Card.Suit.CLUBS));    // Dewey Hits: King (Busts 26)
    }
}