package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        //TODO
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
        //TODO
        return null;
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        
        return null;
    }

    @Override
    public List<SuspectAccount> getSuspectAccount(String cuenta) {
        return null;
    }
}
