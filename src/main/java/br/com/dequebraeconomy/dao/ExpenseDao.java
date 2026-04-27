package br.com.dequebraeconomy.dao;

import br.com.dequebraeconomy.factory.ConnectionFactory;


import java.sql.Connection;
import java.sql.SQLException;

public class ExpenseDao {
    Connection connection; //---> declaro meu atributo que vai guardar minha conexão com o db


    //CRIO UM CONSTRUTOR PARA  ATRIBUIR O VALOR RETORNADO PELA CLASSE CONNECTIONfACTORY
    public ExpenseDao() throws SQLException {
        this.connection = ConnectionFactory.getConnection();//-->meu atributo recebe o valor de retorno do metodo getConnection() dss class ConnectionFactory
                //--> esse metodo precisa sempre do tratamento SQLexception antes do corpo do metodo.
     }



}
