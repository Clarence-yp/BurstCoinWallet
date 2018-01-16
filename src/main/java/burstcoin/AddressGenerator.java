package burstcoin;

import burstcoin.nxt.Curve25519;
import burstcoin.nxt.ReedSolomon;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AddressGenerator {
    /**
     * @param passwordLength passwordlength for random generation
     * @param prefix prefix string on password
     * @param postfix postfix string on password
     * @return
     */
    public String generate(int passwordLength, String prefix, String postfix)
    {
        String random = RandomStringUtils.random(passwordLength, true, true);
        String password = prefix + random + postfix;
        String addressRS = getAddressRS(password.getBytes(Charset.forName("UTF-8")));
        return addressRS;
    }

    public String generate(String passPhrase)
    {
        String password = passPhrase;
        String addressRS = getAddressRS(password.getBytes(Charset.forName("UTF-8")));
        return addressRS;
    }

    private String getAddressRS(byte[] secretPhraseBytes)
    {
        byte[] publicKeyHash = getMessageDigest().digest(getPublicKey(secretPhraseBytes));
        Long accountId = fullHashToId(publicKeyHash);
        return "BURST-" + ReedSolomon.encode(nullToZero(accountId));
    }

    /**
     * Null to zero.
     *
     * @param l the l
     * @return the long
     */
    public long nullToZero(Long l)
    {
        return l == null ? 0 : l;
    }

    /**
     * Gets message digest.
     *
     * @return the message digest
     */
    public MessageDigest getMessageDigest()
    {
        try
        {
            return MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Get public key.
     *
     * @param secretPhraseBytes the secret phrase bytes
     * @return the byte [ ]
     */
    public byte[] getPublicKey(byte[] secretPhraseBytes)
    {
        byte[] publicKey = new byte[32];
        Curve25519.keygen(publicKey, null, getMessageDigest().digest(secretPhraseBytes));
        return publicKey;
    }

    /**
     * Full hash to id.
     *
     * @param hash the hash
     * @return the long
     */
    public Long fullHashToId(byte[] hash)
    {
        if(hash == null || hash.length < 8)
        {
            throw new IllegalArgumentException("Invalid hash: " + Arrays.toString(hash));
        }
        BigInteger bigInteger = new BigInteger(1, new byte[]{hash[7], hash[6], hash[5], hash[4], hash[3], hash[2], hash[1], hash[0]});
        return bigInteger.longValue();
    }

}
