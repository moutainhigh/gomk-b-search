package io.gomk.framework.utils.tree;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TreeDto {
	/**
	   * 当前节点id
	   */
	  private String id;

	  /**
	   * 父节点id
	   */
	  private String parentId;

	  /**
	   * 名称
	   */
	  private String name;

	  private List<TreeDto> childsList = new ArrayList<TreeDto>();
	
}
