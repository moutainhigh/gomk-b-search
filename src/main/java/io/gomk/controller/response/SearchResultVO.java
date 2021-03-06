package io.gomk.controller.response;

import java.util.HashSet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchResultVO {
	
	@ApiModelProperty(value = "id")
    private String id;
	
	@ApiModelProperty(value = "标题")
    private String title;
	
	@ApiModelProperty(value = "正文")
	private String content;
	
	@ApiModelProperty(value = "入库时间")
	private String addDate;
	
	@ApiModelProperty(value = "标签")
	private HashSet<String> tags;
	
	@ApiModelProperty(value = "压缩文件目录结构")
	private String directoryTree ;
}
