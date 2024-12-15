package com.m2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvertisementDto {

    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    private int id;

    private String title;
    private String description;
    private Date publicationDate;
    private String location;
    private String deliveryOption;
    private String objectState;
    private UserDto user;
    private CategoryDto category;
    private String keywords;
    private Boolean available;

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
                     .keywords(advertisement.getKeywords())
                     .available(advertisement.getAvailable())
                     .build();

     }

     public static Advertisement toEntity(AdvertisementDto advertisementDto) {
         if (advertisementDto == null) {
             return null;
         }

         return Advertisement.builder()
                 .id(advertisementDto.getId())
                 .title(advertisementDto.getTitle())
                 .description(advertisementDto.getDescription())
                 .publicationDate(advertisementDto.getPublicationDate())
                 .location(advertisementDto.getLocation())
                 .deliveryOption(advertisementDto.getDeliveryOption())
                 .objectState(advertisementDto.getObjectState())
                 .user(UserDto.toEntity(advertisementDto.getUser()))
                 .category(CategoryDto.toEntity(advertisementDto.getCategory()))
                 .keywords(advertisementDto.getKeywords())
                 .available(advertisementDto.getAvailable())
                 .build();
     }
}
