package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.service.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ExcelUploadResolver {

    @Autowired
    private ExcelUploadService excelUploadService;

    @MutationMapping
    public List<Contract> uploadContractExcel(@Argument MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            throw new IllegalArgumentException("仅支持Excel文件上传");
        }
        return excelUploadService.uploadExcel(file);
    }
}