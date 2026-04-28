package sydney.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;

public class ShoeBug02 extends Shoe{

    @Override
    public void init(){
        cards.clear();
        cards.add(new Card(10, Card.Suit.SPADES));
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(6, Card.Suit.CLUBS));
        cards.add(new Card(Card.ACE, Card.Suit.CLUBS));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
    }

}