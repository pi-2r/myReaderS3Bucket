package org.commoncrawl.myReaderS3Bucket;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.json.JSONException;
import org.json.JSONObject;

public class myReaderS3Bucket {
	
	 public void start(AmazonS3 s3, String bucketName, String prefix, String path, String filename, Integer limit) throws S3ServiceException {
		int count = 0;
		
		saveData save = new saveData();
		save.createFolder(path, filename);
		String wat = "wat";

		
		S3Service s3s = new RestS3Service(null);
		ObjectListing list = s3.listObjects(bucketName, prefix);

		do {
			List<S3ObjectSummary> summaries = list.getObjectSummaries();
			for (S3ObjectSummary summary : summaries) {
				try {
					String key = summary.getKey();
					if (key.contains(wat)) {
						System.out.println("WAT format found: " + key);
						org.jets3t.service.model.S3Object f = s3s.getObject(bucketName, key, null, null, null, null,
								null, null);
						ArchiveReader ar = WARCReaderFactory.get(key, f.getDataInputStream(), true);

						for (ArchiveRecord r : ar) {
							if (!r.getHeader().getMimetype().equals("application/json")) {
								continue;
							}
							try {
								byte[] rawData = IOUtils.toByteArray(r, r.available());
								String content = new String(rawData);
								JSONObject json = new JSONObject(content);
								try {
									String server = json.getJSONObject("Envelope").getJSONObject("Payload-Metadata").getJSONObject("HTTP-Response-Metadata").getJSONObject("Headers")
											.getString("Server");
									//System.out.println("server: " + server);
									save.writeInFile(path, filename, server);
								} catch (JSONException ex) {
									//ex.printStackTrace();
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					continue;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (++count >= limit) return;
			}
			list = s3.listNextBatchOfObjects(list);
		} while (list.isTruncated());
	}
}
