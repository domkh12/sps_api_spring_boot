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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

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
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sites = authUtil.loggedUserSites();
        String userUuid = authUtil.loggedUserUuid();

        Integer totalUserCount = 0;
        Integer totalVehicleCount = 0;
        Integer totalParkingAreasCount = 0;
        Integer totalParkingSlotsCount = 0;
        Integer totalCompanies = 0;
        Integer activeSessionCount = 0;
        Integer checkInCount = 0;
        Integer totalBranches = 0;
        long avgStayTime = 0;
        Double occupancyRate = 0.0;
        List<Site> branches = siteRepository.findAll();
        List<AnalysisCompanyResponse> companyResponses = new ArrayList<>();
        List<AnalysisBranchResponse> branchResponses = new ArrayList<>();
        List<HourlyOccupancyResponse> hourlyOccupancyResponses = new ArrayList<>();
        List<WeeklyOccupancyResponse> weeklyOccupancyResponses = new ArrayList<>();
        List<ParkingAreasUtilizationResponse> parkingAreasUtilization = new ArrayList<>();
        List<ParkingAreasDetailsResponse> parkingAreasDetails = new ArrayList<>();

        if (isAdmin) {
            List<Company> companies = companyRepository.findAll();
            Long sumDurations = parkingLotDetailRepository.sumByIsCheckOutTrue();
            Long countCheckOutTrue = parkingLotDetailRepository.countByIsCheckOutTrue();
            // Calculate average stay time by take all sum duration / countCheckOutTrue
            if (sumDurations != null && countCheckOutTrue != null && countCheckOutTrue > 0) {
                avgStayTime = sumDurations / countCheckOutTrue;
            }
            totalUserCount = userRepository.countByUuidNot(userUuid);
            totalBranches = siteRepository.totalBranches();
            checkInCount = parkingLotDetailRepository.totalCheckIn();
            activeSessionCount = parkingLotRepository.countByIsAvailableTrue();
            totalVehicleCount = vehicleRepository.totalVehicleCount();
            totalCompanies = companyRepository.totalCompanies();
            totalParkingAreasCount = parkingSpaceRepository.totalParkingAreasCount();
            totalParkingSlotsCount = parkingLotRepository.totalParkingSlotsCount();

            // Calculate Rate of slot that occupied
            Integer occupiedSlots = parkingLotRepository.OccupiedSlots();

            if (occupiedSlots != null && totalParkingSlotsCount != null && totalParkingSlotsCount > 0) {
                occupancyRate = BigDecimal.valueOf((occupiedSlots * 100.0) / totalParkingSlotsCount)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }

            // Map company to AnalysisCompanyResponse
            companyResponses = companies.stream().map(
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

            branchResponses = branches.stream().map(
                    branch -> {

                        int totalSlots = branch.getParkingSpaces().stream().mapToInt(ParkingSpace::getLotQty).sum();
                        int sumOccupiedSlots = branch.getParkingSpaces().stream()
                                .mapToInt(space -> parkingLotRepository.countByParkingSpace_IdAndIsAvailableFalse(space.getId()))
                                .sum();

                        return AnalysisBranchResponse.builder()
                                .name(branch.getSiteName())
                                .areas(branch.getParkingSpaces().size())
                                .slots(totalSlots)
                                .occupied(sumOccupiedSlots)
                                .efficiency(totalSlots > 0 ? (int) ((double) sumOccupiedSlots / totalSlots * 100) : 0)
                                .build();
                    }
            ).toList();

            Integer totalSlot = parkingLotRepository.countFirstBy();
            hourlyOccupancyResponses = getHour().stream().map(
                    hour -> HourlyOccupancyResponse.builder()
                            .hour(formatHour(hour))
                            .occupied(parkingLotDetailRepository.countByTimeInBetween(hour, hour.plusHours(1)))
                            .total(totalSlot)
                            .build()
            ).toList();

            weeklyOccupancyResponses = getDays().stream().map(
                    day -> {
                        Long parkingDurationDaily = parkingLotDetailRepository.sumByIsCheckOutTrueAndTimeOutBetween(day, day.plusDays(1));
                        Long parkingCountDaily = parkingLotDetailRepository.countByIsCheckOutTrueAndTimeOutBetween(day, day.plusDays(1));
                        double avgDailyParking = 0.0;
                        if (parkingDurationDaily != null) {
                            avgDailyParking = BigDecimal.valueOf((parkingDurationDaily / 60.0) / (double) parkingCountDaily)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .doubleValue();
                        }

                        return WeeklyOccupancyResponse.builder()
                                .day(formatDay(day))
                                .occupancy(parkingLotDetailRepository.countByTimeInBetween(day, day.plusDays(1)))
                                .avgStayTime(avgDailyParking)
                                .build();
                    }
            ).toList();

        } else if (isManager) {
            Long sumDurations = parkingLotDetailRepository.sumLongByIsCheckInTrueAndSite_UuidIn(sites);
            Long countCheckOutTrue = parkingLotDetailRepository.countLongByIsCheckInTrueAndSite_UuidIn(sites);
            // Calculate average stay time by take all sum duration / countCheckOutTrue
            if (sumDurations != null && countCheckOutTrue != null && countCheckOutTrue > 0) {
                avgStayTime = sumDurations / countCheckOutTrue;
            }
            totalUserCount = userRepository.countUserBySite(userUuid, sites);
            checkInCount = parkingLotDetailRepository.countByIsCheckInTrueAndSite_UuidIn(sites);
            activeSessionCount = parkingLotRepository.countByIsAvailableTrueAndParkingSpace_Site_UuidIn(sites);
            totalParkingAreasCount = parkingSpaceRepository.countBySite_UuidIn(sites);
            totalParkingSlotsCount = parkingLotRepository.countByParkingSpace_Site_UuidIn(sites);

            Integer occupiedSlots = parkingLotRepository.OccupiedSlotsBySites(sites);

            if (occupiedSlots != null && totalParkingSlotsCount != null && totalParkingSlotsCount > 0) {
                occupancyRate = BigDecimal.valueOf((occupiedSlots * 100.0) / totalParkingSlotsCount)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }

            Integer totalSlot = parkingLotRepository.countByParkingSpace_Site_UuidIn(sites);
            hourlyOccupancyResponses = getHour().stream().map(
                    hour -> HourlyOccupancyResponse.builder()
                            .hour(formatHour(hour))
                            .occupied(parkingLotDetailRepository.countByTimeInBetweenAndSite_UuidIn(hour, hour.plusHours(1), sites))
                            .total(totalSlot)
                            .build()
            ).toList();

            weeklyOccupancyResponses = getDays().stream().map(
                    day -> {
                        Long parkingDurationDaily = parkingLotDetailRepository.sumByIsCheckOutTrueAndTimeOutBetweenAndSite_UuidIn(day, day.plusDays(1), sites);
                        Long parkingCountDaily = parkingLotDetailRepository.countByIsCheckOutTrueAndTimeOutBetweenAndSite_UuidIn(day, day.plusDays(1), sites);
                        double avgDailyParking = 0.0;
                        if (parkingDurationDaily != null) {
                            avgDailyParking = BigDecimal.valueOf((parkingDurationDaily / 60.0) / (double) parkingCountDaily)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .doubleValue();
                        }

                        return WeeklyOccupancyResponse.builder()
                                .day(formatDay(day))
                                .occupancy(parkingLotDetailRepository.countByTimeInBetweenAndSite_UuidIn(day, day.plusDays(1), sites))
                                .avgStayTime(avgDailyParking)
                                .build();
                    }
            ).toList();

            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findBySite_UuidIn(sites);
            parkingAreasUtilization = parkingSpaces.stream().map(space -> ParkingAreasUtilizationResponse.builder()
                    .name(space.getLabel())
                    .value(space.getLotQty())
                    .color(ColorUtil.generateUniqueColor(space.getLabel()))
                    .build()).toList();

            parkingAreasDetails = parkingSpaces.stream().map(space -> {
                // Calculate Rate of slot that occupied
                Integer occupiedSlotsBySpace = parkingLotRepository.OccupiedSlotsBySpace(space.getId());
                Integer totalParkingSlotsCountBySpace = parkingLotRepository.countByParkingSpace_Id(space.getId());

                Double occupancyRateBySpace = 0.0;
                if (occupiedSlotsBySpace != null && totalParkingSlotsCountBySpace != null && totalParkingSlotsCountBySpace > 0) {
                    occupancyRateBySpace = BigDecimal.valueOf((occupiedSlotsBySpace * 100.0) / totalParkingSlotsCountBySpace)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();
                }
                        return ParkingAreasDetailsResponse.builder()
                                .id(space.getId())
                                .name(space.getLabel())
                                .totalSlots(space.getLotQty())
                                .occupancyRate(occupancyRateBySpace)
                                .occupied(parkingLotRepository.countByParkingSpace_IdAndIsAvailableFalse(space.getId()))
                                .available(parkingLotRepository.countByIsAvailableTrueAndParkingSpace_Id(space.getId()))
                                .barColor(ColorUtil.generateUniqueColor(space.getLabel()))
                                .build();
                    }
            ).toList();
        }

        if (isAdmin) {
            return AnalysisResponse.builder()
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
                    .hourlyData(hourlyOccupancyResponses)
                    .weeklyData(weeklyOccupancyResponses)
                    .build();
        }

        if (isManager) {
            return AnalysisResponse.builder()
                    .totalStats(TotalStatResponse.builder()
                            .totalUserCount(totalUserCount)
                            .totalVehicleCount(totalVehicleCount)
                            .totalParkingAreasCount(totalParkingAreasCount)
                            .totalParkingSlotsCount(totalParkingSlotsCount)
                            .activeSessionCount(activeSessionCount)
                            .checkInCount(checkInCount)
                            .occupancyRate(occupancyRate)
                            .averageStayTime((double) avgStayTime)
                            .build())
                    .parkingAreasUtilization(parkingAreasUtilization)
                    .parkingAreasDetails(parkingAreasDetails)
                    .hourlyData(hourlyOccupancyResponses)
                    .weeklyData(weeklyOccupancyResponses)
                    .build();
        }
        return null;
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

    private String formatHour(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ha");
        return dateTime.format(formatter);
    }

    private String formatDay(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E");
        return dateTime.format(formatter);
    }

    private List<LocalDateTime> getDays() {
        List<LocalDateTime> days = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime currentDay = now.truncatedTo(ChronoUnit.DAYS).minusDays(6);
        LocalDateTime endDay = now.truncatedTo(ChronoUnit.DAYS);

        // Generate 7 day ending with the current day
        while (currentDay.isBefore(endDay) || currentDay.equals(endDay)) {

            days.add(currentDay);
            currentDay = currentDay.plusDays(1);
        }
        return days;
    }

    private List<LocalDateTime> getHour() {
        List<LocalDateTime> hours = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime currentHour = now.truncatedTo(ChronoUnit.HOURS).minusHours(11);
        LocalDateTime endHour = now.truncatedTo(ChronoUnit.HOURS);

        // Generate 24 hours ending with the current hour
        while (currentHour.isBefore(endHour) || currentHour.equals(endHour)) {

            hours.add(currentHour);
            currentHour = currentHour.plusHours(1);
        }

        return hours;
    }

}
