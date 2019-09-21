package io.gomk.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchResultVO {
	
	@ApiModelProperty(value = "标题")
    private String title;
	
	@ApiModelProperty(value = "正文")
	private String content;
	
	@ApiModelProperty(value = "资格要求")
	private String zgyqInfo;
	
	@ApiModelProperty(value = "招标范围")
	private String zbfwInfo;
	
	@ApiModelProperty(value = "入库时间")
	private String addDate;
	
	@ApiModelProperty(value = "标签")
	private String tag;
	
	@ApiModelProperty(value = "文件路径")
	private String fileUrl;
	
	
}
