package com.wtx.pte;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class App {

    private final static Workbook workbook = new XSSFWorkbook();
    private static Config CONFIG = new Config();
    private final static String CONFIG_PATH = "/config.yml";

    private static void initConfig() {
        Yaml yaml = new Yaml();
        InputStream inputSteam = App.class.getResourceAsStream(CONFIG_PATH);
        CONFIG = yaml.loadAs(inputSteam, Config.class);
    }


    public static void main(String[] args) throws Exception {
        //初始化配置
        initConfig();

        // 使用Jackson解析Swagger JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(CONFIG.getPostmanJsonPath()));

        // 创建Excel工作簿
        Sheet sheet = workbook.createSheet("接口清单");


        // 创建表头
        Row headerRow = sheet.createRow(0);

        createCommonStyleCell(headerRow, CONFIG.getColumnConfig().getName()).setCellValue("Name");
        createCommonStyleCell(headerRow, CONFIG.getColumnConfig().getPath()).setCellValue("Path");
        createCommonStyleCell(headerRow, CONFIG.getColumnConfig().getRequestParams()).setCellValue("RequestBody");

        // item
        ArrayNode arrayNode = (ArrayNode) rootNode.path("item");
        for (JsonNode elementNode : arrayNode) {
            ArrayNode interfaceArr = (ArrayNode) elementNode.get("item");
            for (JsonNode interfaceNode : interfaceArr) {
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                createCommonStyleCell(row, CONFIG.getColumnConfig().getName()).setCellValue(interfaceNode.get("name").textValue());
                createCommonStyleCell(row, CONFIG.getColumnConfig().getPath()).setCellValue(interfaceNode.get("request").get("url").get("raw").textValue());
                createCommonStyleCell(row, CONFIG.getColumnConfig().getRequestParams()).setCellValue(interfaceNode.get("request").get("body").get("raw").textValue());
            }
        }

        sheet.autoSizeColumn(CONFIG.getColumnConfig().getName());
        sheet.autoSizeColumn(CONFIG.getColumnConfig().getPath());
        sheet.autoSizeColumn(CONFIG.getColumnConfig().getRequestParams());
        // 写入文件
        try (FileOutputStream outputStream = new FileOutputStream(CONFIG.getExcelPath())) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Cell createCommonStyleCell(Row row, int column) {
        Cell cell = row.createCell(column);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        return cell;
    }
}
