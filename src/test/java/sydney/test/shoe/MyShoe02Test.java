package sydney.test.shoe;

import charlie.card.Card;
import charlie.plugin.IShoe;
import junit.framework.TestCase;
import sydney.plugin.MyShoe02;

public class MyShoe02Test extends TestCase {

    public void test(){
        IShoe shoe = new MyShoe02();
        shoe.init();
        assert shoe.size() == 10;
        Card card1 = shoe.next();
        assert card1.getRank() == 2;
        Card card2 = shoe.next();
        assert card2.getRank() == Card.QUEEN;



    }
}
