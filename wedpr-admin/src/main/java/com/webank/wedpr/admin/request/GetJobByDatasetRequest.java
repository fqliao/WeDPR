package com.webank.wedpr.admin.request;

import com.webank.wedpr.common.utils.Constant;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetJobByDatasetRequest {
    @NotBlank private String datasetId;
    private Integer pageNum = Constant.DEFAULT_PAGE_NUM;

    @Min(value = 1, message = "分页条数最小不能小于1")
    @Max(value = 10000, message = "分页条数最大不能大于10000")
    private Integer pageSize = Constant.DEFAULT_PAGE_SIZE;
}
