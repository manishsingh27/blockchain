package com.mks.blockchain.response;

import java.util.List;

import com.mks.blockchain.domain.Block;


public class BlockChainResponse {
	
	private List<Block> chain;

    private int length;

    public BlockChainResponse() {

    }

    public BlockChainResponse(List<Block> chain) {
        this.chain = chain;
        this.length = chain.size();
    }

    public List<Block> getChain() {
        return chain;
    }

    public int getLength() {
        return length;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
