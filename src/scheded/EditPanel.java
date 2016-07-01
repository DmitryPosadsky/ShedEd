package scheded;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EditPanel {
    
    Connection connection;
    Statement statement;
    PreparedStatement preparedStatement;
    CellData cellData;
    
    private TablePanel tablePanel;
    
    private JPanel editPanel;
    private JPanel checkBoxPanel;
    private JPanel centerPanel;
    private JPanel jComboBoxPanel;
    
    private JCheckBox week;
    private JCheckBox subgroup;
    private JCheckBox comment;
    
    private JComboBox[] subject;
    private JComboBox[] teacher;
    private JComboBox[] classroom;
    
    private JSpinner subgroupCount;
    
    boolean weekJCheckBoxIsPressed;
    boolean subgroupJCheckBoxIsPressed;
    
    int tableType;
    
    int courseNumber;
    int groupNumber;
    int dayNumber;
    
    int groupNumberForCourse; // Номер группы, использоуемый в course
    
    int weeks;
    int subgroups;
    
    int currentDayOfWeek;
    int currentLessonNumber;
    
    int cellWeeks;
    int cellSubgroups;
    
    String teachers[][];
    String subjects[][];
    String classrooms[][];
    
    String selectLessonInfo;
    String teacherFullName;
    String subjectName;
    
    JLabel weekUpTitle;
    JLabel weekDownTitle;
    
    JLabel[] subgroupTitleUp;
    JLabel[] subgroupTitleDown;
    
    JPanel jComboBoxPanelUp;
    JPanel jComboBoxPanelDown;
    
    JPanel[] jComboBoxPanels;
    
    GridLayout jComboBoxPanelLayout;
    GridLayout jComboBoxPanelLayoutUp;
    GridLayout jComboBoxPanelLayoutDown;
    GridLayout jComboBoxPanelsLayout;
    
    public EditPanel(Connection connect) {
        
        connection = connect;
        
        currentDayOfWeek = 0;
        currentLessonNumber = 0;
        
        editPanel = new JPanel();
        editPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        BorderLayout editPanelLayout = new BorderLayout();
        editPanel.setLayout(editPanelLayout);
        
        checkBoxPanel = new JPanel();
        GridLayout checkBoxPanelLayout = new GridLayout(3,1);
        checkBoxPanel.setLayout(checkBoxPanelLayout);
        
        week = new JCheckBox("Разделение на недели");
        subgroup = new JCheckBox("Разделение на подгруппы");
        comment = new JCheckBox("Комментарий");
        
        week.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                weekActionPerformed(event);
            }
        });
        
        subgroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                subgroupActionPerformed(event);
            }
        });
        
        comment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                commentActionPerformed(event);
            }
        });
        
        checkBoxPanel.add(week);
        checkBoxPanel.add(subgroup);
        checkBoxPanel.add(comment);
        
        editPanel.add(checkBoxPanel, BorderLayout.WEST);
        
        //---------------------end WEST-------------------------//
        
        centerPanel = new JPanel();
        BorderLayout centerPanelLayout = new BorderLayout();
        centerPanel.setLayout(centerPanelLayout);
        
        subgroupCount = new JSpinner();
        subgroupCount.setValue(1);
        subgroupCount.setEnabled(false);
        subgroupCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                subgroupCountStateChanged(event);
            }
        });
        centerPanel.add(subgroupCount, BorderLayout.WEST);
        
        jComboBoxPanel = new JPanel();
        GridLayout jComboBoxPanelLayout = new GridLayout(2,1);
        jComboBoxPanel.setLayout(jComboBoxPanelLayout);
        
        subject = new JComboBox[6];
        teacher = new JComboBox[6];
        classroom = new JComboBox[6];
        
        String[] subjectCork;
        String subjectRequest = "SELECT subject FROM subjects order by subject";
        String subjectNameRequest = "Предмет";
        String subjectNameRequestColm = "subject";
        try {
            subjectCork = getDataList(subjectRequest, subjectNameRequest, 
                    subjectNameRequestColm);
        } catch (SQLException ex) {
            subjectCork = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String[] teacherCork;
        String teacherRequestTest = "Declare @Result varchar(8000);" +
                "Select @Result = RTRIM(ISNULL(@Result,'') + "
                + "ISNULL(classrooms_set.classroom_id,''))" +
                "From classrooms_set; select @Result;";
        
        String teacherRequest = "SELECT full_name FROM teachers order by full_name";
        String teacherNameRequest = "Преподаватель";
        String teacherNameRequestColm = "full_name";
        try {
            teacherCork = getDataList(teacherRequest, teacherNameRequest, 
                    teacherNameRequestColm);
        } catch (SQLException ex) {
            teacherCork = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String[] classroomCork;
        String classroomRequest = "SELECT classroom FROM classrooms order by classroom";
        String classroomNameRequest = "Аудитория";
        String classroomNameRequestColm = "classroom";
        try {
            classroomCork = getDataList(classroomRequest, classroomNameRequest, 
                    classroomNameRequestColm);
        } catch (SQLException ex) {
            classroomCork = null;
            Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < 6; ++i) {
            subject[i] = new JComboBox(subjectCork);
            teacher[i] = new JComboBox(teacherCork);
            classroom[i] = new JComboBox(classroomCork);
            subject[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    //entryToDB();
                }
            });
            teacher[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    //entryToDB();
                }
            });
            classroom[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    //entryToDB();
                }
            });
        }
        
        JLabel mainTitle = new JLabel("Редактирование расписания");
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        jComboBoxPanel.add(mainTitle);
        JPanel jComboBoxStartPanel = new JPanel();
        jComboBoxStartPanel.setLayout(new GridLayout(1,3));
        jComboBoxStartPanel.add(subject[0]);
        jComboBoxStartPanel.add(teacher[0]);
        jComboBoxStartPanel.add(classroom[0]);
        jComboBoxPanel.add(jComboBoxStartPanel);
        
        centerPanel.add(jComboBoxPanel, BorderLayout.CENTER);
        
        editPanel.add(centerPanel, BorderLayout.CENTER);
        
        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                okButtonClick();
            }
        });
        editPanel.add(okButton, BorderLayout.EAST);
        
        init();
        
        
    }
    
    public void setTablePanel(TablePanel tPanel) {
        tablePanel = tPanel;
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
    
    public JPanel getPanel() {
        return editPanel;
    }
    
    public void okButtonClick() {
        
        tableType = tablePanel.getTableType();
        
        switch (tableType) {
            case 1: { okButtonClickCourse(); break; }
            case 2: { okButtonClickGroup(); break; }
            case 3: { okButtonClickTeacher(); break; }
            case 4: { okButtonClickClassroom(); break; }
        }
        
    }
    
    public void okButtonClickCourse() {
        
        int studyGroupId = 1; // Первый курс, первая группа
        
        int curriculum = 0;
        
        int curClassroomSetId = 73;
        int curLessonType = 0;
        int curElectiveCourse = 0;
        int curSemester = 0;
        int curKindOfDepartment = 0;
        
        String insertLessonInfo;
        
        String teacherItemN;
        String subjectIdItemN;
        
        int teacherIndexN = 0;
        int subjectIdIndexN = 0;
        
        ResultSet rs;
        
        try {
                       
            statement = connection.createStatement();
                    
            String req = "delete from lessons l using "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + dayNumber
                                + " and les.less_num = " + currentLessonNumber 
                                + " and s.course_num = " + courseNumber
                                + " and s.group_num = " + groupNumberForCourse
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";  

            statement.executeUpdate(req);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        for (int i = 0; i < weeks; ++i) {
            for (int j = 0; j < subgroups; ++j) {
                
                teacherIndexN = 0;
                subjectIdIndexN = 0;
                curriculum = 0;
                   
                teacherItemN = String.valueOf(teacher[i*3 + j].getSelectedItem());
                subjectIdItemN = String.valueOf(subject[i*3 + j].getSelectedItem());
                
                //System.out.println(teacherItemN + " " + subjectIdItemN);
                
                try {
                    statement = connection.createStatement();
                    
                    selectLessonInfo = "select id from study_groups where course_num = '" +
                            courseNumber + "' and group_num = '" + groupNumberForCourse + "';";

                    rs = statement.executeQuery(selectLessonInfo);
                    
                    while (rs.next()) {

                        studyGroupId = Integer.parseInt(rs.getString("id"));

                    }
                    
                    selectLessonInfo = "select id from teachers where full_name = '" +
                            teacherItemN + "';";

                    rs = statement.executeQuery(selectLessonInfo);
                    
                    while (rs.next()) {

                        teacherIndexN = Integer.parseInt(rs.getString("id"));

                    }
                    
                    selectLessonInfo = "select id from subjects where subject = '" +
                            subjectIdItemN + "';";

                    rs = statement.executeQuery(selectLessonInfo);
                    
                    while (rs.next()) {

                        subjectIdIndexN = Integer.parseInt(rs.getString("id"));

                    }
                    
                    //System.out.println(teacherIndexN + " " + subjectIdIndexN);

                    selectLessonInfo = "select id from curriculum where teacher_id = " +
                            teacherIndexN + " and study_group_id = " + studyGroupId +
                            " and subject_id = " + subjectIdIndexN;

                    rs = statement.executeQuery(selectLessonInfo);

                    if (!rs.next()) {
                        insertLessonInfo = "insert into curriculum(teacher_id, "
                                 + "study_group_id, subject_id) values(" + teacherIndexN 
                                 + ", " + studyGroupId + ", " + subjectIdIndexN + ")";
                        preparedStatement = connection.prepareStatement(insertLessonInfo);
                        preparedStatement.executeUpdate();
                    }

                    rs = statement.executeQuery(selectLessonInfo);

                    while (rs.next()) {

                        curriculum = Integer.parseInt(rs.getString("id"));

                    }
                    
                    //System.out.println(curriculum);
                    
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                //System.out.println(curriculum);
                
                if (curriculum != 0) {

                    try {

                        String stm = "insert into lessons(curriculumid, days, "
                                + "lesson_time_id, classroom_set_id, subgroup_count, "
                                + "subgroup_num, week_split, les_type, notice, "
                                + "elective_course, semester, kind_of_department) "
                                + "VALUES(" + curriculum + ", " + dayNumber + 
                                ", " + currentLessonNumber + ", " + curClassroomSetId + 
                                ", " + subgroups + ", " + (j + 1) +
                                ", " + ((weeks == 1) ? 0 : (i+1)) + ", " + curLessonType + 
                                ", " + "' '" + ", " + curElectiveCourse + 
                                ", " + curSemester + ", " + curKindOfDepartment + ")" ;

                        preparedStatement = connection.prepareStatement(stm);
                        preparedStatement.executeUpdate();

                    } catch (SQLException e) {
                           System.out.println(e.getMessage());
                    }
                }
            }
        } 
        
        tablePanel.updateTable();
        
    }
    
    public void okButtonClickGroup() {
        
        int studyGroupId = 1; // Первый курс, первая группа
        
        int curriculum = 0;
        
        int curClassroomSetId = 73;
        int curLessonType = 0;
        int curElectiveCourse = 0;
        int curSemester = 0;
        int curKindOfDepartment = 0;
        
        String insertLessonInfo;
        
        String teacherItemN;
        String subjectIdItemN;
        
        int teacherIndexN = 0;
        int subjectIdIndexN = 0;
        
        ResultSet rs;
        
        try {
                       
            statement = connection.createStatement();
                    
            String req = "delete from lessons l using "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + currentDayOfWeek
                                + " and les.less_num = " + currentLessonNumber 
                                + " and s.course_num = " + courseNumber
                                + " and s.group_num = " + groupNumber 
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";  

            statement.executeUpdate(req);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        for (int i = 0; i < weeks; ++i) {
            for (int j = 0; j < subgroups; ++j) {
                
                teacherIndexN = 0;
                subjectIdIndexN = 0;
                curriculum = 0;
                   
                teacherItemN = String.valueOf(teacher[i*3 + j].getSelectedItem());
                subjectIdItemN = String.valueOf(subject[i*3 + j].getSelectedItem());
                
                //System.out.println(teacherItemN + " " + subjectIdItemN);
                
                try {
                    statement = connection.createStatement();
                    
                    selectLessonInfo = "select id from study_groups where course_num = '" +
                            courseNumber + "' and group_num = '" + groupNumber + "';";

                    rs = statement.executeQuery(selectLessonInfo);
                    
                    while (rs.next()) {

                        studyGroupId = Integer.parseInt(rs.getString("id"));

                    }
                    
                    selectLessonInfo = "select id from teachers where full_name = '" +
                            teacherItemN + "';";

                    rs = statement.executeQuery(selectLessonInfo);
                    
                    while (rs.next()) {

                        teacherIndexN = Integer.parseInt(rs.getString("id"));

                    }
                    
                    selectLessonInfo = "select id from subjects where subject = '" +
                            subjectIdItemN + "';";

                    rs = statement.executeQuery(selectLessonInfo);
                    
                    while (rs.next()) {

                        subjectIdIndexN = Integer.parseInt(rs.getString("id"));

                    }
                    
                    //System.out.println(teacherIndexN + " " + subjectIdIndexN);

                    selectLessonInfo = "select id from curriculum where teacher_id = " +
                            teacherIndexN + " and study_group_id = " + studyGroupId +
                            " and subject_id = " + subjectIdIndexN;

                    rs = statement.executeQuery(selectLessonInfo);

                    if (!rs.next()) {
                        insertLessonInfo = "insert into curriculum(teacher_id, "
                                 + "study_group_id, subject_id) values(" + teacherIndexN 
                                 + ", " + studyGroupId + ", " + subjectIdIndexN + ")";
                        preparedStatement = connection.prepareStatement(insertLessonInfo);
                        preparedStatement.executeUpdate();
                    }

                    rs = statement.executeQuery(selectLessonInfo);

                    while (rs.next()) {

                        curriculum = Integer.parseInt(rs.getString("id"));

                    }
                    
                    //System.out.println(curriculum);
                    
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                //System.out.println(curriculum);
                
                if (curriculum != 0) {

                    try {
                        
                        String stm = "insert into lessons(curriculumid, days, "
                                + "lesson_time_id, classroom_set_id, subgroup_count, "
                                + "subgroup_num, week_split, les_type, notice, "
                                + "elective_course, semester, kind_of_department) "
                                + "VALUES(" + curriculum + ", " + currentDayOfWeek + 
                                ", " + currentLessonNumber + ", " + curClassroomSetId + 
                                ", " + subgroups + ", " + (j + 1) +
                                ", " + ((weeks == 1) ? 0 : (i+1)) + ", " + curLessonType + 
                                ", " + "' '" + ", " + curElectiveCourse + 
                                ", " + curSemester + ", " + curKindOfDepartment + ")" ;

                        preparedStatement = connection.prepareStatement(stm);
                        preparedStatement.executeUpdate();

                    } catch (SQLException e) {
                           System.out.println(e.getMessage());
                    }
                }
            }
        } 
        
        tablePanel.updateTable();
        
    }
    
    public void okButtonClickTeacher() {
        
    }
    
    public void okButtonClickClassroom() {
        
    }
    
    public void setLessonInfo(CellData cData) {
        
        cellData = cData;
        
        currentDayOfWeek = cellData.j;
        currentLessonNumber = cellData.i;
        
        weeks = cellData.weekCount;
        subgroups = cellData.groupCount;
        
        groupNumberForCourse = cellData.groupNumber;

        cellWeeks = cellData.weekCount;
        cellSubgroups = cellData.groupCount;
        
        for (int i = 0; i < weeks; ++i) 
            for (int j = 0; j < subgroups; ++j) {
                teachers[i][j] = cellData.teacher[i][j];
                subjects[i][j] = cellData.subject[i][j];
                //classrooms[i][j] = cellData.classroom[i][j];
                
            }
        
        if (weeks == 2) 
            week.setSelected(true);
        else
            week.setSelected(false);
        
        if (subgroups > 1) {
            subgroup.setSelected(true);
            subgroupCount.setEnabled(true);
            subgroupCount.setValue(subgroups);
        }
        else {
            subgroup.setSelected(false);
            subgroupCount.setEnabled(false);
            subgroupCount.setValue(1);
        }
        
        
        updateEditPanel();
        
        removeSelectedCheckBox();
        
        setSelectedCheckBox();

              
    }
    
    public void updateEditPanel() {
      
        jComboBoxPanel.removeAll();
            jComboBoxPanel.setLayout(null); 
            if (weeks == 2)
                jComboBoxPanelLayout = new GridLayout(4, 1);
            else
                jComboBoxPanelLayout = new GridLayout(1, 1);
            jComboBoxPanel.setLayout(jComboBoxPanelLayout);
            jComboBoxPanelLayoutUp = new GridLayout(2, subgroups);
            jComboBoxPanelLayoutDown = new GridLayout(2, subgroups);
            jComboBoxPanelUp.removeAll();
            jComboBoxPanelDown.removeAll();
            jComboBoxPanelUp.setLayout(jComboBoxPanelLayoutUp);
            jComboBoxPanelDown.setLayout(jComboBoxPanelLayoutDown);

            for (int i = 0; i < weeks; ++i) {
                
                for (int j = 0; j < subgroups; ++j)
                    if (i == 0)
                        jComboBoxPanelUp.add(subgroupTitleUp[j]);
                    else 
                        jComboBoxPanelDown.add(subgroupTitleDown[j]);
                
                for (int j = 0; j < subgroups; ++j) {

                        jComboBoxPanels[i*3 + j].add(subject[i*3 + j]);
                        jComboBoxPanels[i*3 + j].add(teacher[i*3 + j]);
                        jComboBoxPanels[i*3 + j].add(classroom[i*3 + j]);

                        if (i == 0) {
                            jComboBoxPanelUp.add(jComboBoxPanels[i*3 + j]);
                        }
                        else {
                            jComboBoxPanelDown.add(jComboBoxPanels[i*3 + j]);
                        }
                    }
            }

            //weekUpTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            if (weeks == 2) 
                jComboBoxPanel.add(weekUpTitle);
            jComboBoxPanel.add(jComboBoxPanelUp);
            //weekDownTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            if (weeks == 2) {
                jComboBoxPanel.add(weekDownTitle);
                jComboBoxPanel.add(jComboBoxPanelDown);
            }
            
            jComboBoxPanel.updateUI();   

    }
    
    public void setSelectedCheckBox() {
        
        for (int i = 0; i < cellWeeks; ++i) 
                for (int j = 0; j < cellSubgroups; ++j) {

                    subject[i*3 + j].setSelectedItem(subjects[i][j]);
                    teacher[i*3 + j].setSelectedItem(teachers[i][j]);
                    //classroom[i*subgroups + j].setSelectedItem(classrooms[i][j]);
                    
                }
        
    }
    
    public void setCourseNumber(int cNumber) {
        courseNumber = cNumber;
    }
    
    public void setGroupNumber(int gNumber) {
        groupNumber = gNumber;
    }
    
    public void setDayNumber(int dNumber) {
        dayNumber = dNumber;
    }
    
    public void removeSelectedCheckBox() {
        
        for (int i = 0; i < 2; ++i) 
                for (int j = 0; j < 3; ++j) {
                    
                    subject[i*3 + j].setSelectedIndex(0);
                    teacher[i*3 + j].setSelectedIndex(0);
                    //classroom[i*subgroups + j].setSelectedIndex(0);
                    
                }
        
    }
       
    public void weekActionPerformed(ActionEvent event) {
        weeks = 1;
        if (week.isSelected()) 
            weeks = 2;                 
        //createJComboBox(weeks, Integer.valueOf(subgroupCount.getValue().toString()));
        subgroups = Integer.valueOf(subgroupCount.getValue().toString());
        updateEditPanel();
    }
    
    public void subgroupActionPerformed(ActionEvent event) {
        if (subgroup.isSelected()) 
            subgroupCount.setEnabled(true);
        else {
            subgroupCount.setEnabled(false);
            subgroupCount.setValue(1);
        }
        
    }
    
    public void commentActionPerformed(ActionEvent event) {
        
    }
    
    public void subgroupCountStateChanged(ChangeEvent event) {
        if (Integer.valueOf(subgroupCount.getValue().toString()) < 1) {
            subgroupCount.setValue(1);
            return;
        }
        else
            if (Integer.valueOf(subgroupCount.getValue().toString()) > 3) {
                subgroupCount.setValue(3);
                return;
            }
            else {
                weeks = 1;
                if (week.isSelected()) 
                    weeks = 2;                 
                //createJComboBox(weeks, Integer.valueOf(subgroupCount.getValue().toString()));
                subgroups = Integer.valueOf(subgroupCount.getValue().toString());
                updateEditPanel();

            }

    }
    
    public void init() {
        
        weekUpTitle = new JLabel("Верхняя неделя");
        weekUpTitle.setHorizontalAlignment(JLabel.CENTER);
        weekDownTitle = new JLabel("Нижняя неделя");
        weekDownTitle.setHorizontalAlignment(JLabel.CENTER);
        subgroupTitleUp = new JLabel[3];
        subgroupTitleDown = new JLabel[3];
        for (int i = 0; i < 3; ++i) {
            subgroupTitleUp[i] = new JLabel((i+1) + "-ая подгруппа");
            subgroupTitleUp[i].setHorizontalAlignment(JLabel.CENTER);
            subgroupTitleDown[i] = new JLabel((i+1) + "-ая подгруппа");
            subgroupTitleDown[i].setHorizontalAlignment(JLabel.CENTER);
        }
        
        jComboBoxPanelUp = new JPanel();
        jComboBoxPanelDown = new JPanel();
        
        jComboBoxPanels = new JPanel[6];
        
        jComboBoxPanelsLayout = new GridLayout(1,3);
        
        for (int i = 0; i < 6; ++i) {
            
            jComboBoxPanels[i] = new JPanel();
            
            jComboBoxPanels[i].add(subject[i]);
            jComboBoxPanels[i].add(teacher[i]);
            jComboBoxPanels[i].add(classroom[i]);
            
            jComboBoxPanels[i].setLayout(jComboBoxPanelsLayout);
            jComboBoxPanels[i].setBorder(new EmptyBorder(0,5,0,5));
            
        }
        
        jComboBoxPanelUp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        jComboBoxPanelDown.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));      
        
        subjects = new String[2][3];
        teachers = new String[2][3];
        
    }
    
    public void createJComboBox(int weeks, int subgroups) {
            
            jComboBoxPanel.removeAll();
            jComboBoxPanel.setLayout(null); 
            if (weeks == 2)
                jComboBoxPanelLayout = new GridLayout(4, 1);
            else
                jComboBoxPanelLayout = new GridLayout(1, 1);
            jComboBoxPanel.setLayout(jComboBoxPanelLayout);
            jComboBoxPanelLayoutUp = new GridLayout(2, subgroups);
            jComboBoxPanelLayoutDown = new GridLayout(2, subgroups);
            jComboBoxPanelUp.removeAll();
            jComboBoxPanelDown.removeAll();
            jComboBoxPanelUp.setLayout(jComboBoxPanelLayoutUp);
            jComboBoxPanelDown.setLayout(jComboBoxPanelLayoutDown);

            for (int i = 0; i < weeks; ++i) {
                for (int j = 0; j < subgroups; ++j)
                    if (i == 0)
                        jComboBoxPanelUp.add(subgroupTitleUp[j]);
                    else 
                        jComboBoxPanelDown.add(subgroupTitleDown[j]);
                for (int j = 0; j < subgroups; ++j) {

                        jComboBoxPanels[i*subgroups + j].setLayout(jComboBoxPanelsLayout);
                        jComboBoxPanels[i*subgroups + j].add(subject[i*subgroups + j]);
                        jComboBoxPanels[i*subgroups + j].add(teacher[i*subgroups + j]);
                        jComboBoxPanels[i*subgroups + j].add(classroom[i*subgroups + j]);

                        if (i == 0) {
                            jComboBoxPanelUp.add(jComboBoxPanels[i*subgroups + j]);
                        }
                        else {
                            jComboBoxPanelDown.add(jComboBoxPanels[i*subgroups + j]);
                        }
                    }
            }

            //weekUpTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            if (weeks == 2) 
                jComboBoxPanel.add(weekUpTitle);
            jComboBoxPanel.add(jComboBoxPanelUp);
            //weekDownTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            if (weeks == 2) {
                jComboBoxPanel.add(weekDownTitle);
                jComboBoxPanel.add(jComboBoxPanelDown);
            }
            
            jComboBoxPanel.revalidate();
            
    }
    
    public static void main(String[] argv) {
        JFrame frame = new JFrame();
        Connection connection = null;
        EditPanel ePanel = new EditPanel(connection);
        //ePanel.init();
        frame.add(ePanel.getPanel());
        
        frame.setVisible(true);
        frame.setSize(1000, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
