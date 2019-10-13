package io.gomk.controller.response;

import java.util.Set;

import lombok.Data;

@Data
public class NumberVO {
	private String words;
	private Set<String> pkgCode;
	private Set<String> supplDocumentCode;
}
