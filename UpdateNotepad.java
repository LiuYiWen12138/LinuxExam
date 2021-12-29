import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/UpdateNotepad")
public class UpdateNotepad extends HttpServlet {
    final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    final static String URL = "jdbc:mysql://180.76.145.137/LinuxExam";
    final static String USER = "root";
    final static String PASS = "Lyw731218@";
    final static String SQL_INSERT_NOTEPAD = "UPDATE  notepad SET noteName=? WHERE id=?";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Notepad req = getRequestBody(request);
        getServletContext().log(req.toString());
        PrintWriter out = response.getWriter();

        out.println(createNotepad(req));
        out.flush();
        out.close();
    }

    private Notepad getRequestBody(HttpServletRequest request) throws IOException {
        Notepad note = new Notepad();
        StringBuffer bodyJ = new StringBuffer();
        String line = null;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            bodyJ.append(line);
        Gson gson = new Gson();
        note = gson.fromJson(bodyJ.toString(), new TypeToken<Notepad>() {
        }.getType());
        return note;
    }

    private int createNotepad(Notepad req) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int retcode = -1;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.prepareStatement(SQL_INSERT_NOTEPAD);
           
            stmt.setString(1, req.noteName);
            stmt.setInt(2, req.id);
         
            int row = stmt.executeUpdate();
            if (row > 0)
                retcode = 1;

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return retcode;
    }
class Notepad {
        int id;
        String noteName;
        String noteTime;
    }

}

