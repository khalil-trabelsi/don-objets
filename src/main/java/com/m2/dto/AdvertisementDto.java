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
         advertisement.setKeywords(advertisementDto.getKeywords());
         return advertisement;
     }
}
