package com.mks.blockchain.controllers;


import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mks.blockchain.domain.Block;
import com.mks.blockchain.domain.BlockChain;
import com.mks.blockchain.domain.CryptoTransaction;
import com.mks.blockchain.domain.Transaction;
import com.mks.blockchain.response.BlockChainResponse;

@RestController
public class BlockChainClientController {

	@Autowired
	private BlockChain blockChain;

	@RequestMapping("/blockchain")
	public BlockChainResponse chain() {
		return new BlockChainResponse(blockChain.getChain());
	}

	/*@RequestMapping(value = "/addblock", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public BlockChainResponse addblock(@RequestBody CryptoTransaction transaction) {
				
		List<CryptoTransaction> currentTransactions = new LinkedList<>();
		currentTransactions.add(transaction);
		
		Block block = new Block(currentTransactions);
				
		blockChain.addBlockToTheChain(block);

		return new BlockChainResponse(blockChain.getChain());
	}*/
	
	@RequestMapping("/verifyblockchain")
	public Boolean verifychain() {
		
		Boolean isChainValid = true;
		Block currentBlock; 
		Block previousBlock;
		List<Block> blockchain = blockChain.getChain();
		
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			
			if(!currentBlock.getcurrentBlockHash().equals(currentBlock.calculateHash()) ){
				isChainValid = false;
			}
			
			if(!previousBlock.getcurrentBlockHash().equals(currentBlock.getPreviousBlockHash()) ) {
				isChainValid = false;
			}
		}
						
		return isChainValid;
	}

}
