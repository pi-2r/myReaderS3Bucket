package org.commoncrawl.myReaderS3Bucket;

import org.jets3t.service.S3ServiceException;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws S3ServiceException {
		
		final String bucket = "aws-publicdatasets";
		final String prefix = "common-crawl/crawl-data/CC-MAIN-2015-22";
		
		final String path = "/tmp/myReaderS3Bucket/";
		final String filename = "my_data.txt";
	
		final Integer nbFolder = 1; 
		
		System.out.println("Started");
		myReaderS3Bucket reader = new myReaderS3Bucket();
		AmazonS3Client s3 = new AmazonS3Client();
		
		System.out.println("Please wait...");
		reader.start(s3, bucket, prefix, path, filename, nbFolder);
		System.out.println("done !!!");
	}
}
