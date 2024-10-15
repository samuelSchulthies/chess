package service;

import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryUserDAO;
import requestresult.ClearResult;

public class ClearService {
    ClearResult clear(){
        MemoryUserDAO memUserDAO = new MemoryUserDAO();
        MemoryAuthTokenDAO memAuthTokenDAO = new MemoryAuthTokenDAO();

        memUserDAO.clear();
        memAuthTokenDAO.clear();
        return new ClearResult();
    }
}