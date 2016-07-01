package scheded;

import java.awt.BorderLayout;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class SchedEd {
    
    private JFrame mainFrame;
    BorderLayout mainFrameBLayout;
    
    Connection connection;
    
    private Authorize authorize;   
    private MenuPanel menuPanel;
    private TablePanel tablePanel;
    private EditPanel editPanel;
    
    
    public SchedEd() {
        
        mainFrame = new JFrame("ShedEd - редактор расписания [Pre-Alpha]");
        
        connection = getDBConnection();
        
        mainFrameBLayout = new BorderLayout();
        mainFrame.setLayout(mainFrameBLayout);
        
        authorize = new Authorize(connection);       
        menuPanel = new MenuPanel(connection);
        editPanel = new EditPanel(connection);  
        tablePanel = new TablePanel(connection, editPanel);
        editPanel.setTablePanel(tablePanel);
        menuPanel.setTablePanel(tablePanel);
        menuPanel.setEditPanel(editPanel);
        
        
        mainFrame.add(menuPanel.getPanel(), BorderLayout.NORTH);
        mainFrame.add(tablePanel.getPanel(), BorderLayout.CENTER);
        mainFrame.add(editPanel.getPanel(), BorderLayout.SOUTH);
   
    }
    
   
   
    public static void main(String[] args) {
       
        SchedEd schedEd = new SchedEd();

        schedEd.authorize.getMainClass(schedEd);
        //schedEd.authorize.show();
        schedEd.showFrame();
             
        
    }
    
    public Connection getDBConnection() {
        Connection dbConnection = null;
        
        String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/schedule"; 
        String DB_USER = "postgres";
        String DB_PASSWORD = "";
        String DB_DRIVER = "org.postgresql.Driver";
        
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
    
    public void testSQL() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
 
        String createTableSQL = "SELECT * FROM study_groups;";
 
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
 
                // выполнить SQL запрос
            PreparedStatement ps = dbConnection.prepareStatement(createTableSQL);
            
            ResultSet rs = ps.executeQuery();
    
            while( rs.next() ) {
                System.out.println( rs.getString( "id" ) );
            }
            ps.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }
    
    public void showFrame() {
        
        mainFrame.setSize(1000, 800);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
    }
}
