package scheded;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.paint.Color;
import javax.swing.*;

public class Authorize {
    
    Connection connection;
    Statement statement;
    SchedEd schedEd;
    
    private JFrame authorizeFrame;
    private JPanel authorizePanel;
    
    private JPanel loginPasswordPanel;
    
    private JLabel mainText;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    
    String login;
    String password;
    int count;
    
    private JFormattedTextField loginField;
    private JPasswordField passwordField;
    
    private JButton loginButton;
    
    public Authorize(Connection connect) {
        
        connection = connect;
        
        authorizeFrame = new JFrame();
        authorizePanel = new JPanel();
        
        authorizeFrame.setLayout(new BorderLayout());
        
        BorderLayout authorizePanelLayout = new BorderLayout();
        authorizePanel.setLayout(authorizePanelLayout);
        
        mainText = new JLabel("Авторизация");
        mainText.setHorizontalAlignment(JLabel.CENTER);
        mainText.setPreferredSize(new Dimension(300,30));
        mainText.setForeground(java.awt.Color.DARK_GRAY);
        mainText.setFont(new Font("Verdana", Font.BOLD, 20));
        authorizePanel.add(mainText, BorderLayout.NORTH);
        
        loginPasswordPanel = new JPanel();
        GridLayout loginPasswordPanelLayout = new GridLayout(4,1);
        loginPasswordPanel.setLayout(loginPasswordPanelLayout);
        
        loginLabel = new JLabel("Логин:");
        loginLabel.setHorizontalAlignment(JLabel.CENTER);
        passwordLabel = new JLabel("Пароль:");
        passwordLabel.setHorizontalAlignment(JLabel.CENTER);
        
        loginField = new JFormattedTextField();
        passwordField = new JPasswordField();
        
        loginPasswordPanel.add(loginLabel);
        loginPasswordPanel.add(loginField);
        loginPasswordPanel.add(passwordLabel);
        loginPasswordPanel.add(passwordField);
        
        authorizePanel.add(loginPasswordPanel, BorderLayout.CENTER);
        
        loginButton = new JButton("Вход");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isCorrectData()) {
                    authorizeFrame.setVisible(false);
                    schedEd.showFrame();
                }
                else
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Неверный логин или пароль \n Попробуйте снова");
            }
        });
        authorizePanel.add(loginButton, BorderLayout.SOUTH);
        
        authorizeFrame.add(authorizePanel, BorderLayout.CENTER);
        
    }
    
    public void show() {
        
        authorizeFrame.setSize(400, 200);
        authorizeFrame.setVisible(true);
        authorizeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //authorizeFrame.pack();
        authorizeFrame.setLocationRelativeTo(null);
        
        //return entryToDB();
        
    }
    
    public boolean isCorrectData() {
        
        login = loginField.getText();
        password = new String(passwordField.getPassword());
        
        //System.out.println(login + " " + password);
        
        try {
                    statement = connection.createStatement();

                    String selectLessonInfo = "SELECT COUNT(login) FROM users "
                            + "WHERE login = '" + login + "' and password = '" 
                            + password + "';";
                    ResultSet rs = statement.executeQuery(selectLessonInfo);
  
                    while (rs.next()) {
                        
                        count = Integer.valueOf(rs.getString("count"));

                    }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            count = 0;
        }
        
        passwordField.setText("");
        
        if (count > 0)
            return true;
        else
            return false;
    }
    
    public void getMainClass (SchedEd se) {
        schedEd = se;
    }
}
