package io.gomk.controller;
import java.util.List;
import java.util.Map;

import io.gomk.controller.response.Result;
import io.gomk.hdfs.HdfsService;
import io.gomk.hdfs.test.User;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.BlockLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
//@RestController
//@RequestMapping("/hdfs")
public class HdfsController {
    private static Logger LOGGER = LoggerFactory.getLogger(HdfsController.class);

    @Autowired
    HdfsService hdfsService;

    /**
     * 创建文件夹
     * @param path
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "mkdir", method = RequestMethod.POST)
    @ResponseBody
    public Result mkdir(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            LOGGER.debug("请求参数为空");
            return new Result(Result.FAILURE, "请求参数为空");
        }
        // 创建空文件夹
        boolean isOk = HdfsService.mkdir(path);
        if (isOk) {
            LOGGER.debug("文件夹创建成功");
            return new Result(Result.SUCCESS, "文件夹创建成功");
        } else {
            LOGGER.debug("文件夹创建失败");
            return new Result(Result.FAILURE, "文件夹创建失败");
        }
    }

    /**
     * 读取HDFS目录信息
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/readPathInfo")
    public Result readPathInfo(@RequestParam("path") String path) throws Exception {
        List<Map<String, Object>> list = HdfsService.readPathInfo(path);
        return new Result(Result.SUCCESS, "读取HDFS目录信息成功", list);
    }

    /**
     * 获取HDFS文件在集群中的位置
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/getFileBlockLocations")
    public Result getFileBlockLocations(@RequestParam("path") String path) throws Exception {
        BlockLocation[] blockLocations = HdfsService.getFileBlockLocations(path);
        return new Result(Result.SUCCESS, "获取HDFS文件在集群中的位置", blockLocations);
    }

    /**
     * 创建文件
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/createFile")
    public Result createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file)
            throws Exception {
        if (StringUtils.isEmpty(path) || null == file.getBytes()) {
            return new Result(Result.FAILURE, "请求参数为空");
        }
        HdfsService.createFile(path, file);
        return new Result(Result.SUCCESS, "创建文件成功");
    }

    /**
     * 读取HDFS文件内容
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/readFile")
    public Result readFile(@RequestParam("path") String path) throws Exception {
        String targetPath = HdfsService.readFile(path);
        return new Result(Result.SUCCESS, "读取HDFS文件内容", targetPath);
    }

    /**
     * 读取HDFS文件转换成Byte类型
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/openFileToBytes")
    public Result openFileToBytes(@RequestParam("path") String path) throws Exception {
        byte[] files = HdfsService.openFileToBytes(path);
        return new Result(Result.SUCCESS, "读取HDFS文件转换成Byte类型", files);
    }

    /**
     * 读取HDFS文件装换成User对象
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/openFileToUser")
    public Result openFileToUser(@RequestParam("path") String path) throws Exception {
        User user = HdfsService.openFileToObject(path, User.class);
        return new Result(Result.SUCCESS, "读取HDFS文件装换成User对象", user);
    }

    /**
     * 读取文件列表
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/listFile")
    public Result listFile(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return new Result(Result.FAILURE, "请求参数为空");
        }
        List<Map<String, String>> returnList = HdfsService.listFile(path);
        return new Result(Result.SUCCESS, "读取文件列表成功", returnList);
    }

    /**
     * 重命名文件
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
    @PostMapping("/renameFile")
    public Result renameFile(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName)
            throws Exception {
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return new Result(Result.FAILURE, "请求参数为空");
        }
        boolean isOk = HdfsService.renameFile(oldName, newName);
        if (isOk) {
            return new Result(Result.SUCCESS, "文件重命名成功");
        } else {
            return new Result(Result.FAILURE, "文件重命名失败");
        }
    }

    /**
     * 删除文件
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteFile")
    public Result deleteFile(@RequestParam("path") String path) throws Exception {
        boolean isOk = HdfsService.deleteFile(path);
        if (isOk) {
            return new Result(Result.SUCCESS, "delete file success");
        } else {
            return new Result(Result.FAILURE, "delete file fail");
        }
    }

    /**
     * 上传文件
     * @param path
     * @param uploadPath
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
            throws Exception {
        HdfsService.uploadFile(path, uploadPath);
        return new Result(Result.SUCCESS, "upload file success");
    }

    /**
     * 下载文件
     * @param path
     * @param downloadPath
     * @return
     * @throws Exception
     */
    @PostMapping("/downloadFile")
    public Result downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
            throws Exception {
        HdfsService.downloadFile(path, downloadPath);
        return new Result(Result.SUCCESS, "download file success");
    }

    /**
     * HDFS文件复制
     * @param sourcePath
     * @param targetPath
     * @return
     * @throws Exception
     */
    @PostMapping("/copyFile")
    public Result copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath)
            throws Exception {
        HdfsService.copyFile(sourcePath, targetPath);
        return new Result(Result.SUCCESS, "copy file success");
    }

    /**
     * 查看文件是否已存在
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/existFile")
    public Result existFile(@RequestParam("path") String path) throws Exception {
        boolean isExist = HdfsService.existFile(path);
        return new Result(Result.SUCCESS, "file isExist: " + isExist);
    }

}
