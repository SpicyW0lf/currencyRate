package servlets;

import DAO.CurrencyDAOImpl;
import DAO.CurrencyRateDAOImpl;
import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CurrencyRate;
import services.CurrencyRateService;
import util.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRate/new")
public class CurrencyRateNewServlet extends HttpServlet {

    private final CurrencyRateService currencyRateService = CurrencyRateService.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (Validator.isNotNull(baseCurrencyCode, targetCurrencyCode, rate)) {
            ExceptionHandler.handleException(400, "One of parameters is missed", resp);
            return;
        }
        try {
            CurrencyRate currRate = currencyRateService.getByCodePair(baseCurrencyCode + targetCurrencyCode);
            if (currRate != null) {
                ExceptionHandler.handleException(409, "CurrencyRate with such codes is already exists", resp);
                return;
            }
            currencyRateService.create(baseCurrencyCode, targetCurrencyCode, rate);

            resp.sendRedirect("/exchangeRate/" + baseCurrencyCode + targetCurrencyCode);
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }
    }
}
