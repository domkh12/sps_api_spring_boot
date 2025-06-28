package edu.npic.sps.features.file;

import edu.npic.sps.features.file.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileResponse> uploadMultipleFiles(List<MultipartFile> files);

    List<FileResponse> findAll();

    FileResponse uploadFile(MultipartFile file) throws IOException;
}
