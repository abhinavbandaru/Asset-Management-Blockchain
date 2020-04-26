import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public ArrayList<String> assetList = new ArrayList<>();
    public ArrayList<Integer> costlist = new ArrayList<>();
    private int bal = 0;

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getBalance() {
        int total = bal;
        return total;
    }
    public void addMoney(int a){
        bal += a;
    }


    public Transaction sendFunds(PublicKey _recipient,float value, String asset) {
        if(getBalance() < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, asset, inputs, bal);
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }

}