package edu.npic.sps.features.file;

import edu.npic.sps.features.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Management", description = "APIs for managing files")
public class FileController {

    private final FileService fileService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/multiples")
    List<FileResponse> uploadMultipleFiles(@RequestPart List<MultipartFile> files) throws IOException {
        return fileService.uploadMultipleFiles(files);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    FileResponse uploadFile(@RequestPart MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @GetMapping
    List<FileResponse> findAll() {
        return fileService.findAll();
    }

}
