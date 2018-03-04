package com.mks.blockchain.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mks.blockchain.domain.Block;
import com.mks.blockchain.domain.BlockChain;
import com.mks.blockchain.domain.CryptoTransaction;
import com.mks.blockchain.domain.TransactionInput;
import com.mks.blockchain.domain.TransactionOutput;
import com.mks.blockchain.domain.TransactionTracker;
import com.mks.blockchain.helper.Utilities;
import com.mks.blockchain.response.BlockChainResponse;
import com.mks.blockchain.wallet.MyWallet;

@RestController
public class CryptoCurrencyController {

	@Autowired
	private BlockChain blockChain;

	@Autowired
	private MyWallet senderWallet;

	@Autowired
	private MyWallet reciverWallet;

	public static CryptoTransaction genesisTransaction;

	@RequestMapping("/verifysig")
	public Boolean verifysig() {

		CryptoTransaction transaction = new CryptoTransaction(senderWallet.publicKey, reciverWallet.publicKey, 5, null);
		transaction.generateSignature(senderWallet.privateKey);

		return transaction.verifiySignature();

	}

	@RequestMapping("/startCryptoBlockchain")
	public void startCryptoBlockchain() {

		MyWallet firstWallet = new MyWallet();

		genesisTransaction = new CryptoTransaction(firstWallet.publicKey, senderWallet.publicKey, 100f, null);
		genesisTransaction.generateSignature(firstWallet.privateKey);

		genesisTransaction.setTransactionId("0");

		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.getReciepient(),
				genesisTransaction.getValue(), genesisTransaction.getTransactionId()));

		TransactionTracker.UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		// Creating and Mining Genesis block
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		blockChain.addBlockToTheChain(genesis);

		Block block1 = new Block(genesis.getcurrentBlockHash());

		block1.addTransaction(senderWallet.sendFunds(reciverWallet.publicKey, 40f));
		blockChain.addBlockToTheChain(block1);

		Block block2 = new Block(block1.getcurrentBlockHash());

		block2.addTransaction(senderWallet.sendFunds(reciverWallet.publicKey, 200f));
		blockChain.addBlockToTheChain(block2);

		Block block3 = new Block(block2.getcurrentBlockHash());

		block3.addTransaction(reciverWallet.sendFunds(senderWallet.publicKey, 20));
		
		System.out.println("Sender's balance is: " + senderWallet.getBalance());
		System.out.println("Receiver's balance is: " + reciverWallet.getBalance());
		
				

	}
	
	@RequestMapping("/verifycrptoblockchain")
	public Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;

		List<Block> blockchain = blockChain.getChain();
		String hashTarget = Utilities.getZerosBasedOnInput(Utilities.difficulty);
		HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); 
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		// loop through blockchain to check hashes:
		for (int i = 1; i < blockchain.size(); i++) {

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			// compare registered hash and calculated hash:
			if (!currentBlock.getcurrentBlockHash().equals(currentBlock.calculateHash())) {
				System.out.println("#Current Hashes not equal");
				return false;
			}
			// compare previous hash and registered previous hash
			if (!previousBlock.getcurrentBlockHash().equals(currentBlock.getPreviousBlockHash())) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			// check if hash is solved
			if (!currentBlock.getcurrentBlockHash().substring(0, Utilities.difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}

			// loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
				CryptoTransaction currentTransaction = currentBlock.getTransactions().get(t);

				if (!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}
				if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for (TransactionInput input : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if (tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if (input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for (TransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if (currentTransaction.outputs.get(0).reciepient != currentTransaction.getReciepient()) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if (currentTransaction.outputs.get(1).reciepient != currentTransaction.getSender()) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}

			}

		}
		System.out.println("Blockchain is valid");
		return true;
	}

}
