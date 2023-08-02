package servlets;

import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Currency;
import services.CurrencyService;
import util.Validator;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currencies/new")
public class CurrencyNewServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (Validator.isNotNull(name, code, sign)) {
            ExceptionHandler.handleException(400, "You missed one or more arguments", resp);
            return;
        }
        try {
            if (currencyService.getByCode(code) != null) {
                ExceptionHandler.handleException(409, "Currency with such code is already exists", resp);
                return;
            }
            currencyService.create(code, name, sign);
            resp.sendRedirect("/currency/" + code);
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }
    }
}
