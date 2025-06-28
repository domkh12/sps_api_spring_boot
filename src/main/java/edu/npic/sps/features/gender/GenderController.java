package edu.npic.sps.features.gender;

import edu.npic.sps.features.gender.dto.GenderRequest;
import edu.npic.sps.features.gender.dto.GenderResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genders")
@RequiredArgsConstructor
@Tag(name = "Gender Management", description = "APIs for managing genders")
public class GenderController {

    private final GenderService genderService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByUuid(@PathVariable String uuid){
        genderService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    GenderResponse updateByUuid(@PathVariable String uuid, @Valid @RequestBody GenderRequest genderRequest){
        return genderService.updateByUuid(uuid, genderRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    GenderResponse create(@Valid @RequestBody GenderRequest genderRequest){
        return genderService.create(genderRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<GenderResponse> findAll(){
        return genderService.findAll();
    }
}
