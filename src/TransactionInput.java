public class TransactionInput {
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}