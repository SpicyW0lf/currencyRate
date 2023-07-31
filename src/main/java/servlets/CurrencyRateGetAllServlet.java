package servlets;

import DAO.CurrencyRateDAO;
import DAO.CurrencyRateDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.CurrencyMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class CurrencyRateGetAllServlet extends HttpServlet {

    private final CurrencyRateDAO currencyRateDAO = CurrencyRateDAOImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            PrintWriter out = resp.getWriter();
            String json = CurrencyMapper.mapToJson(currencyRateDAO.getAllCurrencyRates());
            resp.setStatus(200);
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            resp.sendError(500, e.getMessage());
        }
    }
}
