package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.domain.VehicleType;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.features.vehicletype.VehicleTypeRepository;
import edu.npic.sps.mapper.UserMapper;
import edu.npic.sps.mapper.VehicleMapper;
import edu.npic.sps.mapper.VehicleTypeMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final UserMapper userMapper;
    private final VehicleTypeMapper vehicleTypeMapper;
    private final AuthUtil authUtil;

    @Override
    public VehicleResponse update(String uuid, VehicleRequest vehicleRequest) {


        Vehicle vehicle  = vehicleRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        User user = userRepository.findByUuid(vehicleRequest.userId()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        VehicleType vehicleType = vehicleTypeRepository.findByUuid(vehicleRequest.vehicleTypeId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VehicleType Not Found")
        );

        if(vehicleRepository.existsByNumberPlate(vehicleRequest.numberPlate()) && !vehicle.getNumberPlate().equals(vehicleRequest.numberPlate())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Vehicle number plate already exists"
            );
        }

        vehicleMapper.fromVehicleRequest(vehicleRequest,vehicle);

//        vehicle.setUser(user);
        vehicle.setVehicleType(vehicleType);
        vehicleRepository.save(vehicle);

//        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
//                .firstName(vehicle.getUser().getFirstName())
//                .lastName(vehicle.getUser().getLastName())
//                .gender(userMapper.toGenderReponse(vehicle.getUser().getGender()))
//                .dateOfBirth(vehicle.getUser().getDateOfBirth())
//                .uuid(vehicle.getUser().getUuid())
//                .fullName(vehicle.getUser().getFullName())
//                .email(vehicle.getUser().getEmail())
//                .createdAt(vehicle.getUser().getCreatedAt())
//                .phoneNumber(vehicle.getUser().getPhoneNumber())
//                .roleNames(vehicle.getUser().getRoles().stream().map(Role::getName).toList())
//                .status(vehicle.getUser().getStatus())
//                .profileImage(vehicle.getUser().getProfileImage())
//                .build();
//
//        return VehicleResponse.builder()
//                .uuid(vehicle.getUuid())
//                .numberPlate(vehicle.getNumberPlate())
//                .licensePlateKhName(vehicle.getLicensePlateKhName())
//                .licensePlateEngName(vehicle.getLicensePlateEngName())
//                .vehicleMake(vehicle.getVehicleMake())
//                .vehicleModel(vehicle.getVehicleModel())
//                .color(vehicle.getColor())
//                .image(vehicle.getImage())
//                .vehicleType(vehicleTypeMapper.toVehicleTypeResponse(vehicle.getVehicleType()))
//                .createdAt(vehicle.getCreatedAt())
//                .user(userDetailResponse)
//                .build();
        return null;
    }

    @Override
    public VehicleResponse create(CreateVehicle createVehicle) {

        if (vehicleRepository.existsByNumberPlate(createVehicle.numberPlate())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Vehicle number plate already exits");
        }

        User user = userRepository.findByUuid(createVehicle.userId()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        VehicleType vehicleType = vehicleTypeRepository.findByUuid(createVehicle.vehicleTypeId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VehicleType Not Found")
        );

        Vehicle vehicle = vehicleMapper.fromCreateVehicle(createVehicle);
        vehicle.setUuid(UUID.randomUUID().toString());
        vehicle.setIsDeleted(false);
        vehicle.setCreatedAt(LocalDateTime.now());
//        vehicle.setUser(user);
        vehicle.setVehicleType(vehicleType);
        vehicleRepository.save(vehicle);

//        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
//                .firstName(vehicle.getUser().getFirstName())
//                .lastName(vehicle.getUser().getLastName())
//                .gender(userMapper.toGenderReponse(vehicle.getUser().getGender()))
//                .dateOfBirth(vehicle.getUser().getDateOfBirth())
//                .uuid(vehicle.getUser().getUuid())
//                .fullName(vehicle.getUser().getFullName())
//                .email(vehicle.getUser().getEmail())
//                .createdAt(vehicle.getUser().getCreatedAt())
//                .phoneNumber(vehicle.getUser().getPhoneNumber())
//                .roleNames(vehicle.getUser().getRoles().stream().map(Role::getName).toList())
//                .status(vehicle.getUser().getStatus())
//                .profileImage(vehicle.getUser().getProfileImage())
//                .build();
//
//        return VehicleResponse.builder()
//                .uuid(vehicle.getUuid())
//                .numberPlate(vehicle.getNumberPlate())
//                .licensePlateKhName(vehicle.getLicensePlateKhName())
//                .licensePlateEngName(vehicle.getLicensePlateEngName())
//                .vehicleMake(vehicle.getVehicleMake())
//                .vehicleModel(vehicle.getVehicleModel())
//                .color(vehicle.getColor())
//                .image(vehicle.getImage())
//                .vehicleType(vehicleTypeMapper.toVehicleTypeResponse(vehicle.getVehicleType()))
//                .createdAt(vehicle.getCreatedAt())
//                .user(userDetailResponse)
//                .build();
        return null;
    }

    @Override
    public VehicleResponse findByNumPlate(String numberPlate) {

        Vehicle vehicle = vehicleRepository.findByNumberPlate(numberPlate).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "NumberPlate not found!"
                )
        );
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public Page<VehicleResponse> findAll(int pageNo, int pageSize) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0!"
            );
        }

        if (pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 0!"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Vehicle> vehicles = Page.empty();

        if(isManager){
            vehicles = vehicleRepository.findAll(pageRequest);
        }else if(isAdmin){
            vehicles = vehicleRepository.findBySites_Uuid(sites.stream().findFirst().orElseThrow(), pageRequest);
        }

        return vehicles.map(vehicleMapper::toVehicleResponse);
    }
}
