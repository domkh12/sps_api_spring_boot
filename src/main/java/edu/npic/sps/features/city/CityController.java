package edu.npic.sps.features.city;

import edu.npic.sps.features.city.dto.CityRequest;
import edu.npic.sps.features.city.dto.CityResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Tag(name = "City Management", description = "APIs for managing cities")
public class CityController {

    private final CityService cityService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByUuid(@PathVariable String uuid){
        cityService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    CityResponse updateByUuid(@PathVariable String uuid, @Valid @RequestBody CityRequest cityRequest){
        return cityService.updateByUuid(uuid, cityRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CityResponse create(@Valid @RequestBody CityRequest cityRequest){
        return cityService.create(cityRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CityResponse> findAll(){
        return cityService.findAll();
    }

}
