package com.example.demo.controller;

import com.example.demo.exception.BusinessException;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
    import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp",
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            ".txt", ".csv",
            ".mp3", ".mp4", ".avi", ".mov", ".wav",
            ".zip", ".rar", ".7z"
    );

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @RequestMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(400, "上传的文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BusinessException(400, "文件名格式不正确");
        }

        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(suffix)) {
            throw new BusinessException(400, "不支持的文件类型: " + suffix);
        }

        String fileName = UUID.randomUUID() + suffix;

        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new BusinessException("创建上传目录失败: " + e.getMessage());
            }
        }

        try {
            file.transferTo(dir.resolve(fileName).toFile());
            return ResultUntils.success("/uploads/" + fileName);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

}