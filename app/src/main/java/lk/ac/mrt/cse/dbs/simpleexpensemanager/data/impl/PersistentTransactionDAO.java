package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Warunika on 12/4/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    SQLiteDatabase mydb;
    java.io.File filename = Constants.CONTEXT.getFilesDir();
    public PersistentTransactionDAO()
    {
        mydb = SQLiteDatabase.openOrCreateDatabase(filename.getAbsolutePath() + "/130113D.sqlite", null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS Transactions(accountNo VARCHAR(50),expenseType VARCHAR(50),amount NUMERIC(10,2), date Date);");
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        mydb.execSQL("INSERT INTO Transactions VALUES('"+accountNo+"','"+((expenseType==ExpenseType.INCOME)?"INCOME":"EXPENSE")+"','"+amount+"','"+date.toString()+"');");
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultSet = mydb.rawQuery("Select * from Transactions",null);
        resultSet.moveToFirst();
        List<Transaction> result = new ArrayList<Transaction>();
        while(!resultSet.isAfterLast())
        {
            result.add( new Transaction(new Date(resultSet.getString(3)),resultSet.getString(0),((resultSet.getString(1)=="INCOME")?ExpenseType.INCOME:ExpenseType.EXPENSE), Double.parseDouble(resultSet.getString(2) ) ));
            resultSet.moveToNext();
        }
        return result;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = mydb.rawQuery("Select * from Transactions ORDER BY date LIMIT "+limit,null);
        resultSet.moveToFirst();
        List<Transaction> transactions = new ArrayList<Transaction>();
        while(!resultSet.isAfterLast())
        {
            transactions.add( new Transaction(new Date(resultSet.getString(3)),resultSet.getString(0),((resultSet.getString(1)=="INCOME")?ExpenseType.INCOME:ExpenseType.EXPENSE), Double.parseDouble(resultSet.getString(2) ) ));
            resultSet.moveToNext();
        }
        return transactions;
    }
}
