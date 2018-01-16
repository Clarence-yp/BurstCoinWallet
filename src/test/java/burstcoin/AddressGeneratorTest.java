package burstcoin;

import static org.junit.Assert.*;
import org.junit.Test;

public class AddressGeneratorTest {

    /**
     * Test case generated from
     * https://wallet1.burstnation.com:8125/index.html
     */
    @Test
    public void test() {
        AddressGenerator addressGenerator = new AddressGenerator();
        String passphrase = "shape course glad pen dove grant disappear stage natural funny existence government";
        String testAddressgenerated = "BURST-D4MQ-9ADQ-2XAR-6RVKR";
        assertEquals( testAddressgenerated, addressGenerator.generate(passphrase));
    }

}
