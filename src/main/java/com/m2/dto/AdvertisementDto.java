package com.m2.dto;

import com.m2.model.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {

    private int id;
    private String title;
    private String description;
    private Date publicationDate;
    private String location;
    private String deliveryOption;
    private String objectState;

    private UserDto user;
    private CategoryDto category;

     public static AdvertisementDto fromEntity(Advertisement advertisement) {
         if (advertisement == null) {
             return null;
         }
             return AdvertisementDto.builder()
                 .id(advertisement.getId())
                     .title(advertisement.getTitle())
                     .description(advertisement.getDescription())
                     .publicationDate(advertisement.getPublicationDate())
                     .location(advertisement.getLocation())
                     .deliveryOption(advertisement.getDeliveryOption())
                     .objectState(advertisement.getObjectState())
                     .user(UserDto.fromEntity(advertisement.getUser()))
                     .category(CategoryDto.fromEntity(advertisement.getCategory()))
                     .build();

     }

     public static Advertisement toEntity(AdvertisementDto advertisementDto) {
         if (advertisementDto == null) {
             return null;
         }

         Advertisement advertisement = new Advertisement();
         advertisement.setId(advertisementDto.getId());
         advertisement.setTitle(advertisementDto.getTitle());
         advertisement.setDescription(advertisementDto.getDescription());
         advertisement.setPublicationDate(advertisementDto.getPublicationDate());
         advertisement.setLocation(advertisementDto.getLocation());
         advertisement.setDeliveryOption(advertisementDto.getDeliveryOption());
         advertisement.setObjectState(advertisementDto.getObjectState());
         advertisement.setUser(UserDto.toEntity(advertisementDto.getUser()));
         advertisement.setCategory(CategoryDto.toEntity(advertisementDto.getCategory()));
         return advertisement;
     }
}
