package DAO;

import Model.Account;

public interface AccountDAO {
    Account createAccount(Account account);
    Account getAccountByUsername(String username);
    Account getAccountById(int accountId);
}