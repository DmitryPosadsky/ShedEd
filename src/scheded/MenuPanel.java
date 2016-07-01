package scheded;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;

public class MenuPanel {
    
    private Connection connection;
    Statement statement;
    TablePanel tablePanel;
    EditPanel editPanel;
    
    JFrame addFrame;
    
    private JPanel menuPanel;
    private JPanel controlPanel;
    private JMenuBar menuBar;
    
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenu addMenu;
    
    private JComboBox firstListBox; 
    private JComboBox secondListBox; 
    
     
    public MenuPanel(Connection con) {
        
        connection = con;
        
        addFrame = new JFrame();
        
        menuPanel = new JPanel();
        controlPanel = new JPanel();
        BorderLayout menuLayout = new BorderLayout();
        GridLayout controlPanelLayout = new GridLayout(1,4);
        menuPanel.setLayout(menuLayout);
        controlPanel.setLayout(controlPanelLayout);
        
        //controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        
        viewMenu = new JMenu("Вид");
        menuBar.add(viewMenu);
        
        addMenu = new JMenu("Добавить");
        menuBar.add(addMenu);
        

        JMenuItem course = new JMenuItem("Курс");
        JMenuItem group = new JMenuItem("Группа");
        JMenuItem teacher = new JMenuItem("Преподаватель");
        JMenuItem auditory = new JMenuItem("Аудитория");
        
        course.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                pasteCourseEditor();
                tablePanel.setTableType(1);
                editPanel.setCourseNumber(1);
                editPanel.setDayNumber(1);
                tablePanel.updateTable();
            }
        });
        group.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                pasteGroupEditor();
                tablePanel.setColumnCount(7);
                tablePanel.setTableType(2);
                tablePanel.updateTable();
                editPanel.setCourseNumber(1);
                editPanel.setGroupNumber(1);
            }
        });
        teacher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                pasteTeacherEditor();
                tablePanel.setTableType(3);
                tablePanel.updateTable();
            }
        });
        auditory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(new JPanel(),"В разработке");
                tablePanel.setTableType(4);
                tablePanel.updateTable();
            }
        });

        viewMenu.add(course);
        viewMenu.add(group);
        viewMenu.add(teacher);
        viewMenu.add(auditory);
        
        
        JMenuItem addGroup = new JMenuItem("Группа");
        JMenuItem addTeacher = new JMenuItem("Преподаватель");
        JMenuItem addAuditory = new JMenuItem("Аудитория");
        JMenuItem addSpecialization = new JMenuItem("Специализация");
        

        addGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addGroup();
            }
        });
        addTeacher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addTeacher();
            }
        });
        addAuditory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(new JPanel(),"В разработке");
            }
        });
        addSpecialization.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(new JPanel(),"В разработке");
            }
        });

        
        addMenu.add(addGroup);
        addMenu.add(addTeacher);
        addMenu.add(addAuditory);
        addMenu.add(addSpecialization);

        
        menuPanel.add(menuBar, BorderLayout.NORTH);
        
        //pasteGroupEditor(1);
        
        menuPanel.add(controlPanel, BorderLayout.SOUTH);
             
     
    }
    
    public JPanel getPanel() {
        return menuPanel;
    }
    
    public void pasteCourseEditor() {
        
        tablePanel.setCourseNumber(1);
        tablePanel.setDayNumber(1);
        
        JLabel selectCourseLabel = new JLabel("Выберите курс: ");
        JLabel selectDayLabel = new JLabel("Выберите день недели: ");
        String[] courseList;
        String courseListRequest = "Курс";
        String courseListRequestName = "course_num";
        
        String getCourseListSQL = "select distinct course_num from study_groups order by course_num";
        
        try {
            courseList = getDataList(getCourseListSQL, courseListRequest, 
                    courseListRequestName);
        } catch (SQLException ex) {
            courseList = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        firstListBox = new JComboBox(courseList);
        firstListBox.removeItemAt(0);
        firstListBox.setFocusable(false);
        firstListBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                tablePanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                tablePanel.setDayNumber(secondListBox.getSelectedIndex());
                tablePanel.updateTable();
                editPanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                editPanel.setDayNumber(secondListBox.getSelectedIndex() + 1);
            }
        });
        
        String[] dayList = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница",
            "Суббота"};
        
        secondListBox = new JComboBox(dayList);
        secondListBox.setFocusable(false);
        secondListBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                tablePanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                tablePanel.setDayNumber(secondListBox.getSelectedIndex()+1);
                tablePanel.updateTable();
                editPanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                editPanel.setDayNumber(secondListBox.getSelectedIndex() + 1);
            }
        });
        
        controlPanel.removeAll();
        controlPanel.add(selectCourseLabel);
        controlPanel.add(firstListBox);
        controlPanel.add(selectDayLabel);
        controlPanel.add(secondListBox);
        controlPanel.revalidate();
        
    }
    
    public void pasteGroupEditor() {
        
        int courseNumber;
        
        tablePanel.setCourseNumber(1);
        tablePanel.setGroupNumber(1);
        
        JLabel selectCourseLabel = new JLabel("Выберите курс: ");
        JLabel selectGroupLabel = new JLabel("Выберите группу: ");
        String[] courseList;
        String courseListRequest = "Курс";
        String courseListRequestName = "course_num";
        
        String getCourseListSQL = "select distinct course_num from study_groups "
                + "order by course_num";
        
        try {
            courseList = getDataList(getCourseListSQL, courseListRequest, 
                    courseListRequestName);
        } catch (SQLException ex) {
            courseList = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        firstListBox = new JComboBox(courseList);
        firstListBox.removeItemAt(0);
        firstListBox.setFocusable(false);
        firstListBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                tablePanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                tablePanel.setGroupNumber(Integer.valueOf(secondListBox.getSelectedItem().toString()));
                tablePanel.updateTable();
                editPanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                editPanel.setGroupNumber(Integer.valueOf(secondListBox.getSelectedItem().toString()));
            }
        });
        
        String[] groupList;
        String groupListRequest = "Группа";
        String groupListRequestName = "group_num";
        
        courseNumber = Integer.valueOf(firstListBox.getSelectedItem().toString());
        String getGroupListSQL = "select distinct group_num from study_groups"
                + " where course_num = " + courseNumber + " order by group_num";
        
        try {
            groupList = getDataList(getGroupListSQL, groupListRequest, 
                    groupListRequestName);
        } catch (SQLException ex) {
            groupList = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        secondListBox = new JComboBox(groupList);
        secondListBox.removeItemAt(0);
        secondListBox.setFocusable(false);
        secondListBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                tablePanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                tablePanel.setGroupNumber(Integer.valueOf(secondListBox.getSelectedItem().toString()));
                tablePanel.updateTable();
                editPanel.setCourseNumber(Integer.valueOf(firstListBox.getSelectedItem().toString()));
                editPanel.setGroupNumber(Integer.valueOf(secondListBox.getSelectedItem().toString()));
            }
        });
    
        
        controlPanel.removeAll();
        
        controlPanel.add(selectCourseLabel);
        controlPanel.add(firstListBox);
        controlPanel.add(selectGroupLabel);
        controlPanel.add(secondListBox);
        controlPanel.revalidate();
        
    }
    
    public void pasteTeacherEditor() {
        
        tablePanel.setColumnCount(7);
        
        JLabel selectTeacherLabel = new JLabel("Выберите преподавателя: ");
        String[] teacherList;
        String teacherListRequest = "Преподаватель";
        String teacherListRequestName = "full_name";
        
        String getTeacherListSQL = "select full_name from teachers order by full_name";
        
        try {
            teacherList = getDataList(getTeacherListSQL, teacherListRequest, 
                    teacherListRequestName);
        } catch (SQLException ex) {
            teacherList = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        firstListBox = new JComboBox(teacherList);
        firstListBox.removeItemAt(0);
        firstListBox.setFocusable(false);
        firstListBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                
                ResultSet rs;
                int id = 0;
                
                try {
            
                    statement = connection.createStatement();

                    String selectLessonInfo = "select distinct id from teachers "
                            + "where full_name = '" + firstListBox.getSelectedItem().toString() + "';";
                    rs = statement.executeQuery(selectLessonInfo);

                    while (rs.next()) {
                        id = Integer.valueOf(rs.getString("id"));
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());

                }
                
                tablePanel.setTeacherNumber(id);
                tablePanel.setColumnCount(7);
                tablePanel.updateTable();
            }
        });
              
        controlPanel.removeAll();
        controlPanel.add(selectTeacherLabel);
        controlPanel.add(firstListBox);
        
        controlPanel.revalidate();
        
    }
       
    public String[] getDataList(String request, String nameRequest, 
            String nameRequestColm) throws SQLException {

 
        String[] requestResult = null;
        int resultSetSize;
        
        try {
            
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(request,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            
            ResultSet rs = ps.executeQuery();

            rs.last();
            resultSetSize = rs.getRow();
            rs.beforeFirst();

            requestResult = new String[resultSetSize+1];

            int i = 1;
            requestResult[0] = nameRequest;
            while( rs.next() ) {
                requestResult[i] = rs.getString(nameRequestColm);
                ++i;
            }
            
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return requestResult;
        
    }
    
    public void setEditPanel(EditPanel ePanel) {
        editPanel = ePanel;
    }
    
    public void setTablePanel(TablePanel tPanel) {
        tablePanel = tPanel;
    }
    
    public void addGroup() {
        
        addFrame.setTitle("Добавление группы");
        GridLayout addGroupFrameLayout = new GridLayout(7,1);
        addFrame.setLayout(addGroupFrameLayout);
        
        JLabel courseLabel = new JLabel("Курс: ");
        JLabel groupLabel = new JLabel("Группа: ");
        JLabel specializationLabel = new JLabel("Специализация: ");
        
        courseLabel.setHorizontalAlignment(JLabel.CENTER);
        groupLabel.setHorizontalAlignment(JLabel.CENTER);
        specializationLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JTextField courseTextField = new JTextField();
        JTextField groupTextField = new JTextField();
              
        courseTextField.setHorizontalAlignment(JTextField.CENTER);
        groupTextField.setHorizontalAlignment(JTextField.CENTER);
        
        String[] specializationList;
        String courseListRequest = "Курс";
        String courseListRequestName = "specialization";
        
        String getCourseListSQL = "select specialization from specializations";
        
        try {
            specializationList = getDataList(getCourseListSQL, courseListRequest, 
                    courseListRequestName);
        } catch (SQLException ex) {
            specializationList = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JComboBox specializationComboBox = new JComboBox(specializationList);
        specializationComboBox.removeItemAt(0);
        
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addGroupButtonClick(Integer.valueOf(courseTextField.getText()), 
                        Integer.valueOf(groupTextField.getText()), 
                        specializationComboBox.getSelectedIndex() + 1);
            }
        });
        
        addFrame.add(courseLabel);
        addFrame.add(courseTextField);
        addFrame.add(groupLabel);
        addFrame.add(groupTextField);
        addFrame.add(specializationLabel);
        addFrame.add(specializationComboBox);
        addFrame.add(addButton);
        
        
        addFrame.setSize(400, 400);
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.setVisible(true);
        addFrame.setLocationRelativeTo(null);
        
    }
    
    public void addTeacher() {
        
        addFrame.setTitle("Добавление преподавателя");
        GridLayout addGroupFrameLayout = new GridLayout(9,1);
        addFrame.setLayout(addGroupFrameLayout);
        
        JLabel firstNameLabel = new JLabel("Фамилия: ");
        JLabel secondNameLabel = new JLabel("Имя: ");
        JLabel middleNameLabel = new JLabel("Отчество: ");
        JLabel degreeLabel = new JLabel("Степень: ");
        
        firstNameLabel.setHorizontalAlignment(JLabel.CENTER);
        secondNameLabel.setHorizontalAlignment(JLabel.CENTER);
        middleNameLabel.setHorizontalAlignment(JLabel.CENTER);
        degreeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JTextField firstNameTextField = new JTextField();
        JTextField secondNameTextField = new JTextField();
        JTextField middleNameTextField = new JTextField();
        JTextField degreeTextField = new JTextField();
        
              
        firstNameTextField.setHorizontalAlignment(JTextField.CENTER);
        secondNameTextField.setHorizontalAlignment(JTextField.CENTER);
        middleNameTextField.setHorizontalAlignment(JTextField.CENTER);
        degreeTextField.setHorizontalAlignment(JTextField.CENTER);
        
        /*
        String[] specializationList;
        String courseListRequest = "Курс";
        String courseListRequestName = "specialization";
        
        String getCourseListSQL = "select specialization from specializations";
        
        try {
            specializationList = getDataList(getCourseListSQL, courseListRequest, 
                    courseListRequestName);
        } catch (SQLException ex) {
            specializationList = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        
        JComboBox specializationComboBox = new JComboBox(specializationList);
        specializationComboBox.removeItemAt(0);
                */
        
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addTeacherButtonClick(firstNameTextField.getText(), 
                        secondNameTextField.getText(), 
                        middleNameTextField.getText(),
                        degreeTextField.getText());
            }
        });
        
        addFrame.add(firstNameLabel);
        addFrame.add(firstNameTextField);
        addFrame.add(secondNameLabel);
        addFrame.add(secondNameTextField);
        addFrame.add(middleNameLabel);
        addFrame.add(middleNameTextField);
        addFrame.add(degreeLabel);
        addFrame.add(degreeTextField);
        addFrame.add(addButton);
        
        
        addFrame.setSize(400, 400);
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.setVisible(true);
        addFrame.setLocationRelativeTo(null);        
        
    }
    
    public void addAuditory() {
        
    }
    
    public void addSpezialization() {
        
    }
    
    
    public void addGroupButtonClick(int course, int group, int id) {
        
        try {
            
            statement = connection.createStatement();

            String selectLessonInfo = "select id from study_groups where course_num = '" + course
                    + "' and group_num = '" + group + "';";
            
            ResultSet rs = statement.executeQuery(selectLessonInfo);
  
            if (rs.next()) {
                JOptionPane.showMessageDialog(new JPanel(), "На " + course + " уже существует " + 
                        group + " группа!");
            }
            else {
                String req = "insert into study_groups(course_num, group_num, specialization_id, "
                        + "name) values(" + course + ", " + group + ", " + id + ", 0" + ")";  

                statement.executeUpdate(req);
                
                addFrame.removeAll();
                addFrame.setVisible(false);
                
                
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
                    
        }
              
    }
    
    public void addTeacherButtonClick(String fName, String sName, String mName, String degree) {
        
        //System.out.println(fName + " " + sName + " " + mName);
        
        try {
            
            statement = connection.createStatement();

            String selectLessonInfo = "select id from teachers where first_name = '" + fName
                    + "' and second_name = '" + sName + "' and middle_name = '" + mName + "';";
            
            ResultSet rs = statement.executeQuery(selectLessonInfo);
  
            if (rs.next()) {
                JOptionPane.showMessageDialog(new JPanel(), "Такой преподаватель уже существует!");
            }
            else {
                String req = "insert into teachers(first_name, second_name, middle_name, degree,"
                        + "full_name) values('" + fName + "', '" + sName + "', '" + mName + "', '" 
                        +  degree + "', '" + fName + " " + sName.charAt(0) + "." + mName.charAt(0) 
                        + ".');";  

                statement.executeUpdate(req);
                
                addFrame.removeAll();
                addFrame.setVisible(false);
                
                
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
                    
        }        
        
    }
    
}
