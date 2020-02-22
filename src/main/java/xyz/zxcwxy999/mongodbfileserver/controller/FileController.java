package xyz.zxcwxy999.mongodbfileserver.controller;


import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.zxcwxy999.mongodbfileserver.Util.MD5Util;
import xyz.zxcwxy999.mongodbfileserver.domain.File;
import xyz.zxcwxy999.mongodbfileserver.service.FileService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)//允许所有域名访问
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/")
    public String index(Model model) {
        //展示最新20条数据
        List<File> files = fileService.listFilesByPage(0, 20);
        model.addAttribute("files", files);
        return "index";
    }

    /**
     * 分类查询文件
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("files/{pageIndex}/{pageSize}")
    @ResponseBody
    public List<File> listFilesByPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 获取文件片信息
     *
     * @param id
     * @return
     */
    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id) {
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()) {
            String s= null;
            try {
                s = URLEncoder.encode(file.get().getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=\"" + s + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                        .header("Connection", "close")
                        .body(file.get().getContent().getData());

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }
    }

    /**
     * 在线显示文件
     *
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.get().getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.get().getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .header("Connection", "close")
                    .body(file.get().getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }

    }

    /**
     * 上传
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(),new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            fileService.saveFile(f);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message","Your "+file.getOriginalFilename()+" is wrong!");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("message","You successfully uploaded "+file.getOriginalFilename()+"!");
        return "redirect:/";
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file){
        File returnFile=null;
        try {
            File f=new File(file.getOriginalFilename(),file.getContentType(),file.getSize(),new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            returnFile=fileService.saveFile(f);
            String path="//"+serverAddress+":"+serverPort+"/view/"+returnFile.getId();
            return ResponseEntity.status(HttpStatus.OK).body(path);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@PathVariable String id){
        try {
            fileService.removeFile(id);
            return ResponseEntity.status(HttpStatus.OK).body("Delete Succcess!!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
