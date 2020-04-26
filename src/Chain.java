import java.security.Security;
import java.util.*;
import org.bouncycastle.*;

public class Chain {
    static List<String> usernames = new ArrayList<>();
    static List<String> passwords = new ArrayList<>();
    static ArrayList<Wallet> wallets = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);
    static int k = 0;
    public static Transaction genT;
    static Wallet bank = new Wallet();
    static Block genesis = new Block("0","bankUser");
    //    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<>();
    public static ArrayList<Block> blockchain = new ArrayList<>();
    static Block prevBlock;
    public static void main(String[] args){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
        prevBlock = genesis;
        usernames.add("bankUser");
        passwords.add("bank123");
        Wallet wallet = new Wallet();
        wallets.add(wallet);
        wallet.addMoney(10000);
        wallet.assetList.add("Asset1");
        wallet.assetList.add("Asset2");
        wallet.assetList.add("Asset3");
        wallet.assetList.add("Asset4");
        wallet.costlist.add(30);
        wallet.costlist.add(40);
        wallet.costlist.add(20);
        wallet.costlist.add(70);
        blockchain.add(genesis);
        homePage();
    }

    public static void homePage() {
        System.out.println("Please select an option:\n1.Login - press 1\n2.Register - press 2");
        int num = scan.nextInt();
        if (num == 1)
            login();
        else if (num == 2)
            register();
        else {
            System.out.println("Please enter a valid number");
            homePage();
        }
    }
    public static void register(){
        System.out.print("Please Enter Your username: ");
        Scanner scan = new Scanner(System.in);
        String username;
        username = scan.next();
        boolean flag = false;
        for (String user: usernames) {
            if (user.equals(username)) {
                flag = true;
                break;
            }
        }
        if(flag){
            System.out.println("Username already taken, please choose another username");
            register();
        }
        else{
            System.out.print("Please Enter Your Password: ");
            String password;
            password = scan.next();
            usernames.add(username);
            passwords.add(password);
            Wallet wallet = new Wallet();
            wallets.add(wallet);
            homePage();
        }

    }

    public static void login(){
        String username;
        String password;
        boolean flag = false;
        System.out.print("Please enter your username: ");
        username = scan.next();
        System.out.print("Please enter your password: ");
        password = scan.next();
        for (int i = 0; i < usernames.size(); i++) {
            if(usernames.get(i).equals(username)){
                if(passwords.get(i).equals(password)){
                    flag = true;
                    break;
                }
            }
        }
        if(flag){
            inSystem(username);
        }
        else{
            System.out.println("Your credentials don't match.");
            homePage();
        }
    }
    public static void inSystem(String username){
        System.out.println("Welcome " + username);

        System.out.println("Please select an option:\n1.Buy Assets\n2.Show My Transactions\n3.Validate\n4.Logout\n5.Add Money\n6.Sell Assets\n7.Balance");
        int num = scan.nextInt();
        int store = 0;
        for (int i = 0; i < usernames.size(); i++) {
            if(usernames.get(i).equals(username)){
                store = i;
            }
        }
        if(num == 1){
            System.out.println("What asset would you like to buy:");
            int i = 0;
            for (String asset:wallets.get(0).assetList) {
                i++;
                System.out.println(i+"."+asset+"-"+wallets.get(0).costlist.get(i-1));
            }
            int n = scan.nextInt() + 1;
            if(n>i-1 || n < 1){
                System.out.println("Please enter a valid number");
                inSystem(username);
                return;
            }
            i = 0;
            boolean flag = false;
            for (Block blo: blockchain) {
                if(blo.user.equals(username)) {
                    flag = true;
                    break;
                }
                i++;
            }

            if(!flag) {
                System.out.println("No money in wallet, please add money");
                inSystem(username);
                return;
            }
            Block block = blockchain.get(i);
            boolean check = block.addTransaction(wallets.get(store).sendFunds(wallets.get(0).publicKey, wallets.get(0).costlist.get(n), wallets.get(0).assetList.get(n)));
            if(check){
                System.out.println("Transaction Completed, Thank you for buying " + wallets.get(0).assetList.get(n));
                int j = 0;
                for (Block blo: blockchain) {
                    if(blo.user.equals(username))
                        break;
                    j++;
                }
                wallets.get(store).assetList.add(wallets.get(0).assetList.get(n));
                wallets.get(store).costlist.add(wallets.get(0).costlist.get(n));
                wallets.get(0).assetList.remove(n);
                wallets.get(0).costlist.remove(n);
            }
            else
                System.out.println("Please try again");
            inSystem(username);
        }
        else if(num == 2){
            int i = 0;
            boolean flag = false;
            for (Block blo: blockchain) {
                if(blo.user.equals(username)) {
                    flag = true;
                    break;
                }
                i++;
            }
            if(!flag){
                System.out.println("You have not made a transaction yet");
            }
            else {
                System.out.println("Your Transactions:\n");
                for (Transaction transaction: blockchain.get(i).transactions){
                    System.out.println("Transaction id " + transaction.transactionId);
                    System.out.println("Sender " + transaction.sender);
                    System.out.println("Recipient " + transaction.reciepient);
                    System.out.println("Asset transferred to sender " + transaction.asset);
                    System.out.println("Value of Transaction " + transaction.value);
                    System.out.println("Signature" + Arrays.toString(transaction.signature)+"\n");
                }
            }
            inSystem(username);
        }
        else if(num == 3){
            isChainValid();
            inSystem(username);
        }
        else if(num == 4)
            homePage();
        else if(num == 5){
            System.out.print("How much money would you like to add:");
            int amount = scan.nextInt();
            addMoney(wallets.get(store), amount, username);
            System.out.println(amount + " successfully transferred to your account");
            inSystem(username);
        }
        else  if(num == 6){
            int i = 0;
            boolean flag = false;
            for (Block blo: blockchain) {
                if(blo.user.equals(username)) {
                    flag = true;
                    break;
                }
                i++;
            }
            if(wallets.get(i).assetList.isEmpty()){
                System.out.println("You do not own any assets");
                inSystem(username);
            }
            else {
                System.out.println("What asset would you like to sell: ");
                int lol = 0;
                for (String asset: wallets.get(i).assetList) {
                    lol++;
                    System.out.println(lol+"."+asset);
                }
                int nu = scan.nextInt() - 1;
                Block block = blockchain.get(i);
                if(nu>lol-1||nu<1)
                {
                    System.out.println("Please enter a valid number");
                    inSystem(username);
                }
                else {
                    boolean check = block.addTransaction(wallets.get(0).sendFunds(wallets.get(i).publicKey, wallets.get(i).costlist.get(nu), wallets.get(i).assetList.get(nu)));
                    if(check){
                        System.out.println("Transaction Completed, Thank you for selling " + wallets.get(i).assetList.get(nu));
                        int j = 0;
                        for (Block blo: blockchain) {
                            if(blo.user.equals(username))
                                break;
                            j++;
                        }
                        wallets.get(0).assetList.add(wallets.get(i).assetList.get(nu));
                        wallets.get(0).costlist.add(wallets.get(i).costlist.get(nu));
                        wallets.get(i).assetList.remove(nu);
                        wallets.get(i).costlist.remove(nu);
                    }
                    else
                        System.out.println("Please try again");
                    inSystem(username);
                }
            }
        }
        else if(num == 7){
            System.out.println(wallets.get(store).getBalance());
            inSystem(username);
        }
        else{
            System.out.println("Please enter a valid number");
            inSystem(username);
        }
    }
    public static void isChainValid() {
        Block currentBlock;
        Block previousBlock;
        int difficulty = 5;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return;
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.reciepient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return;
                }

            }

        }
        System.out.println("Blockchain is valid");
    }

    public static void addBlock(Block newBlock) {
        int difficulty = 3;
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static void addMoney(Wallet wallet, int amount, String username){
        Transaction genT;
        genT = new Transaction(bank.publicKey, wallet.publicKey, amount, null, null, 0);
        genT.generateSignature(bank.privateKey);	 //manually sign the genesis transaction
        genT.transactionId = Integer.toString(k++); //manually set the transaction id
        genT.outputs.add(new TransactionOutput(genT.reciepient, genT.value, genT.transactionId)); //manually add the Transactions Output
        int i = 0;
        boolean flag = false;
        for (Block blo: blockchain) {
            if(blo.user.equals(username)) {
                flag = true;
                break;
            }
            i++;
        }
        Block blo;
        if(!flag) {
            blo = new Block(prevBlock.hash, username);
            addBlock(blo);
            prevBlock = blo;
        }
        else
            blo = blockchain.get(i);
        blo.addTransaction(genT);
        i = 0;
        for (Block bla: blockchain) {
            if(bla.user.equals(username)) {
                break;
            }
            i++;
        }
        wallets.get(i).addMoney(amount);
    }
}