package servlets;

import DAO.CurrencyDAO;
import DAO.CurrencyDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.CurrencyMapper;
import models.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currencies")
public class CurrencyGetAllServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyDAO currencyDAO = CurrencyDAOImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String json = CurrencyMapper.mapToJson(currencyDAO.getAllCurrencies());
            resp.setStatus(200);
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            resp.sendError(500, e.getMessage());
        } finally {
            out.flush();
        }
    }
}
