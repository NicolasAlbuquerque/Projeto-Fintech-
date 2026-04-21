package dao;

import model.Category;
import model.Goal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    // A conexão com o banco vem de fora — quem abre é o ConnectionFactory
    private Connection connection;

    public GoalDAO(Connection connection) {
        this.connection = connection;
    }

    // ================================================================
    // INSERT
    // ================================================================
    public void insert(Goal goal) {

        // Primeiro INSERT: campos comuns vão para a tabela pai
        String sqlTransacao = "INSERT INTO T_TRANSACAO (id, dt_transacao, vl_amount, ds_descricao) " +
                "VALUES (SQ_TRANSACAO.NEXTVAL, ?, ?, ?)";

        // Segundo INSERT: campos específicos de Goal vão para a tabela filha
        String sqlObjetivo = "INSERT INTO T_OBJETIVO (id, vl_stipulated, dt_end, vl_current) " +
                "VALUES (SQ_TRANSACAO.CURRVAL, ?, ?, ?)";

        try {
            // --- Primeiro INSERT: T_TRANSACAO ---
            PreparedStatement stmtTransacao = connection.prepareStatement(sqlTransacao);

            stmtTransacao.setDate(1, Date.valueOf(goal.getDate()));
            stmtTransacao.setBigDecimal(2, goal.getAmount());
            stmtTransacao.setString(3, goal.getDescription());

            stmtTransacao.executeUpdate();
            stmtTransacao.close();

            // --- Segundo INSERT: T_OBJETIVO ---
            PreparedStatement stmtObjetivo = connection.prepareStatement(sqlObjetivo);

            stmtObjetivo.setBigDecimal(1, goal.getStipulatedAmount());
            stmtObjetivo.setDate(2, Date.valueOf(goal.getEndDate()));
            stmtObjetivo.setBigDecimal(3, goal.getCurrentValue());

            stmtObjetivo.executeUpdate();
            stmtObjetivo.close();

            System.out.println("Goal inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Goal: " + e.getMessage());
        }
    }

    // ================================================================
    // GET ALL
    // ================================================================
    public List<Goal> getAll() {

        // JOIN entre as duas tabelas para montar o objeto completo
        String sql = "SELECT t.id, t.dt_transacao, t.ds_descricao, " +
                "o.vl_stipulated, o.dt_end, o.vl_current " +
                "FROM T_TRANSACAO t " +
                "JOIN T_OBJETIVO o ON t.id = o.id";

        List<Goal> goals = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Construtor: (id, description, date, stipulatedAmount, endDate, category)
                Goal goal = new Goal(
                        rs.getLong("id"),
                        rs.getString("ds_descricao"),
                        rs.getDate("dt_transacao").toLocalDate(),
                        rs.getBigDecimal("vl_stipulated"),
                        rs.getDate("dt_end").toLocalDate(),
                        Category.GOAL  // Goal sempre é categoria GOAL
                );

                // currentValue não entra no construtor — existe setter para ele
                goal.setCurrentValue(rs.getBigDecimal("vl_current"));

                goals.add(goal);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Goals: " + e.getMessage());
        }

        return goals;
    }
}