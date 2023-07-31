package servlets;

import DAO.CurrencyRateDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.CurrencyRateMapper;
import models.CurrencyRate;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class CurrencyRateByCodes extends HttpServlet {

    private final CurrencyRateDAOImpl currencyRateDAO = CurrencyRateDAOImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pair = req.getPathInfo().substring(1);
        if (pair.length() != 6) {
            resp.sendError(400, "You entered incorrect currency pair");
            return;
        }

        try {
            CurrencyRate cr = currencyRateDAO.getCurrencyRateByCodes(pair);
            if (cr == null) {
                resp.sendError(404, "Currency rate with such codes doesn't exists");
                return;
            }
            PrintWriter out = resp.getWriter();
            String json = CurrencyRateMapper.mapToJson(cr);
            resp.setStatus(200);
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            resp.sendError(500, "Unable to connect to database");
        }
    }
}
