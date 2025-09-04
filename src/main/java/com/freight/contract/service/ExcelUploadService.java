package com.freight.contract.service;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.ContractStatus;
import com.freight.contract.entity.Receivable;
import com.freight.contract.entity.Payable;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelUploadService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ReceivableService receivableService;

    @Autowired
    private PayableService payableService;

    private CurrencyService currencyService;

    public List<Contract> uploadExcel(MultipartFile file) throws IOException {
        List<Contract> createdContracts = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // 假设Excel第一个sheet为合同数据
            Sheet contractSheet = workbook.getSheetAt(0);

            // 跳过表头行，从第二行开始解析(行号从0开始)
            for (int i = 1; i <= contractSheet.getLastRowNum(); i++) {
                Row row = contractSheet.getRow(i);
                if (row == null) continue;

                // 解析合同数据(根据实际Excel列顺序调整)
                Contract contract = new Contract();
                contract.setBusinessNo(getCellValue(row.getCell(0)));
                contract.setCustomerName(getCellValue(row.getCell(1)));
                contract.setAmount(new BigDecimal(getCellValue(row.getCell(2))));
                contract.setStatus(ContractStatus.valueOf(getCellValue(row.getCell(4))));
                contract.setContractDate(LocalDateTime.parse(getCellValue(row.getCell(5))));
                // ... 设置其他字段

                // 保存合同
                Contract savedContract = contractService.createContract(contract);
                // 添加合同保存验证
                if (savedContract == null || savedContract.getId() == null) {
                    throw new IllegalStateException("合同保存失败，无法获取合同ID");
                }
                createdContracts.add(savedContract);

                // 解析应收数据
                Receivable receivable = new Receivable();
                // 将ID关联改为对象关联
                receivable.setContract(savedContract);
                receivable.setAmount(new BigDecimal(getCellValue(row.getCell(6))));
                receivable.setDueDate(LocalDateTime.parse(getCellValue(row.getCell(7))));
                receivableService.createReceivable(receivable);

                // 解析应付数据
                Payable payable = new Payable();
                // 将ID关联改为对象关联
                payable.setContract(savedContract);
                payable.setAmount(new BigDecimal(getCellValue(row.getCell(8))));
                payable.setDueDate(LocalDateTime.parse(getCellValue(row.getCell(9))));
                payableService.createPayable(payable);
            }
        }
        return createdContracts;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}