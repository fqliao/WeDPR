package com.webank.wedpr.components.dataset.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DatasetStatus {
    Success(0, "Success"),
    Failure(-1, "Failure"),
    Fatal(-2, "Fatal"),
    InitialState(1, "InitialState"),
    WaitingForUploadData(2, "WaitingForUploadData"),
    WaitingForLoadDBData(3, "WaitingForLoadDBData"),
    DataUploading(4, "DataUploading"),
    DataAnalyzing(5, "DataAnalyzing"),
    UploadingDataToStorage(6, "UploadingDataToStorage");

    private final Integer code;
    private final String message;
}
