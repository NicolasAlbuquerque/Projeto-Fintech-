package br.com.dequebraeconomy.dao;


import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.Category;
import br.com.dequebraeconomy.model.Expense;
import br.com.dequebraeconomy.model.Income;
import br.com.dequebraeconomy.model.Transaction;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.RecursiveTask;

public class TransactionDao {
//    id_transaction    NUMBER          GENERATED ALWAYS AS IDENTITY,
//    id_user      NUMBER(10)      NOT NULL,
//    id_category    NUMBER(1)       NOT NULL,
//    dt_transaction    DATE            NOT NULL,
//    amount    NUMBER(10,2)    NOT NULL,
//    ds_transaction    VARCHAR2(150)


//    private Transaction parseTransaction(ResultSet result) throws SQLException {
//        Long id_transaction = result.getLong("id_transaction");
//        Long id_user = result.getLong("id_user");
//        Long id_category = result.getLong("id_category");
//        LocalDate date = result.getDate("dt_transaction").toLocalDate();
//        BigDecimal amount = result.getBigDecimal("amount");
//        String description = result.getString("ds_transaction");
//
//
//        Category category = Category.fromId(id_category)
//        switch (category) {
//            case EXPENSE:
//                return new Expense(id_transaction, date, amount, description, category, id_user)
//
//        }
//
//
//    }

    public int registerTransaction(Transaction t, long id) throws  SQLException {
        String sql = "INSERT INTO T_TRANSACTION (ID_USER, ID_CATEGORY, DT_TRANSACTION, AMOUNT, DS_TRANSACTION) VALUES (?,?,?,?,?)";

        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stm = connection.prepareStatement(sql, new String[] {"ID_TRANSACTION"})){

            stm.setLong(1, id);
            stm.setLong(2, t.getCategory().getId());
            stm.setDate(3, java.sql.Date.valueOf(t.getDate()));
            stm.setBigDecimal(4, t.getAmount());
            stm.setString(5, t.getDescription());

            //--> inserindo no banco
            stm.executeUpdate();

            //---> pego o id gerado pelo registro
            ResultSet tId = stm.getGeneratedKeys();

            //--> coloco o ponteiro no próximo item caso houver
            if(tId.next()){
                return tId.getBigDecimal(1).intValue();
            }

            throw new RuntimeException("Erro ao regisstrar");
        }
        }


        public Transaction getById(Long id, long idUser)throws SQLException{
            ResultSet result = null;
            String sql = "SELECT * FROM T_TRANSACTION WHERE ID_TRANSACTION = ?";


            try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)){

                stm.setLong(1, id);

                result = stm.executeQuery();

                if(!result.next()){
                    throw new SQLException("User not Found");
                }

                Long idT = result.getLong("id_transaction");
                Long idUserT = result.getLong("id_user");
                int idCategory = result.getInt("id_category");
                LocalDate date = result.getDate("dt_transaction").toLocalDate();
                BigDecimal amoutT = result.getBigDecimal("amount");
                String description= result.getNString("ds_transaction");




                if(idCategory == 1){
                    String sqlIncome = "SELECT * FROM T_INCOME WHERE ID_TRANSACTION = ? AND USER_ID = ?";


                    try (PreparedStatement stmIncome = connection.prepareStatement(sqlIncome)){

                        stmIncome.setLong(1,idT);
                        stmIncome.setLong(2, idUser);

                        ResultSet rsi = stmIncome.executeQuery();

                        if(!rsi.next()){
                            throw  new SQLDataException("Transaction not found");
                        }


                        String source = rsi.getString("sourceT");

                        Category category = Category.INCOME;
                        return  new Income(idT,idUser,date, amoutT, description,source,category);


                    }
                }else if(idCategory == 2){
                    String sqLExpense = "SELECT * FROM T_EXPENSE WHERE ID_TRANSACTION = ? AND ID_USER = ?";

                    try (PreparedStatement stmExpense = connection.prepareStatement(sqLExpense)){
                        stmExpense.setLong(1, id);
                        stmExpense.setLong(2, idUser);


                        ResultSet rsE = stmExpense.executeQuery();

                        if(!rsE.next()){
                            throw  new SQLDataException("Transaction not found");
                        }

                        String paymentMeyhod = rsE.getNString("payment_method");
                        Boolean paymentStatus = rsE.getBoolean("payment_status");
                        boolean recurring = rsE.getBoolean("recurring_payment");

                        Category category = Category.EXPENSE;
//idT,date, amoutT, description,source,category
                        return new Expense(idT,idUser,date,amoutT, description,paymentMeyhod,paymentStatus,recurring, category);

                    }



                }else if(idCategory == 3){

                }




            }

            throw new SQLException("Unknown category");
        }


    }



