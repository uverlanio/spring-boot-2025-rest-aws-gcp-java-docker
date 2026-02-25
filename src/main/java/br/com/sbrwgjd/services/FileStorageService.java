package br.com.sbrwgjd.services;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.controllers.*;
import br.com.sbrwgjd.exception.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath()
                .normalize();
        try {
            if (Files.notExists(this.fileStorageLocation)) {
                logger.info("Creating Directory: {}", this.fileStorageLocation);
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch (Exception e){
            logger.error("Could not create the directory where files will be stored!");
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                logger.error("Sorry! Filename Contains a Invalid path Sequence " + fileName);
                throw new FileStorageException("Sorry! Filename Contains a Invalid path Sequence " + fileName);
            }

            logger.info("Saving file in Disk");

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file " + fileName + ". Please try again!");
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!",e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                logger.error("File not found " + fileName);
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (Exception e) {
            logger.error("File not found " + fileName);
            throw new FileNotFoundException("File not found " + fileName, e);
        }
    }
}
