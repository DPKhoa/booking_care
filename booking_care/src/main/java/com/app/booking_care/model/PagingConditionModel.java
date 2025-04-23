package com.app.booking_care.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingConditionModel {
    private int pageSize;
    private int pageCurrent;
    private String sortBy;
    private String sortType;

    public PagingConditionModel(int pageSize, int pageCurrent) {
        this.pageSize = pageSize;
        this.pageCurrent = pageCurrent;
    }
}
