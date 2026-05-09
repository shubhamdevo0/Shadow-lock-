package service;

import dao.UserDAO;
import model.user;
import java.sql.SQLException;

public class UserService {
    public static Integer saveUser(user user){
        try {
            if(UserDAO.isExists(user.getEmail())) {
                return 0;
            } else {
                return UserDAO.saveUser(user);
            }
        }   catch (SQLException ex) {
            ex.printStackTrace();

        }
        return null;
    }


}
