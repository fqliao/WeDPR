package com.webank.wedpr.components.project.model;

import com.webank.wedpr.components.project.dao.JobDO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QueryJobsByDatasetIDResponse {
    long totalCount;
    boolean isLast;
    List<JobDO> content;
}
