package xyz.zxcwxy999.fileserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xyz.zxcwxy999.fileserver.domain.File;


/**
 * File 存储库
 */
public interface FileRepository extends MongoRepository<File, String> {
}
