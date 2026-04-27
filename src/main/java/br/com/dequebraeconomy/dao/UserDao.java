package br.com.dequebraeconomy.dao;

import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

//    TROQUEI O ATRIBUTO POR UM TRY WITH RESOURCES QUE CRIA A CONEXÃO E FECHA AUTOMATICAAMENTE
//    private Connection connection; //--> atributo que armazena a conexão com o bando de dados
//
////--> crio um construtor para estanciar a conexão com o banco de dados através da Factory
//
//
//    //
//    public UserDao() throws SQLException{
//        connection = ConnectionFactory.getConnection(); //---> meu atributo conecction recebe a conexão com obanco de dados
//
//    }


    private User parseUser(ResultSet result) throws SQLException {
        Long idUser = result.getLong("id_user");
        String email = result.getString("email");
        String userPassword = result.getString("user_password");
        String name = result.getString("username");
        String cpf = result.getString("cpf");
        LocalDate date = result.getDate("birth_date").toLocalDate();

        return new User(idUser, name, email, userPassword, cpf, date);
    }



    public int registerUser(User user) throws SQLException {
        //crio minha string sql
        String sql = "INSERT INTO T_USER (EMAIL, USER_PASSWORD, USERNAME, BIRTH_DATE, CPF) VALUES( ?,?,?,?,?)";
        try (Connection connection = ConnectionFactory.getConnection();
        //--> crio um objeto PreparedStatemnent para enviar informações
        PreparedStatement stm = connection.prepareStatement(sql, new String[] {"ID_USER"})){ //--> PrepareStatemnent para enviar informações

            //--> utili o setString para organizar as informações dos '?'
            stm.setString(1, user.getEmail());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getName());
            stm.setDate(4, java.sql.Date.valueOf(user.getBirthDate()));
            stm.setString(5, user.getCpf());


            // --> comando para inserir no banco
            stm.executeUpdate();
            //--> PEGAR O ID GERADO.
            ResultSet rsId = stm.getGeneratedKeys();
            if (rsId.next()) {
                return rsId.getBigDecimal(1).intValue();
            }
            throw new SQLException("Erro ao gerar o ID");
        }
    }

    public User getById(long id) throws SQLException {
        ResultSet result = null;
        String sql = "SELECT * FROM T_USER WHERE ID_USER = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) { //--> crio um objeto preparedStatement e insiro o tipo de ação do SQL que quero fazer
            stm.setLong(1, id);// ---> passo os valores da query

            //---> crio um resultSet para salvar o resultado da minha query
            result = stm.executeQuery();

            if (!result.next()) {
                throw new SQLDataException();
            }

                return parseUser(result);
        }
    }
    public List<User> listUsers() throws SQLException {
        String sql = "SELECT * FROM T_USER";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet result = stm.executeQuery()){ // salvo o resultado da query) {

            List<User> userList = new ArrayList<>(); /// crio a lista  para salvar o povo

            while (result.next()) {
                userList.add(parseUser(result));

            }

            return userList;
        }
    }

    public  void updateUser(User user) throws SQLException {
        String sql = "UPDATE T_USER SET email = ?, user_password = ?, username = ?, birth_date = ?, cpf = ? WHERE id_user = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, user.getEmail());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getName());
            stm.setDate(4, java.sql.Date.valueOf(user.getBirthDate()));
            stm.setString(5 , user.getCpf());
            stm.setLong(6, user.getId());

            stm.executeUpdate();
        }
    }
    public void deleteUser(long id) throws SQLException{
        String sql = "DELETE FROM T_USER WHERE ID_USER = ? ";


        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)){
            stm.setLong(1, id);
            int linha = stm.executeUpdate();
            if(linha == 0){
                throw new SQLDataException("User not found.");
            }
        }

    }

}



