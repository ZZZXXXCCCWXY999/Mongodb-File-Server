//package xyz.zxcwxy999.mongodbfileserver.service;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.gridfs.GridFS;
//import com.mongodb.gridfs.GridFSDBFile;
//import com.mongodb.gridfs.GridFSInputFile;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//import java.util.Date;
//
//@Service
//public class GridfsService {
//
//
//
//    @Autowired
//    private MongoDbFactory mongodbfactory;
//
//
//    /**
//     * 插入文件
//     * @param file
//     * @return
//     */
//    public GridFSInputFile save(MultipartFile file){
//
//        GridFS gridFS = new GridFS(mongodbfactory.getDb());
//
//        try{
//
//            InputStream in = file.getInputStream();
//
//            String name = file.getOriginalFilename();
//
//            GridFSInputFile gridFSInputFile = gridFS.createFile(in);
//
//            gridFSInputFile.setFilename(name);
//
//            gridFSInputFile.setContentType(file.getContentType());
//
//            gridFSInputFile.save();
//            return gridFSInputFile;
//        }
//        catch (Exception e){}
//
//        return null;
//
//    }
//
//    /**
//     * 据id返回文件
//     */
//    public GridFSDBFile getById(ObjectId id){
//
//        GridFS gridFS = new GridFS(mongodbfactory.getDb());
//
//        return gridFS.findOne(new BasicDBObject("_id", id));
//
//    }
//
//
//    /**
//     * 删除
//     * @param id
//     */
//    public void remove(String id) {
//        GridFS gridFS = new GridFS(mongodbfactory.getDb());
//        gridFS.remove(new ObjectId(id));
//    }
//
//}