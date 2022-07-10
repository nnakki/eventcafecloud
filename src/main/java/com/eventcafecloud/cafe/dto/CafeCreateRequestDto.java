package com.eventcafecloud.cafe.dto;

import com.eventcafecloud.cafe.domain.CafeOptionType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CafeCreateRequestDto {

    @NotEmpty(message = "카페 이름은 필수 입니다.")
    private String cafeName;

    private Integer cafeZonecode;

    private String cafeAddress;

    @NotEmpty(message = "주소 입력은 필수 입니다.")
    private String cafeAddressDetail;

    // 경도 : x : Longitude
    private Double cafeX;

    // 위도 : y : Latitude
    private Double cafeY;

    @NotEmpty(message = "한 줄 소개 입력은 필수 입니다.")
    private String cafeInfo;

    @NotEmpty(message = "상세 소개 입력은 필수 입니다.")
    private String cafeInfoDetail;

    @NotEmpty(message = "주의 사항 입력은 필수 입니다.")
    private String cafePrecaution;

    @NotEmpty(message = "평일 요금 입력은 필수 입니다.")
    private Integer cafeWeekdayPrice;

    @NotEmpty(message = "주말 요금 입력은 필수 입니다.")
    private Integer cafeWeekendPrice;

    private String cafeOpenTime;

    private String cafeCloseTime;

    @NotEmpty(message = "이미지 선택은 필수 입니다.")
    private List<MultipartFile> files;

    @NotEmpty(message = "옵션 선택은 필수 입니다.")
    private List<CafeOptionType> options;

}
