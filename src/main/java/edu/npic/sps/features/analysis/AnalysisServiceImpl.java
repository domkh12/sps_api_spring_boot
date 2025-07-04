package edu.npic.sps.features.analysis;

import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.analysis.dto.TotalCountResponse;
import edu.npic.sps.features.parkingLot.ParkingLotRepository;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    public TotalCountResponse totalCount() {

        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        long totalUserCount = 0;
        long totalVehicleCount = 0;
        long totalParkingSpaceCount = 0;
        long totalParkingLotCount = 0;
        long totalCompanyCount = 0;
        long totalSiteCount = 0;

        HashMap<String, List<String>> chartData = new HashMap<>();
        Sort sortUserByCreatedAd = Sort.by(Sort.Direction.ASC, "createdAt");
        if (isAdmin) {

            List<User> users = userRepository.findAll(sortUserByCreatedAd);
            totalUserCount = userRepository.count();
            totalVehicleCount = vehicleRepository.count();
            totalParkingSpaceCount = parkingSpaceRepository.count();
            totalParkingLotCount = parkingLotRepository.count();
            totalCompanyCount = parkingLotRepository.count();
            totalSiteCount = parkingLotRepository.count();

            Map<String, Long> userCountsByDate = users.stream()
                    .collect(Collectors.groupingBy(
                            user -> user.getCreatedAt().toLocalDate().toString(),
                            Collectors.counting()
                    ));

            List<String> dates = new ArrayList<>(userCountsByDate.keySet());
            List<String> values = dates.stream()
                    .map(date -> String.valueOf(userCountsByDate.get(date)))
                    .collect(Collectors.toList());

            chartData.put("date", dates);
            chartData.put("value", values);

            return TotalCountResponse.builder()
                    .totalUserCount(totalUserCount)
                    .totalVehicleCount(totalVehicleCount)
                    .totalParkingSpaceCount(totalParkingSpaceCount)
                    .totalParkingLotCount(totalParkingLotCount)
                    .totalCompanyCount(totalCompanyCount)
                    .totalSiteCount(totalSiteCount)
                    .chartData(chartData)
                    .build();
        } else if (isManager) {
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findBySiteUuid(sites.stream().findFirst().orElseThrow());
            totalUserCount = userRepository.countBySites_Uuid(sites.stream().findFirst().orElseThrow());
            totalVehicleCount = vehicleRepository.countBySites_Uuid(sites.stream().findFirst().orElseThrow());
            totalParkingSpaceCount = parkingSpaceRepository.countBySite_Uuid(sites.stream().findFirst().orElseThrow());
            totalParkingLotCount = parkingSpaces.stream().mapToLong(
                    parkingSpace -> parkingLotRepository.countByParkingSpace_Uuid(parkingSpace.getUuid())
            ).sum();
            return TotalCountResponse.builder()
                    .totalUserCount(totalUserCount)
                    .totalVehicleCount(totalVehicleCount)
                    .totalParkingSpaceCount(totalParkingSpaceCount)
                    .totalParkingLotCount(totalParkingLotCount)
                    .chartData(chartData)
                    .build();
        }

    return null;
    }
}
