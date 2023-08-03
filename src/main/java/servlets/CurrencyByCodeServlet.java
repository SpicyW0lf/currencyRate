package servlets;

import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.JsonMapper;
import models.Currency;
import services.CurrencyService;
import util.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyByCodeServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().substring(1);
        if (!Validator.isCodeValid(code)) {
            ExceptionHandler.handleException(400, "You haven't put currency code in path", resp);
            return;
        }

        try {
            Currency curr = currencyService.getByCode(code);
            if (curr == null) {
                ExceptionHandler.handleException(404, "Currency not found", resp);
            } else {
                String json = JsonMapper.toJson(curr);
                PrintWriter pw = resp.getWriter();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.setStatus(200);
                pw.print(json);
                pw.close();
            }
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, e.getMessage(), resp);
        }
    }
}
