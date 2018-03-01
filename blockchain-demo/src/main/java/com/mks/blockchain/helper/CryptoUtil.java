package com.mks.blockchain.helper;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import com.mks.blockchain.domain.Block;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class CryptoUtil {
		

	    private static ObjectMapper objectMapper = new ObjectMapper();

	    public static String hashBlock(Block block) {
	        try {
	            return applySha256(objectMapper.writeValueAsString(block));
	        } catch (JsonProcessingException e) {
	            return null;
	        }
	    }

	    public static String applySha256(String content) {
	        String hash = Hashing
	                    .sha256()
	                    .hashString(content, StandardCharsets.UTF_8)
	                    .toString();

	        return hash;

	    }

	

}
