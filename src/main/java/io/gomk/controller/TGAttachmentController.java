package io.gomk.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.controller.request.AttachmentRequest;
import io.gomk.model.entity.TGAttachment;
import io.gomk.service.TGAttachmentService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nick
 * @since 2019-11-10
 */
@RestController
@RequestMapping("/attachment")
@Api(description = "招标范本库、政策法规库上传下载")
public class TGAttachmentController {

    @Autowired
    private TGAttachmentService attachmentService;

    @ApiOperation(value = "招标范本库列表、政策法规库列表")
    @GetMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value="当前页", required=true, paramType="query", dataType="int", defaultValue="1"),
            @ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
            @ApiImplicitParam(name="attaType", value="6政策法规、7招标范本", required=true, paramType="query", dataType="int", defaultValue="6"),
            @ApiImplicitParam(name="attaName", value="模糊查询文件名", required=false, paramType="query", dataType="String", defaultValue="法律")
    })
    public ResponseData<IPage<TGAttachment>> attamentlist(int page, int pageSize, int attaType,String attaName) throws Exception {

        QueryWrapper<TGAttachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TGAttachment::getAttaType,attaType);
        queryWrapper.lambda().like(TGAttachment::getAttaName,attaName);

        // 当前页码，每页条数
        Page<TGAttachment> pageParam = new Page<>(page, pageSize);

        IPage<TGAttachment> pageResult = attachmentService.page(pageParam,queryWrapper);
        return  ResponseData.success(pageResult);
    }

    @ApiOperation("上传附件")
    @PostMapping()
    public ResponseData<?> add(@ApiParam(name="attaName",value="附件名称")@RequestParam(required = true) String attaName,
                               @ApiParam(name="attaDecs",value="附件描述")@RequestParam(required = false) String attaDecs,
                               @ApiParam(name="attaType",value="附件描述")@RequestParam(required = true) int attaType,
                               @ApiParam(name="attafile",value="附件")@RequestParam(required = true) MultipartFile attafile) throws Exception {

        attachmentService.uploadAtta(attaName,attaDecs,attaType,attafile);
        return ResponseData.success();
    }

    @ApiOperation("删除附件")
    @DeleteMapping()
    public ResponseData<?> delete(String  id) throws Exception {
        QueryWrapper<TGAttachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TGAttachment::getId,id);
        attachmentService.remove(queryWrapper);
        return ResponseData.success();
    }

    @ApiOperation(value = "根据主键下载附件", notes = "根据主键下载附件")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String downloadById(@ApiParam(name="id",value="主键")@RequestParam(required = true) String id,
                               HttpServletResponse response,HttpServletRequest request
                                        ) throws Exception{
        return attachmentService.downloadById(id,response,request);
    }
}

