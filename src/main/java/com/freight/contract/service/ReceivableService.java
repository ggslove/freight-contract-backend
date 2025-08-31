package com.freight.contract.service;

import com.freight.contract.entity.Receivable;
import com.freight.contract.entity.ReceivableStatus;
import com.freight.contract.repository.ReceivableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceivableService {
    
    @Autowired
    private ReceivableRepository receivableRepository;
    
    public List<Receivable> getAllReceivables() {
        return receivableRepository.findAll();
    }
    
    public Optional<Receivable> getReceivableById(Long id) {
        return receivableRepository.findById(id);
    }
    
    public List<Receivable> getReceivablesByContractId(Long contractId) {
        return receivableRepository.findByContractId(contractId);
    }
    
    public List<Receivable> getReceivablesByStatus(ReceivableStatus status) {
        return receivableRepository.findByStatus(status);
    }
    
    public List<Receivable> getReceivablesByCustomerName(String customerName) {
        return receivableRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    public List<Receivable> searchReceivables(String keyword) {
        return receivableRepository.searchByKeyword(keyword);
    }
    
    public Receivable createReceivable(Receivable receivable) {
        return receivableRepository.save(receivable);
    }
    
    public Optional<Receivable> updateReceivable(Long id, Receivable receivableDetails) {
        return receivableRepository.findById(id)
                .map(receivable -> {
                    receivable.setCustomerName(receivableDetails.getCustomerName());
                    receivable.setAmount(receivableDetails.getAmount());
                    receivable.setCurrency(receivableDetails.getCurrency());
                    receivable.setDueDate(receivableDetails.getDueDate());
                    receivable.setStatus(receivableDetails.getStatus());
                    return receivableRepository.save(receivable);
                });
    }
    
    public boolean deleteReceivable(Long id) {
        return receivableRepository.findById(id)
                .map(receivable -> {
                    receivableRepository.delete(receivable);
                    return true;
                })
                .orElse(false);
    }
}