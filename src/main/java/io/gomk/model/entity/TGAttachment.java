package io.gomk.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author nick
 * @since 2019-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TGAttachment对象", description="")
public class TGAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("atta_name")
    private String attaName;

    @TableField("atta_decs")
    private String attaDecs;

    @TableField("atta_type")
    private Integer attaType;

    @TableField("ftp_type")
    private String ftpType;

    @TableField("ftp_path")
    private String ftpPath;

    @TableField("created_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdTime;


}
