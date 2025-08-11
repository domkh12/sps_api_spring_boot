package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.domain.*;
import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.mapper.ParkingLotDetailMapper;
import edu.npic.sps.mapper.ParkingLotMapper;
import edu.npic.sps.mapper.VehicleMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingLotDetailServiceImpl implements ParkingLotDetailService {

    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final ParkingLotDetailMapper parkingLotDetailMapper;
    private final AuthUtil authUtil;

    @Override
    public Page<ParkingDetailResponse> filter(int pageNo, int pageSize, String keywords, LocalDateTime dateFrom, LocalDateTime dateTo) {
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must greater than 0!"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLotDetail> parkingLotDetails = Page.empty();

        if (isAdmin){
            if (dateFrom != null && dateTo != null){
                parkingLotDetails = parkingLotDetailRepository.filterParkingLotDetailWithDateRange(
                        keywords,
                        dateFrom,
                        dateTo,
                        pageRequest
                );
            }else{
                parkingLotDetails = parkingLotDetailRepository.filterParkingLotDetailWithKeywords(
                        keywords,
                        pageRequest
                );
            }
        }else if (isManager){
            if (dateFrom != null && dateTo != null){
                parkingLotDetails = parkingLotDetailRepository.filterParkingLotDetailWithDateRangeManager(
                        keywords,
                        dateFrom,
                        dateTo,
                        sites.getFirst(),
                        pageRequest
                );
            }else {
                parkingLotDetails = parkingLotDetailRepository.filterParkingLotDetailWithKeywordsManager(
                        keywords,
                        sites.getFirst(),
                        pageRequest
                );
            }
        }


        return parkingLotDetails.map(parkingLotDetailMapper::toParkingDetailResponse);
    }

    @Override
    public Page<ParkingDetailResponse> findAll(int pageNo, int pageSize) {
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number or page size must greater than 0!");
        }

        Page<ParkingLotDetail> parkingLotDetails = Page.empty();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        if (isAdmin){
            parkingLotDetails = parkingLotDetailRepository.findAll(pageRequest);
        }else if (isManager){
            parkingLotDetails = parkingLotDetailRepository.findBySite_Uuid(sites.getFirst(), pageRequest);
        }

        return parkingLotDetails.map(parkingLotDetailMapper::toParkingDetailResponse);
    }

    @Override
    public ParkingDetailResponse getParkingDetailByUuid(String uuid) {

        ParkingLotDetail parkingLotDetail = parkingLotDetailRepository.findByParkingLot_UuidAndIsParking(uuid, true).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking details not found!")
        );

        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);
    }
}
