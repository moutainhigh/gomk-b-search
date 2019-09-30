package io.gomk.service;

import java.io.IOException;

import io.gomk.common.rs.response.ResponseData;


public interface IIndexService {

	public ResponseData<String> createZBIndex() throws IOException;
	public ResponseData<?> createZGYQIndex() throws IOException;
	public ResponseData<?> createPBBFIndex() throws IOException;
	public ResponseData<?> createJSYQIndex() throws IOException;
	public ResponseData<?> createZJIndex() throws IOException;
	public ResponseData<String> bulkZBDoc() throws IOException;
}
