package org.example.propertyservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.propertyservice.config.SecurityUtils;
import org.example.propertyservice.constant.ErrorCodes;
import org.example.propertyservice.domain.dto.PropertyAddressDTO;
import org.example.propertyservice.domain.dto.PropertyDTO;
import org.example.propertyservice.domain.dto.PropertyFilterDTO;
import org.example.propertyservice.domain.dto.PropertyPage;
import org.example.propertyservice.domain.dto.base.ImageUploadResponse;
import org.example.propertyservice.domain.dto.client.UserDTO;
import org.example.propertyservice.domain.entity.PropertyEntity;
import org.example.propertyservice.domain.entity.PropertyAddressEntity;
import org.example.propertyservice.domain.entity.PropertyMediaEntity;
import org.example.propertyservice.domain.enums.MediaType;
import org.example.propertyservice.domain.enums.PropertyStatus;
import org.example.propertyservice.exception.AppException;
import org.example.propertyservice.mapper.PropertiesMapper;
import org.example.propertyservice.mapper.PropertyAddressMapper;
import org.example.propertyservice.proxy.UserProxy;
import org.example.propertyservice.repository.PropertyRepository;
import org.example.propertyservice.repository.PropertyAddressRepository;
import org.example.propertyservice.repository.PropertyMediaRepository;
import org.example.propertyservice.repository.specs.PropertyAddressSpecification;
import org.example.propertyservice.repository.specs.PropertySpecification;
import org.example.propertyservice.repository.specs.SpecificationUtils;
import org.example.propertyservice.service.PropertiesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class PropertiesServiceImpl implements PropertiesService {
    private final PropertyRepository propertyRepository;
    private final PropertiesMapper propertiesMapper;
    private final PropertyMediaRepository propertyMediaRepository;
    private final PropertyAddressMapper propertyAddressMapper;
    private final PropertyAddressRepository propertyAddressRepository;
    private final UserProxy userProxy;

    @Override
    @Transactional
    public Long create(PropertyDTO propertyDTO) {
        if (SecurityUtils.hasRole("ROLE_USER")) {
            throw new AppException(ErrorCodes.ERROR_007);
        }
        PropertyEntity propertyEntity = propertiesMapper.toEnity(propertyDTO);
        Long userId = SecurityUtils.getCurrentUserId().orElseThrow(() -> new AppException(ErrorCodes.ERROR_003));
        propertyEntity.setUserId(userId);
        propertyRepository.save(propertyEntity);
        saveEntity(propertyDTO, propertyEntity.getId());
        return propertyEntity.getId();
    }

    public void saveEntity(PropertyDTO propertyDTO, Long propertyId) {
        if (Objects.nonNull(propertyDTO.getMedias())) {
            List<PropertyMediaEntity> propertyMediaEntityList = new ArrayList<>();
            for (ImageUploadResponse response : propertyDTO.getMedias()) {
                propertyMediaEntityList.add(PropertyMediaEntity.builder()
                        .propertyId(propertyId)
                        .mediaUrl(response.getUrl())
                        .fileId(response.getFileId())
                        .mediaType(MediaType.IMAGE)
                        .build());
            }
            propertyMediaRepository.saveAll(propertyMediaEntityList);
        }
        if (Objects.nonNull(propertyDTO.getAddress())) {
            PropertyAddressEntity addressEntity = propertyAddressMapper.toEnity(propertyDTO.getAddress());
            addressEntity.setPropertyId(propertyId);
            propertyAddressRepository.save(addressEntity);
        }
    }

    @Override
    @Transactional
    public Long update(PropertyDTO propertyDTO) {
        PropertyEntity propertyEntity = propertyRepository.findById(propertyDTO.getId()).orElseThrow(() -> new AppException(ErrorCodes.ERROR_004));
        propertiesMapper.updateEntity(propertyDTO, propertyEntity);
        propertyRepository.save(propertyEntity);
        propertyMediaRepository.deleteAllByPropertyId(propertyDTO.getId());
        propertyAddressRepository.deleteAllByPropertyId(propertyDTO.getId());
        saveEntity(propertyDTO, propertyEntity.getId());
        return propertyEntity.getId();
    }

    @Override
    public PropertyDTO getProperty(Long id) {
        PropertyEntity propertyEntity = propertyRepository.findById(id).orElseThrow(() -> new AppException(ErrorCodes.ERROR_004));
        PropertyDTO propertyDTO = propertiesMapper.toDTO(propertyEntity);
        propertyDTO.setUser(userProxy.getUser(propertyEntity.getUserId()));
        List<PropertyMediaEntity> propertyMedias = propertyMediaRepository.findAllByPropertyId(propertyEntity.getId());
        List<ImageUploadResponse> medias = new ArrayList<>();
        for (PropertyMediaEntity media : propertyMedias) {
            medias.add(ImageUploadResponse.builder()
                    .url(media.getMediaUrl())
                    .fileId(media.getFileId())
                    .build());
        }
        propertyDTO.setMedias(medias);
        PropertyAddressDTO propertyAddressDTO = propertyAddressMapper.toDTO(propertyAddressRepository.findByPropertyId(id));
        propertyDTO.setAddress(propertyAddressDTO);
        return propertyDTO;
    }

    @Override
    public Page<PropertyPage> getList(PropertyFilterDTO dto, Pageable pageable) {
        // 1. Khởi tạo Spec
        Specification<PropertyEntity> spec = Specification.where(null);

        // --- GIAI ĐOẠN 1: XỬ LÝ LỌC ĐỊA CHỈ & BẢN ĐỒ ---
        // Kiểm tra xem user có filter theo địa chỉ hoặc bản đồ không
        boolean hasAddressFilter = (dto.getProvinceName() != null || dto.getDistrictName() != null
                || dto.getWardName() != null || dto.getStreetName() != null || dto.getMinLat() != null);
        if (hasAddressFilter) {
            Specification<PropertyAddressEntity> addressSpec = Specification.where(null);
            // 1.1. Các filter hành chính
            addressSpec = SpecificationUtils.addIfHasText(addressSpec, dto.getProvinceName(), PropertyAddressSpecification::hasProvinceName);
            addressSpec = SpecificationUtils.addIfHasText(addressSpec, dto.getDistrictName(), PropertyAddressSpecification::hasDistrictName);
            addressSpec = SpecificationUtils.addIfHasText(addressSpec, dto.getWardName(), PropertyAddressSpecification::hasWardName);
            addressSpec = SpecificationUtils.addIfHasText(addressSpec, dto.getStreetName(), PropertyAddressSpecification::hasStreetName);

            // 1.2. Thêm filter bản đồ (Viewport) - PHẦN BẠN CÒN THIẾU
            if (dto.getMinLat() != null) {
                addressSpec = addressSpec.and(PropertyAddressSpecification.withinViewport(
                        dto.getMinLat(), dto.getMaxLat(),
                        dto.getMinLng(), dto.getMaxLng()
                ));
            }

            List<PropertyAddressEntity> addresses = propertyAddressRepository.findAll(addressSpec);
            if (addresses.isEmpty()) {
                return Page.empty(pageable);
            }

            Set<Long> addressPropertyIds = addresses.stream()
                    .map(PropertyAddressEntity::getPropertyId)
                    .collect(Collectors.toSet());

            if (dto.getIds() == null) {
                dto.setIds(addressPropertyIds);
            } else {
                // Nếu DTO đã có sẵn list ID cần tìm, thì ta lấy GIAO (Retain) với list ID tìm thấy ở địa chỉ
                // Ví dụ: User gửi list [1, 2, 3] nhưng chỉ có [2, 3] nằm ở Hà Nội -> Lấy [2, 3]
                dto.getIds().retainAll(addressPropertyIds);
                if (dto.getIds().isEmpty()) {
                    return Page.empty(pageable);
                }
            }
        }

        if (Boolean.TRUE.equals(dto.getIsMyPosts())) {
            // lấy bài của chính mình
            dto.setUserId(SecurityUtils.getCurrentUserId().orElseThrow(() -> new AppException(ErrorCodes.ERROR_003)));
        }
        // 2. Nhóm Filter Property (Dùng Utils)
        spec = SpecificationUtils.addIfHasText(spec, dto.getSearch(), PropertySpecification::hasSearch);
        spec = SpecificationUtils.addIfNotNull(spec, dto.getUserId(), PropertySpecification::hasUserId);
        spec = SpecificationUtils.addIfNotEmpty(spec, dto.getIds(), PropertySpecification::hasIds);

        spec = SpecificationUtils.addIfNotNull(spec, dto.getTransactionType(), PropertySpecification::hasTransactionType);
        spec = SpecificationUtils.addIfNotNull(spec, dto.getPropertyType(), PropertySpecification::hasPropertyType);
        spec = SpecificationUtils.addIfNotNull(spec, dto.getDirection(), PropertySpecification::hasDirection);
        spec = SpecificationUtils.addIfNotNull(spec, dto.getLegalStatus(), PropertySpecification::hasLegalStatus);
        spec = SpecificationUtils.addIfNotNull(spec, dto.getPriceNegotiable(), PropertySpecification::hasPriceNegotiable);

        // 3. Nhóm Filter Phức tạp (Price & Area - không dùng Utils được vì nhận 2 tham số)
        if (dto.getPriceMin() != null || dto.getPriceMax() != null) {
            spec = spec.and(PropertySpecification.hasPriceBetween(dto.getPriceMin(), dto.getPriceMax()));
        }

        if (dto.getAreaMin() != null || dto.getAreaMax() != null) {
            spec = spec.and(PropertySpecification.hasAreaBetween(dto.getAreaMin(), dto.getAreaMax()));
        }

        // 6. Execute Query
        Page<PropertyEntity> entityPage = propertyRepository.findAll(spec, pageable);
        if (entityPage.isEmpty()) {
            return Page.empty(pageable);
        }

        Set<Long> userIds = new HashSet<>();
        Set<Long> propertyIds = new HashSet<>();
        for (PropertyEntity propertyEntity : entityPage) {
            propertyIds.add(propertyEntity.getId());
            userIds.add(propertyEntity.getUserId());
        }
        Map<Long, UserDTO> userMap = userProxy.getUsersMap(userIds);
        Map<Long, List<PropertyMediaEntity>> mediaMap = propertyMediaRepository.findAllByPropertyIdIn(propertyIds)
                .stream()
                .collect(Collectors.groupingBy(PropertyMediaEntity::getPropertyId));
        Map<Long, PropertyAddressDTO> propertyAddressDTOMap = propertyAddressRepository.findAllByPropertyIdIn(propertyIds)
                .stream()
                .collect(Collectors.toMap(PropertyAddressEntity::getPropertyId, propertyAddressMapper::toDTO,
                        (existing, replacement) -> existing));
        return entityPage.map(propertyEntity -> {
            PropertyPage propertyPage = propertiesMapper.toPropertyPage(propertyEntity);
            // Gán User
            if (userMap.containsKey(propertyEntity.getUserId())) {
                propertyPage.setUser(userMap.get(propertyEntity.getUserId()));
            }
            // Gán Media
            if (mediaMap.containsKey(propertyEntity.getId())) {
                List<String> mediaUrls = mediaMap.get(propertyEntity.getId())
                        .stream()
                        .map(PropertyMediaEntity::getMediaUrl)
                        .collect(Collectors.toList());
                propertyPage.setMediaUrls(mediaUrls);
            }
            // Gán Address
            if (propertyAddressDTOMap.containsKey(propertyEntity.getId())) {
                propertyPage.setAddress(propertyAddressDTOMap.get(propertyEntity.getId()));
            }
            return propertyPage;
        });
    }

    // phê duyệt, từ chối bài đăng
    @Override
    @Transactional
    public void updateStatus(Long propertyId, PropertyStatus status) {
        if (!SecurityUtils.hasRole("ROLE_ADMIN")) {
            throw new AppException(ErrorCodes.ERROR_008);
        }
        PropertyEntity propertyEntity = propertyRepository.findById(propertyId).orElseThrow(() -> new AppException(ErrorCodes.ERROR_004));
        if (!propertyEntity.getStatus().equals(PropertyStatus.PENDING)) {
            throw new AppException(ErrorCodes.ERROR_009);
        }
        propertyEntity.setStatus(status);
        propertyRepository.save(propertyEntity);
    }

    @Override
    @Transactional
    public void delete(Long propertyId) {
        List<PropertyMediaEntity> mediaEntities = propertyMediaRepository.findAllByPropertyId(propertyId);
        List<String> fileIds = mediaEntities.stream().map(PropertyMediaEntity::getFileId)
                .filter(Objects::nonNull)
                .toList();
        if (!fileIds.isEmpty()) {
            // Xóa trên cloud
            userProxy.deleteImages(fileIds);
        }
        propertyMediaRepository.deleteAllByPropertyId(propertyId);
        propertyAddressRepository.deleteAllByPropertyId(propertyId);
        propertyRepository.deleteById(propertyId);
    }

    @Override
    public List<String> getSuggestions(String keyword) {
        // 1. Validate: Nếu null hoặc chuỗi rỗng thì trả về list rỗng luôn
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Tạo giới hạn: Mỗi loại lấy tối đa 5 kết quả để hiển thị cho nhanh
        Pageable limit = PageRequest.of(0, 5);

        // 3. Gọi DB
        List<String> projectNames = propertyRepository.searchProjectNames(keyword.trim(), limit);
        List<String> titles = propertyRepository.searchTitles(keyword.trim(), limit);

        // 4. Gộp kết quả
        // Dùng LinkedHashSet để:
        // - Loại bỏ trùng lặp (nếu tên dự án giống tiêu đề)
        // - Giữ thứ tự: Dự án quan trọng hơn nên xếp trước
        Set<String> suggestions = new LinkedHashSet<>();
        suggestions.addAll(projectNames); // Add dự án trước
        suggestions.addAll(titles);       // Add tiêu đề sau

        // 5. Trả về List
        return new ArrayList<>(suggestions);
    }
}
