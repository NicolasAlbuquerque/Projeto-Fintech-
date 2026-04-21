package dao;

import model.Category;
import model.Income;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    // A conexão com o banco vem de fora — quem abre é o ConnectionFactory
    private Connection connection;

    public IncomeDAO(Connection connection) {
        this.connection = connection;
    }

    // ================================================================
    // INSERT
    // ================================================================
    public void insert(Income income) {

        // Primeiro INSERT: campos comuns vão para a tabela pai
        String sqlTransacao = "INSERT INTO T_TRANSACAO (id, dt_transacao, vl_amount, ds_descricao) " +
                "VALUES (SQ_TRANSACAO.NEXTVAL, ?, ?, ?)";

        // Segundo INSERT: campo específico vai para a tabela filha
        String sqlReceita = "INSERT INTO T_RECEITA (id, ds_source) " +
                "VALUES (SQ_TRANSACAO.CURRVAL, ?)";

        try {
            // --- Primeiro INSERT: T_TRANSACAO ---
            PreparedStatement stmtTransacao = connection.prepareStatement(sqlTransacao);

            stmtTransacao.setDate(1, Date.valueOf(income.getDate()));
            stmtTransacao.setBigDecimal(2, income.getAmount());
            stmtTransacao.setString(3, income.getDescription());

            stmtTransacao.executeUpdate();
            stmtTransacao.close();

            // --- Segundo INSERT: T_RECEITA ---
            PreparedStatement stmtReceita = connection.prepareStatement(sqlReceita);

            stmtReceita.setString(1, income.getSource());

            stmtReceita.executeUpdate();
            stmtReceita.close();

            System.out.println("Income inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Income: " + e.getMessage());
        }
    }

    // ================================================================
    // GET ALL
    // ================================================================
    public List<Income> getAll() {

        // JOIN entre as duas tabelas para montar o objeto completo
        String sql = "SELECT t.id, t.dt_transacao, t.vl_amount, t.ds_descricao, r.ds_source " +
                "FROM T_TRANSACAO t " +
                "JOIN T_RECEITA r ON t.id = r.id";

        List<Income> incomes = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Construtor: (id, date, amount, description, source, category)
                Income income = new Income(
                        rs.getLong("id"),
                        rs.getDate("dt_transacao").toLocalDate(),
                        rs.getBigDecimal("vl_amount"),
                        rs.getString("ds_descricao"),
                        rs.getString("ds_source"),
                        Category.INCOME   // Income sempre é categoria INCOME
                );

                incomes.add(income);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Incomes: " + e.getMessage());
        }

        return incomes;
    }
}