package io.gomk.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author nick
 * @date 2019/11/10 11:58
 */
@Data
public class AttachmentRequest {

    @ApiModelProperty("附件名称")
    private String attaName;
    @ApiModelProperty("附件描述")
    private String attaDecs;
    @ApiModelProperty("附件类型，6政策法规 7招标范本")
    private Integer attaType;
}
