package br.com.dequebraeconomy.view;
import br.com.dequebraeconomy.dao.UserDao;
import br.com.dequebraeconomy.model.User;

import java.security.PublicKey;
import java.sql.SQLException;
import java.util.List;


public class UserView {

    public int cadastroUser(User user) {
        ///         CADASTRO DO USUARIO
        int id = 0;
        //estanciando meu usuario

        try {
            UserDao dao = new UserDao(); //--> estancio userDao pra poder utiliar os recursos do objeto
            id = dao.registerUser(user);


            System.out.println("User registrated");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return id;
    }




        //             GET USER BY ID
    public User getById(int id) {
            StringBuilder sb = new StringBuilder();
            User foundUser = null;
        try {
            UserDao dao = new UserDao();
            foundUser = dao.getById(id);

            if(foundUser != null) {
                sb.append("User ID: "). append(foundUser.getId());
                sb.append("Name: ").append(foundUser.getName() + "\n");
                sb.append("Email: ").append(foundUser.getEmail() + "\n");
                sb.append("CPF: ").append(foundUser.getCpf() + "\n");
                sb.append("Data de nascimento: ").append(foundUser.getBirthDate() + "\n");
            }else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return foundUser;
    }


    // LISTAR TODOS OS USUARIO

    public List<User> listAllUsers() {
        List<User> userList = null;
        try {
            UserDao dao = new UserDao();
             userList = dao.listUsers();
            for(User user : userList ){
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return userList;
    }
    // ATUALIZAR USER

    public void userUpdate(User user) throws SQLException{
        try {
            UserDao dao = new UserDao();

            dao.updateUser(user);

        } catch (Exception e) {
            System.err.println(e);

        }

    }

    //DELETAR USUÁRIO

    public boolean deleteUser(long id)throws SQLException{
        try{
            UserDao dao = new UserDao();
            dao.deleteUser(id);
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }

        return false;
    }


}

