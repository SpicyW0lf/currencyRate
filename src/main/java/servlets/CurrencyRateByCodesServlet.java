package servlets;

import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.JsonMapper;
import models.CurrencyRate;
import services.CurrencyRateService;
import services.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class CurrencyRateByCodesServlet extends HttpServlet {

    private final CurrencyRateService currencyRateService = CurrencyRateService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pair = req.getPathInfo().substring(1);
        if (pair.length() != 6) {
            ExceptionHandler.handleException(400, "You entered incorrect currency pair", resp);
            return;
        }

        try {
            CurrencyRate cr = currencyRateService.getByCodePair(pair);
            if (cr == null) {
                ExceptionHandler.handleException(404, "Currency rate with such codes doesn't exists", resp);
                return;
            }

            String json = JsonMapper.toJson(cr);
            PrintWriter out = resp.getWriter();
            resp.setStatus(200);
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }
    }
}
