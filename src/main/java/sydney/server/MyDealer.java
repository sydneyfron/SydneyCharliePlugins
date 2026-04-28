package sydney.server;

import charlie.card.Hid;
import charlie.card.Hand;
import charlie.card.Card;
import charlie.plugin.IPlayer;
import charlie.dealer.Dealer;
import charlie.actor.House;
import charlie.util.Play;
import org.apache.log4j.Logger;

/**
 * MyDealer implementation fixing splitting Ace bugs and second-hand Blackjack detection.
 */
public class MyDealer extends Dealer {
    private final Logger LOG = Logger.getLogger(MyDealer.class);

    public MyDealer(House house) {
        super(house);
    }

    @Override
    public void hit(IPlayer iplayer, Hid hid) {
        Hand hand = validate(hid);
        if (hand == null) return;

        Card card = deal();
        hand.hit(card);
        hid.request(Play.HIT);

        for (IPlayer player : playerSequence) {
            player.deal(hid, card, hand.getValues());
        }

        if (hand.isBroke()) {
            updateBankroll(hid, LOSS);
            for (IPlayer _player : playerSequence) {
                _player.bust(hid);
            }
            goNextHand();
        }
        else if (hand.isCharlie()) {
            hid.request(Play.STAY);
            updateBankroll(hid, CHARLIE_PAYS);
            for (IPlayer _player : playerSequence) {
                _player.charlie(hid);
            }
            goNextHand();
        }
        else if (hand.isBlackjack()) {
            hid.request(Play.STAY);
            updateBankroll(hid, BLACKJACK_PAYS);
            for (IPlayer _player : playerSequence) {
                // Fix: Notify as Blackjack, not Charlie
                _player.blackjack(hid);
            }
            goNextHand();
        }
        else if (hand.getValue() == 21) {
            goNextHand();
        }
    }

    @Override
    public void split(IPlayer player, Hid hid) {
        Hand origHand = validate(hid);
        if (origHand == null) return;

        Hid newHid = new Hid(hid.getSeat(), hid.getAmt(), 0);
        newHid.setSplit(true);
        hid.setSplit(true);

        hid.request(Play.SPLIT);
        newHid.request(Play.SPLIT);

        Hand newHand = origHand.split(newHid);
        players.put(newHand.getHid(), player);

        int i = handSequence.indexOf(hid);
        handSequence.add((i + 1), newHand.getHid());
        hands.put(newHid, newHand);

        player.split(newHid, hid);

        // Process First Hand
        Card card1 = deal();
        origHand.hit(card1);
        for (IPlayer p : playerSequence) {
            p.deal(hid, card1, origHand.getValues());
        }

        // Check if First Hand is a win
        if (origHand.isBlackjack()) {
            updateBankroll(hid, BLACKJACK_PAYS);
            for (IPlayer p : playerSequence) {
                p.blackjack(hid);
            }
        }

        // Process Second Hand
        Card card2 = deal();
        newHand.hit(card2);
        for (IPlayer p : playerSequence) {
            p.deal(newHid, card2, newHand.getValues());
        }

        // FIX: Check if the Second Hand is a Blackjack win
        if (newHand.isBlackjack()) {
            updateBankroll(newHid, BLACKJACK_PAYS);
            for (IPlayer p : playerSequence) {
                p.blackjack(newHid);
            }
        }

        // Advance the system turn
        goNextHand();
    }
}