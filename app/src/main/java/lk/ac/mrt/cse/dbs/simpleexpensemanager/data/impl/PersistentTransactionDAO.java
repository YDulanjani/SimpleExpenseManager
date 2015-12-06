package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DbOperator;

/**
 * Created by Yamuna on 12/6/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO{

    DbOperator dbOperator;

    public  PersistentTransactionDAO(Context context){

        dbOperator=new DbOperator(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dbOperator.logTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbOperator.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }
}
