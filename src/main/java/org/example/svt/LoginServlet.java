package org.example.svt;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.example.tinderDAO.ControllerTinderDao;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;

public class LoginServlet extends HttpServlet {

    private int currentUserId;
    private String cookieId;
    private final ControllerTinderDao controllerTinderDao;
    private final Configuration conf;

    public LoginServlet(ControllerTinderDao controllerTinderDao, Configuration conf) {
        this.controllerTinderDao = controllerTinderDao;
        this.conf = conf;

//        ClassLoader cl = this.getClass().getClassLoader();
//        URL resource = cl.getResource("html/1.txt");
//        String file = resource.getFile();
//        String path = file.substring(0, file.length() - 5);
//        String file1 = path + "chat.ftl";
//        File f1 = new File(file1);
//        String file2 = path + "like-page-Andrii.ftl";
//        File f2 = new File(file2);
//        String file3 = path + "login.ftl";
//        File f3 = new File(file3);
//        String file4 = path + "people-list-Andrii.ftl";
//        File f4 = new File(file4);
//        String file5 = path + "signup.ftl";
//        File f5 = new File(file5);
//        String file6 = path + "css/style1.css";
//        File f6 = new File(file6);
    }

    HashMap<String, Object> data = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        data.put("name", "");
        try (PrintWriter w = resp.getWriter()) {
            conf.getTemplate("login.ftl").process(data, w);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        String username = req.getParameter("username");
        String password = req.getParameter("password");

       try {
            cookieId = controllerTinderDao.checkUser(username, password);
            if (cookieId == null) {

                data.put("name", "Username or password is incorrect, please try again");
                try (PrintWriter w = resp.getWriter()) {
                    conf.getTemplate("login.ftl").process(data, w);
                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }

            } else {
               
                resp.addCookie(new Cookie("id", cookieId));
                resp.sendRedirect("/users");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}