package servlets;

import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.JsonMapper;
import services.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currencies")
public class CurrencyGetAllServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String json = JsonMapper.toJson(currencyService.getAll());
            resp.setStatus(200);
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }
    }
}
