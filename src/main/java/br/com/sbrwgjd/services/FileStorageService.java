package br.com.sbrwgjd.services;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.controllers.*;
import br.com.sbrwgjd.exception.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    public FileStorageService(Path fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
    }

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

        this.fileStorageLocation = path;
        try {
            log.info("Creating Directories");
            Files.createDirectory(this.fileStorageLocation);
        } catch (Exception e){
            log.error("Could not create the directory where files will be stored!");
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                log.error("Sorry! Filename Contains a Invalid path Sequence " + fileName);
                throw new FileStorageException("Sorry! Filename Contains a Invalid path Sequence " + fileName);
            }

            log.info("CSaving file in Disk");

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            log.error("Could not store file " + fileName + ". Please try again!");
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!",e);
        }
    }
}
