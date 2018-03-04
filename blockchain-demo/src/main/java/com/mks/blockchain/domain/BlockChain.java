package com.mks.blockchain.domain;

import java.util.Collections;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mks.blockchain.helper.Utilities;

//responsible for managing the chain and store transactions 
//And add new blocks to the chain

@Component
public class BlockChain {

	private List<Block> blockChain;

	@Autowired
	public BlockChain() {

		this.blockChain = Collections.synchronizedList(new LinkedList<>());

		//CryptoTransaction genesisBlockData = new CryptoTransaction("First sender", "First recipient", 101);

		//currentTransactions.add(genesisBlockData);

		//addBlockToTheChain(new Block("0", currentTransactions)); 
	}

	public Block addBlockToTheChain(Block block) { 

		if (blockChain != null && blockChain.size() > 0) {

			block.setPreviousBlockHash(blockChain.get(blockChain.size() - 1).getcurrentBlockHash());
		}

		block.setBlockNumber(blockChain.size() + 1);

		//mine the block
		block.mineCurrentBlock(Utilities.difficulty);
		
		this.blockChain.add(block);

		return block;
	}

	public List<Block> getChain() {
		return Collections.unmodifiableList(this.blockChain);
	}

}
