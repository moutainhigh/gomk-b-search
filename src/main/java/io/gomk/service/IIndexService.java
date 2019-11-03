package io.gomk.service;

import java.io.IOException;

import io.gomk.common.rs.response.ResponseData;


public interface IIndexService {

	public ResponseData<String> createZBIndex() throws IOException;
	public ResponseData<String> createZGYQIndex() throws IOException;
	public ResponseData<String> createPBBFIndex() throws IOException;
	public ResponseData<String> createJSYQIndex() throws IOException;
	public ResponseData<String> createZJIndex() throws IOException;
	public ResponseData<String> bulkZBDoc() throws IOException;
	public ResponseData<String> deleteIndex(String zbIndex);
	public ResponseData<String> bulkZGYQDoc() throws IOException;
	public ResponseData<String> bulkJSYQDoc() throws IOException;
	public ResponseData<String> bulkPBBFDoc() throws IOException;
	public ResponseData<String> bulkZJCGDoc() throws IOException;
	public ResponseData<String> createCompletionIndex() throws IOException;
	public ResponseData<String> BulkCompletionDoc() throws IOException;
	public ResponseData<String> createZCFGIndex() throws IOException;
	public ResponseData<String> bulkZCFGDoc() throws IOException;
	public ResponseData<String> createZBFBIndex() throws IOException;
	public ResponseData<String> bulkZBFBDoc() throws IOException;
	public ResponseData<String> bulkTBDoc() throws IOException;
}
