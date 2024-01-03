package com.marcomarchionni.ibportfolio.dtos.request;

import com.marcomarchionni.ibportfolio.dtos.validators.AssetCategory;
import com.marcomarchionni.ibportfolio.dtos.validators.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionFindDto {

    private Boolean tagged;

    @Size(max = 20)
    @NullOrNotBlank
    private String symbol;

    @AssetCategory
    private String assetCategory;
}
