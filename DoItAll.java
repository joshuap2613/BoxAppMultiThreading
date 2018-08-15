package com.multiconsumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.IAccessTokenCache;
import com.box.sdk.InMemoryLRUAccessTokenCache;

public class DoItAll {
	public static void main(String[] args) throws IOException, InterruptedException {
		Reader reader = new FileReader("/Users/joshua.prupes@ibm.com/Documents/config.json");
		File file = new File("outputFile.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		BoxConfig boxConfig = BoxConfig.readFrom(reader);
		
		// Set cache info
		int MAX_CACHE_ENTRIES = 1000;
		IAccessTokenCache accessTokenCache = new 
		  InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);

		// Create new app enterprise connection object
		BoxDeveloperEditionAPIConnection client = 
				BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(boxConfig, accessTokenCache);
		//access account of person with userId
		client.asUser("3742256249");
		BoxFolder root = new BoxFolder(client,"27583997032");
		
		Coach prupe = new Coach(root, client,10);
		prupe.traverse();
	}
}
