package edu.npic.sps.features.analysis;

import edu.npic.sps.domain.Company;
import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.domain.Site;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.analysis.dto.*;
import edu.npic.sps.features.company.CompanyRepository;
import edu.npic.sps.features.parkingLot.ParkingLotRepository;
import edu.npic.sps.features.parkingLotDetail.ParkingLotDetailRepository;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.util.AuthUtil;
import edu.npic.sps.util.ColorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final CompanyRepository companyRepository;
    private final SiteRepository siteRepository;
    private final ParkingLotDetailRepository parkingLotDetailRepository;

    @Override
    public AnalysisResponse getAnalysis() {

        Integer totalUserCount = userRepository.totalUserCount();
        Integer totalVehicleCount = vehicleRepository.totalVehicleCount();
        Integer totalParkingAreasCount = parkingSpaceRepository.totalParkingAreasCount();
        Integer totalParkingSlotsCount = parkingLotRepository.totalParkingSlotsCount();
        Integer totalCompanies = companyRepository.totalCompanies();
        Integer totalBranches = siteRepository.totalBranches();
        Long sumDurations = parkingLotDetailRepository.sumByIsCheckOutTrue();
        Long countCheckOutTrue = parkingLotDetailRepository.countByIsCheckOutTrue();

        List<Company> companies = companyRepository.findAll();
        List<Site> branches = siteRepository.findAll();

        // Calculate average stay time by take all sum duration / countCheckOutTrue
        long avgStayTime = 0;
        if (sumDurations != null && countCheckOutTrue != null && countCheckOutTrue > 0) {
            avgStayTime = sumDurations / countCheckOutTrue;
        }

        // Calculate Rate of slot that occupied
        Integer occupiedSlots = parkingLotRepository.OccupiedSlots();
        Double occupancyRate = 0.0;
        if (occupiedSlots != null && totalParkingSlotsCount != null && totalParkingSlotsCount > 0) {
            occupancyRate = BigDecimal.valueOf((occupiedSlots * 100.0) / totalParkingSlotsCount)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        // Map company to AnalysisCompanyResponse
        List<AnalysisCompanyResponse> companyResponses = companies.stream().map(
                (company) -> {
                    // Get all parking space IDs for this company
                    List<Integer> parkingSpaceIds = company.getSites()
                            .stream()
                            .flatMap(site -> site.getParkingSpaces().stream())
                            .map(ParkingSpace::getId)
                            .toList();

                    // Calculate company average stay time correctly
                    Long companyTotalDuration = 0L;
                    Long companyTotalCheckouts = 0L;

                    for (Integer spaceId : parkingSpaceIds) {
                        Long spaceDuration = parkingLotDetailRepository.sumByParkingSpace_IdAndIsCheckOutTrue(spaceId);
                        Long spaceCheckouts = parkingLotDetailRepository.countByParkingSpace_IdAndIsCheckOutTrue(spaceId);

                        if (spaceDuration != null) companyTotalDuration += spaceDuration;
                        if (spaceCheckouts != null) companyTotalCheckouts += spaceCheckouts;
                    }

                    // Calculate average (avoid division by zero)
                    double companyAvgStayTime = companyTotalCheckouts > 0 ?
                            (double) companyTotalDuration / companyTotalCheckouts : 0.0;

                    return AnalysisCompanyResponse.builder()
                            .id(company.getId())
                            .name(company.getCompanyName())
                            .branches(siteRepository.countByCompany_Id(company.getId()))
                            .totalAreas(company
                                    .getSites()
                                    .stream()
                                    .mapToInt(site -> parkingSpaceRepository.countBySite_Id(site.getId()))
                                    .sum()
                            )
                            .totalSlots(company
                                    .getSites()
                                    .stream().mapToInt(
                                            site -> site
                                                    .getParkingSpaces()
                                                    .stream()
                                                    .mapToInt(space -> parkingLotRepository.countByParkingSpace_Id(space.getId()))
                                                    .sum()
                                    ).sum()
                            )
                            .occupiedSlots(company
                                    .getSites()
                                    .stream().mapToInt(
                                            site -> site
                                                    .getParkingSpaces()
                                                    .stream()
                                                    .mapToInt(space -> parkingLotRepository.countByParkingSpace_IdAndIsAvailableFalse(space.getId()))
                                                    .sum()
                                    ).sum()
                            )
                            .avgStayTime(companyAvgStayTime)
                            .color(ColorUtil.generateUniqueColor(company.getCompanyName()))
                            .build();
                }
        ).toList();

        // Map Branches to AnalysisBranchResponse
        List<AnalysisBranchResponse> branchResponses = branches.stream().map(
                branch -> {

                    int totalSlots = branch.getParkingSpaces().stream().mapToInt(ParkingSpace::getLotQty).sum();
                    int sumOccupiedSlots = branch.getParkingSpaces().stream()
                            .mapToInt(space -> parkingLotRepository.countByParkingSpace_IdAndIsAvailableFalse(space.getId()))
                            .sum();

                    return  AnalysisBranchResponse.builder()
                          .name(branch.getSiteName())
                          .areas(branch.getParkingSpaces().size())
                          .slots(totalSlots)
                          .occupied(sumOccupiedSlots)
                          .efficiency( totalSlots > 0 ? (int)((double)sumOccupiedSlots / totalSlots * 100) : 0)
                          .build();
                }
        ).toList();


        return  AnalysisResponse.builder()
                .totalStats(TotalStatResponse.builder()
                        .totalUserCount(totalUserCount)
                        .totalVehicleCount(totalVehicleCount)
                        .totalParkingAreasCount(totalParkingAreasCount)
                        .totalParkingSlotsCount(totalParkingSlotsCount)
                        .totalCompanies(totalCompanies)
                        .totalBranches(totalBranches)
                        .occupancyRate(occupancyRate)
                        .averageStayTime((double) avgStayTime)
                        .build())
                .companies(companyResponses)
                .branchData(branchResponses)
                .build();
    }

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
