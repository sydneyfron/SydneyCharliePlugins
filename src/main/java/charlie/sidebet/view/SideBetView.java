/*
Copyright (c) 2014 Ron Coleman

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package charlie.sidebet.view;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.view.AMoneyManager;
import charlie.view.sprite.Chip;
import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
/**
 * This class implements the side bet view
 * @author Ron.Coleman
 */
public class SideBetView implements ISideBetView {
    private final Logger LOG = Logger.getLogger(SideBetView.class);
    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected BasicStroke stroke = new BasicStroke(3);

    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);

    protected List<ChipButton> buttons;
    protected int amt = 0;
    protected AMoneyManager moneyManager;
    protected List<Chip> chips = new ArrayList<>();
    protected String outcomeText = "";
    protected Color outcomeColor = Color.WHITE;

    public SideBetView() {
        LOG.info("side bet view constructed");
    }

    /**
     * Sets the money manager.
     * @param moneyManager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }

    /**
     * Registers a click for the side bet.
     * This method gets invoked on right mouse click.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        outcomeText = "";
        int oldAmt = amt;

        // Test if any chip button has been pressed.
        for (ChipButton button : buttons) {
            if (button.isPressed(x, y)) {
                amt += button.getAmt();
                SoundFactory.play(Effect.CHIPS_IN);

                int n = chips.size();
                Random ran = new Random();
                int placeX = X + 20 + (n * 10) + ran.nextInt(6) - 3;
                int placeY = Y - 20 + ran.nextInt(6) - 3;

                Chip chip = new Chip(button.getImage(), placeX, placeY, amt);
                chips.add(chip);

                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
            }
        }

        // Check if the click is inside the side bet circle to CLEAR the bet
        // Calculate distance from click (x,y) to center of circle (X,Y)
        double distance = Math.sqrt(Math.pow(x - X, 2) + Math.pow(y - Y, 2));

        // If distance is less than or equal to the radius (DIAMETER / 2)
        if (distance <= DIAMETER / 2) {
            amt = 0;
            chips.clear();
            SoundFactory.play(Effect.CHIPS_OUT);

            LOG.info("B. side bet amount cleared");
        }
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();

        if(bet == 0) {
            outcomeText = "";
            return;
        }

        LOG.info("side bet outcome = "+bet);

        // Update the bankroll
        moneyManager.update(bet);

        // Determine WIN or LOSE text
        if (bet > 0) {
            outcomeText = "WIN!";
            outcomeColor = Color.GREEN;
        } else {
            outcomeText = "LOSE!";
            outcomeColor = Color.RED;
        }

        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting.
     */
    @Override
    public void starting() {
        outcomeText = "";
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view.
     */
    @Override
    public void update() {
    }

    /**
     * Renders the view.
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED);
        g.setStroke(dashed);
        g.drawOval(X-DIAMETER/2, Y-DIAMETER/2, DIAMETER, DIAMETER);

        for (Chip chip: chips){
            chip.render(g);
        }

        // Draw the at-stake amount centered
        g.setFont(font);
        g.setColor(Color.WHITE);

        // 1. Convert the amount to a String
        String amtString = Integer.toString(amt);

        // 2. Get the FontMetrics to measure the text's width in pixels
        java.awt.FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(amtString);

        // 3. Calculate the new X coordinate (Center X minus half the text width)
        int centeredX = X - (textWidth / 2);

        // 4. Draw the string using the new centeredX (Y+5 is usually perfect for vertical centering)
        g.drawString(amtString, centeredX, Y + 5);

        int textX = X + DIAMETER + 40;
        int textY = Y - 50;

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(new Color(255, 255, 0, 200));

        g.drawString("SUPER 7 pays 3:1", textX, textY);
        g.drawString("ROYAL MATCH pays 25:1", textX, textY + 20);
        g.drawString("EXACTLY 13 pays 1:1", textX, textY + 40);

        // Renders highlight “WIN” or “LOSE”
        if (!outcomeText.isEmpty()) {
            Font outcomeFont = new Font("Arial", Font.BOLD, 20);
            g.setFont(outcomeFont);
            java.awt.FontMetrics fmOutcome = g.getFontMetrics(outcomeFont);

            int w = fmOutcome.stringWidth(outcomeText);
            int h = fmOutcome.getHeight();
            int boxX = X + 20;
            int boxY = Y - 10;

            // Draw solid highlight box (Green or Red)
            g.setColor(outcomeColor);
            g.fillRoundRect(boxX, boxY, w + 10, h, 10, 10);

            // Set font color based on outcome
            if (outcomeText.equals("WIN!")) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }

            // Draw the text on top
            g.drawString(outcomeText, boxX + 5, boxY + fmOutcome.getAscent());
        }
    }
}

