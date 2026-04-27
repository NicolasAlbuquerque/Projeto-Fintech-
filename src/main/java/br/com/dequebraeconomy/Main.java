package br.com.dequebraeconomy;

import br.com.dequebraeconomy.model.User;
import br.com.dequebraeconomy.view.UserView;

import java.sql.SQLException;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) throws SQLException {
        UserView userView = new UserView();



        LocalDate date = LocalDate.of(1994,9,27);
        long idUser = userView.cadastroUser(new User("Nicolas", "85455", "contato@email.com", "56456", date ));
        long idUser2 = userView.cadastroUser(new User("Mike", "86455", "contato2@email.com", "56456", date ));
        long idUser3 = userView.cadastroUser(new User("Kamily", "86655", "contato3@email.com", "56456", date ));
        User user = userView.getById(1);
        System.out.println(user.toString());
        System.out.println(idUser);

        User atualizar = userView.getById(1);
        atualizar.setName("Nicolas Albuquerque");
        atualizar.setPassword("123456");
        atualizar.setCpf("4844");
        atualizar.setBirthDate(date);
        atualizar.setEmail("Contato.nicolasAlbuquerque@gmail.com");

        userView.userUpdate(atualizar);

        userView.listAllUsers();
        userView.deleteUser(2);
        userView.listAllUsers();




    }
    }

