package com.freight.contract.service;

import com.freight.contract.entity.Payable;
import com.freight.contract.eunus.ContractStatus;
import com.freight.contract.repository.PayableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayableService {

    @Autowired
    private PayableRepository payableRepository;

    public List<Payable> getAllPayables() {
        return payableRepository.findAll();
    }

    public Optional<Payable> getPayableById(Long id) {
        return payableRepository.findById(id);
    }

    public List<Payable> getPayablesByContractId(Long contractId) {
        return payableRepository.findByContractId(contractId);
    }



    public List<Payable> searchPayables(String keyword) {
        return payableRepository.searchByKeyword(keyword);
    }

    public Payable createPayable(Payable payable) {
        return payableRepository.save(payable);
    }

    public Optional<Payable> updatePayable(Long id, Payable payableDetails) {
        return payableRepository.findById(id)
                .map(payable -> {
                    payable.setFinanceItem(payableDetails.getFinanceItem());
                    payable.setAmount(payableDetails.getAmount());
                    payable.setCurrency(payableDetails.getCurrency());
                    payable.setDueDate(payableDetails.getDueDate());
                    payable.setStatus(payableDetails.getStatus());
                    return payableRepository.save(payable);
                });
    }

    public boolean deletePayable(Long id) {
        return payableRepository.findById(id)
                .map(payable -> {
                    payableRepository.delete(payable);
                    return true;
                })
                .orElse(false);
    }
}