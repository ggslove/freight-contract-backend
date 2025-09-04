package com.freight.contract.service;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.ContractStatus;
import com.freight.contract.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {
    
    @Autowired
    private ContractRepository contractRepository;
    
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }
    
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }
    
    public List<Contract> getContractsByCustomerName(String customerName) {
        return contractRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    public List<Contract> getContractsByStatus(ContractStatus status) {
        return contractRepository.findByStatus(status);
    }
    
    public List<Contract> searchContracts(String keyword) {
        return contractRepository.searchByKeyword(keyword);
    }
    

    
    public Contract createContract(Contract contract) {
        // 确保使用save方法并返回保存后的实体
        return contractRepository.save(contract);
    }
    
    public Optional<Contract> updateContract(Long id, Contract contract) {
        return contractRepository.findById(id)
                .map(existing -> {
                    existing.setBusinessNo(contract.getBusinessNo());
                    existing.setCustomerName(contract.getCustomerName());
                    existing.setSalesman(contract.getSalesman());
                    existing.setContractDate(contract.getContractDate());
                    existing.setAmount(contract.getAmount());
                    existing.setStatus(contract.getStatus());
                    return contractRepository.save(existing);
                });
    }
    
    public boolean deleteContract(Long id) {
        return contractRepository.findById(id)
                .map(contract -> {
                    contractRepository.delete(contract);
                    return true;
                })
                .orElse(false);
    }
}