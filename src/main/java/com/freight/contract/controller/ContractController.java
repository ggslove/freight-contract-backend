package com.freight.contract.controller;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Receivable;
import com.freight.contract.entity.Payable;
import com.freight.contract.service.ContractService;
import com.freight.contract.service.ReceivableService;
import com.freight.contract.service.PayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "http://localhost:5173")
public class ContractController {
    
    @Autowired
    private ContractService contractService;
    
    @Autowired
    private ReceivableService receivableService;
    
    @Autowired
    private PayableService payableService;
    
    // 获取所有合同
    @GetMapping("/contracts")
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return ResponseEntity.ok(contracts);
    }
    
    // 根据ID获取合同
    @GetMapping("/contracts/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Optional<Contract> contract = contractService.getContractById(id);
        return contract.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // 创建合同
    @PostMapping("/contracts")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract savedContract = contractService.createContract(contract);
        return ResponseEntity.ok(savedContract);
    }
    
    // 更新合同
    @PutMapping("/contracts/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Optional<Contract> updatedContract = contractService.updateContract(id, contract);
        return updatedContract.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
    
    // 删除合同
    @DeleteMapping("/contracts/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        boolean deleted = contractService.deleteContract(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    // 获取所有应收记录
    @GetMapping("/receivables")
    public ResponseEntity<List<Receivable>> getAllReceivables() {
        List<Receivable> receivables = receivableService.getAllReceivables();
        return ResponseEntity.ok(receivables);
    }
    
    // 创建应收记录
    @PostMapping("/receivables")
    public ResponseEntity<Receivable> createReceivable(@RequestBody Receivable receivable) {
        Receivable savedReceivable = receivableService.createReceivable(receivable);
        return ResponseEntity.ok(savedReceivable);
    }
    
    // 更新应收记录
    @PutMapping("/receivables/{id}")
    public ResponseEntity<Receivable> updateReceivable(@PathVariable Long id, @RequestBody Receivable receivable) {
        Optional<Receivable> updatedReceivable = receivableService.updateReceivable(id, receivable);
        return updatedReceivable.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
    }
    
    // 删除应收记录
    @DeleteMapping("/receivables/{id}")
    public ResponseEntity<Void> deleteReceivable(@PathVariable Long id) {
        boolean deleted = receivableService.deleteReceivable(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    // 获取所有应付记录
    @GetMapping("/payables")
    public ResponseEntity<List<Payable>> getAllPayables() {
        List<Payable> payables = payableService.getAllPayables();
        return ResponseEntity.ok(payables);
    }
    
    // 创建应付记录
    @PostMapping("/payables")
    public ResponseEntity<Payable> createPayable(@RequestBody Payable payable) {
        Payable savedPayable = payableService.createPayable(payable);
        return ResponseEntity.ok(savedPayable);
    }
    
    // 更新应付记录
    @PutMapping("/payables/{id}")
    public ResponseEntity<Payable> updatePayable(@PathVariable Long id, @RequestBody Payable payable) {
        Optional<Payable> updatedPayable = payableService.updatePayable(id, payable);
        return updatedPayable.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }
    
    // 删除应付记录
    @DeleteMapping("/payables/{id}")
    public ResponseEntity<Void> deletePayable(@PathVariable Long id) {
        boolean deleted = payableService.deletePayable(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}