package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Play;
import sydney.client.BotBasicStrategy;
import java.util.List;

public class Huey implements IBot {
    Seat mySeat;
    Hand myHand;
    Dealer dealer;
    Card upCard;
    BotBasicStrategy bs = new BotBasicStrategy();

    @Override
    public Hand getHand() {
        Hid hid = new Hid(this.mySeat);
        this.myHand = new Hand(hid);
        return this.myHand;
    }

    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public void sit(Seat seat) {
        this.mySeat = seat;
    }

    @Override
    public void startGame(List<Hid> hids, int shoeSize) { }

    @Override
    public void deal(Hid hid, Card card, int[] values) {
        if (card == null) return;

        // Track the Dealer's up-card to know when the game starts
        if (hid.getSeat() == Seat.DEALER) {
            this.upCard = card;
        }

        // If the card is for Huey
        if (hid.getSeat() == mySeat) {

            // If upCard isn't null fist deal is over
            // We only need to potentially make a play if we haven't busted or reached 21.
            if (this.upCard != null && myHand.getValue() < 21) {

                // If have 3 cards check if came from a double down
                if (this.myHand.size() == 3) {

                    // Reconstruct the starting 2-card hand to see what we previously requested
                    Hand initialHand = new Hand(new Hid(mySeat));
                    initialHand.hit(this.myHand.getCard(0));
                    initialHand.hit(this.myHand.getCard(1));

                    Play pastPlay = bs.getPlay(initialHand, this.upCard);

                    if (pastPlay == Play.DOUBLE_DOWN) {
                        // The Dealer ends our turn automatically after a double down.
                        // Do NOT play again!
                        return;
                    }
                }
                // If it wasn't a double down, it was a normal HIT, so must make next move
                play(hid);
            }
        }
    }

    @Override
    public void play(final Hid hid) {
        // Only trigger our play loop if the request is actually for us
        if (hid.getSeat() != mySeat) {
            return;
        }

        // Spawn a worker thread so the single-threaded Dealer isn't blocked
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Pause to think
                    Thread.sleep(2000 + new java.util.Random().nextInt(1000));

                    Play advice = bs.getPlay(myHand, upCard);

                    if (advice == Play.HIT) {
                        dealer.hit(Huey.this, myHand.getHid());
                    } else if (advice == Play.DOUBLE_DOWN) {
                        dealer.doubleDown(Huey.this, myHand.getHid());
                    } else {
                        dealer.stay(Huey.this, myHand.getHid());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override public void bust(Hid hid) {}
    @Override public void blackjack(Hid hid) {}
    @Override public void charlie(Hid hid) {}
    @Override public void endGame(int shoeSize) {}
    @Override public void insure() {}
    @Override public void win(Hid hid) {}
    @Override public void lose(Hid hid) {}
    @Override public void push(Hid hid) {}
    @Override public void shuffling() {}
    @Override public void split(Hid hid, Hid hid1) {}
}