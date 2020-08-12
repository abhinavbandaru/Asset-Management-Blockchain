# Asset-Management-Blockchain

Chain.java
    -Initialization:
        creates a bank account(It has unlimited funds and four assets)
        redirects you to homepage
    -homePage()
        user can login or register
    -login()
        checks user credentials and redirects them to inSystem()
    -inSystem()
        allows user to buy and sell assets from bank, add money to wallet, validate chain, see their transactions and logout which will redirect them to the homepage
    -addMoney()
        does a transaction from the bank to the user
    -isChainValid()
        validates the blockchain by verifying each block and every transaction of a block (transactions are validated before addition to the block too)
StringUtil.java
    is responsible for various encryption techniques(SHA-256,ECDSAS) used in this program and also to verify the signatures
    -getMerkleRoot():
        Stores transactions in layers (all the transactions are present here), can be used to view all transactions
Block.java:
    Each user has a new block (for the transactions of a user)
    Initialization: with hash of prev block and users name (used to generate hash of current block)- function calculateHash();
    calculateHash(): used to generate hash of current block by applying SHA256 to time, nonce(increases after every iteration) and
    mineBlock(): used to mine block(proof of work)
    addTransaction()-used to verify and add transaction to the block
Wallet.java
    keyPairGeneration(): used to generate public key and private key (used for zero knowldge proof)
    getBal(): returns user balance
    sendFunds(): used to buy/sell assets
Transaction.java
    processes transactions and verifies signature generated by SHA256
TransactionInput.java, TransactionOutput.java
    used in checking the validity of transaction
