package com.wtx.pte;

import lombok.Data;

@Data
public class Config {
    private String postmanJsonPath;
    private String excelPath;
    private ColumnConfig columnConfig;
}
