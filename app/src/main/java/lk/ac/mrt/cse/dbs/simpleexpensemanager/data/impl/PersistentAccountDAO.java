package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DbOperator;

/**
 * Created by Yamuna on 12/6/2015.
 */
public class PersistentAccountDAO implements AccountDAO {
    DbOperator dbOperator;


    public  PersistentAccountDAO(Context context){
        dbOperator=new DbOperator(context);
    }




    @Override
    public List<String> getAccountNumbersList() {
        return dbOperator.getAccountNumbersList();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbOperator.getAccountsList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return dbOperator.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        dbOperator.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbOperator.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        dbOperator.updateBalance(accountNo,expenseType,amount);
    }
}
