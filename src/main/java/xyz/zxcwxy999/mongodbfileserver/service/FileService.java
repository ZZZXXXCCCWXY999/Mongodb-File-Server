package xyz.zxcwxy999.mongodbfileserver.service;

import xyz.zxcwxy999.mongodbfileserver.domain.File;

import java.util.List;
import java.util.Optional;

public interface FileService {

    /**
     * 保存文件
     * @param file
     * @return
     */
    File saveFile(File file);

    /**
     * 删除文件
     * @param id
     */
    void removeFile(String id);

    /**
     * 根据id获取文件
     * @param id
     * @return
     */
    Optional<File> getFileById(String id);

    /**
     * 分页查询，按照上传时间排序
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<File> listFilesByPage(int pageIndex,int pageSize);
}
