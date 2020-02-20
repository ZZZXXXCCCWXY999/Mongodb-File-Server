package xyz.zxcwxy999.mongodbfileserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.zxcwxy999.mongodbfileserver.domain.File;

public interface FileRepository extends MongoRepository<File,String> {
}
