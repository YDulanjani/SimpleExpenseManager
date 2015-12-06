package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbOperator extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "130140G";
    protected static final String ACCOUNT_TABLE_NAME = "account";
    protected static final String TRANSACTION_TABLE_NAME = "transaction";

    Context context;

    public DbOperator(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);

        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /** CREATE TABLE ACCOUNT
         *  accountNo TEXT Primary key,
         *  bankName TEXT,
         *  accountHolderName TEXT,
         *  balance DOUBLE
        */
        String createAccoutQuery="create table if not exists "+ACCOUNT_TABLE_NAME+"(accountNo TEXT PRIMARY KEY, bankName TEXT,accountHolderName TEXT, balance DOUBLE);";

        /** CREATE TABLE TRANSACTION
         *  accountNo TEXT Foriegn key,
         *  date TEXT,
         *  expenseType TEXT,
         *  amount DOUBLE
         */
        String createTransactionQuery="create table if not exists "+TRANSACTION_TABLE_NAME+"(accountNo TEXT ,date TEXT,expenseType TEXT,amount DOUBLE,PRIMARY KEY(accountNo,date,amount),FOREIGN KEY(accountNo) REFERENCES "+ACCOUNT_TABLE_NAME+"(accountNo));";


        db.execSQL(createAccoutQuery);
        db.execSQL(createTransactionQuery);
    }


    //HANDLE Account Table In Database


    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("accountNo",account.getAccountNo());
        values.put("bankName",account.getBankName());
        values.put("accountHolderName",account.getAccountHolderName());
        values.put("balance",account.getBalance());


        db.insert(ACCOUNT_TABLE_NAME, null, values);
        db.close();
    }

    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<Account>();

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " +ACCOUNT_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            accounts.add(account);
        }
        return accounts;
    }


    public Account getAccount(String accountNo)throws InvalidAccountException {
        Account account = null;

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " +ACCOUNT_TABLE_NAME+"WHERE accountNo='"+accountNo+"';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
        } else {
            db.close();
            throw new InvalidAccountException("Account " + accountNo + " is invalid.");
        }
        db.close();
        return account;
    }

    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT accountNo FROM " +ACCOUNT_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()){
            accountNumbers.add(cursor.getString(0));
        }
        return accountNumbers;
    }

    public void removeAccount(String accountNo)throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        int res=db.delete(ACCOUNT_TABLE_NAME,"accountNo = '"+ accountNo +"'", null);
        if ( res != 1) {
            throw new InvalidAccountException( "Account " + accountNo + " is invalid.");
        }
    }



    //HANDLE Transaction Table In Database


    public void logTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("accountNo",transaction.getAccountNo());
        values.put("date",transaction.getDate().toString());
        values.put("expenseType",transaction.getExpenseType().toString());
        values.put("amount",transaction.getAmount());

        db.insert(TRANSACTION_TABLE_NAME, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " +TRANSACTION_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            Transaction transaction = new Transaction(new Date(cursor.getString(0)), cursor.getString(1), (cursor.getString(2) == "Income" ? ExpenseType.INCOME : ExpenseType.EXPENSE), cursor.getDouble(3));
            transactions.add(transaction);
        }
        return transactions;
    }

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException  {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT balance FROM " + ACCOUNT_TABLE_NAME + "WHERE accountNo ='" + accountNo + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        double result =0;
        if (cursor.moveToFirst()) {
            result = cursor.getDouble(0);
        } else {
            throw new InvalidAccountException( "Account " + accountNo + " is invalid.");
        }
        double newAmount = (expenseType == ExpenseType.INCOME ? result+amount : result-amount);


        ContentValues values = new ContentValues();

        String updateQuery = "UPDATE "
                + ACCOUNT_TABLE_NAME + " SET amount = " + newAmount + " WHERE accountNo ='" + accountNo + "';";
        db.execSQL(updateQuery);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}