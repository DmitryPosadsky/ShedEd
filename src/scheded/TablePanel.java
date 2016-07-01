package scheded;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TablePanel {
    
    EditPanel editPanel;
    
    Connection connection;
    Statement statement = null;
    
    String selectLessonInfo;
    String selectSubLessonInfo;
    
    JPanel tablePanel;
    
    GridBagLayout tablePanelLayout;
    GridBagConstraints gridBagConst;
    
    GridBagLayout cellPanelLayout;
    GridBagConstraints cellGridBagConst;
    
    JLabel[] northLabel;
    JLabel[] westLabel;    
    
    JTextPane[][] textAreaMatrix;
    CellData[][] cellDataMatrix;
    
    int[] groups;
    
    
    int countString;
    int countColumn;
    
    int tableType;
    
    int dayNumber;
    int lessonNumber;
    int courseNumber;
    int groupNumber;
    int teacherNumber;
    
    
    
    String teacherFullName;
    String subject;
    int weekSplit;
    int weekCount;
    int subgroupCount;
    
 
    public TablePanel(Connection connect, EditPanel ep) { 
        
        connection = connect;
        
        editPanel = ep;
        
        tablePanel = new JPanel();
        
        countString = 6;
        countColumn = 7;
        
        courseNumber = 1;
        groupNumber = 1;
        
        tablePanelLayout = new GridBagLayout();
        tablePanel.setLayout(tablePanelLayout);
        gridBagConst = new GridBagConstraints();
        
        gridBagConst.fill = GridBagConstraints.BOTH;
        gridBagConst.anchor = GridBagConstraints.CENTER;
        gridBagConst.insets = new Insets(0, 0, 0, 0);
        gridBagConst.weightx = 1;
        gridBagConst.weighty = 1;
        gridBagConst.gridx = 0;
        gridBagConst.gridy = 0;
        
        
        cellPanelLayout = new GridBagLayout();
        //tablePanel.setLayout(tablePanelLayout);
        cellGridBagConst = new GridBagConstraints();
        
        cellGridBagConst.fill = GridBagConstraints.BOTH;
        cellGridBagConst.anchor = GridBagConstraints.CENTER;
        cellGridBagConst.insets = new Insets(0, 0, 0, 0);
        cellGridBagConst.weightx = 1;
        cellGridBagConst.weighty = 1;
        cellGridBagConst.gridx = 0;
        cellGridBagConst.gridy = 0;
        
        createTable();

    }
   
    public void createTable() {
        
    /*
        northLabel = new JLabel[countColumn+1];
        westLabel = new JLabel[countString+1];    
        
        textAreaMatrix = new JTextPane[countString][countColumn];
        cellDataMatrix = new CellData[countString][countColumn];
        
        gridBagConst.fill = GridBagConstraints.NONE;
        
        for (int j = 1; j <= countColumn; ++j) {
            northLabel[j] = new JLabel();
            northLabel[j].setSize(10, 10);
            
            gridBagConst.gridx = j;
            gridBagConst.gridy = 0;
            
            tablePanel.add(northLabel[j], gridBagConst);
        }
       
        northLabel[1].setText("Понедельник");
        northLabel[2].setText("Вторник");
        northLabel[3].setText("Среда");
        northLabel[4].setText("Четверг");
        northLabel[5].setText("Пятница");
        northLabel[6].setText("Суббота");
        
        northLabel[1].setHorizontalAlignment(JLabel.CENTER);
        northLabel[2].setHorizontalAlignment(JLabel.CENTER);
        northLabel[3].setHorizontalAlignment(JLabel.CENTER);
        northLabel[4].setHorizontalAlignment(JLabel.CENTER);
        northLabel[5].setHorizontalAlignment(JLabel.CENTER);
        northLabel[6].setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 1; i <= countString; ++i) {
            westLabel[i] = new JLabel();
            
            gridBagConst.gridx = 0;
            gridBagConst.gridy = i;
            
            tablePanel.add(westLabel[i], gridBagConst);
        }
             
        updateGroupLabels();
                
        gridBagConst.fill = GridBagConstraints.BOTH;
        gridBagConst.ipadx = 1200/countString;
        gridBagConst.ipady = 500/countString;
       
        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
 
                textAreaMatrix[i][j] = new JTextPane();
                
                textAreaMatrix[i][j].setEditable(false);
              
                gridBagConst.gridx = j;
                gridBagConst.gridy = i;
                
                tablePanel.add(textAreaMatrix[i][j], gridBagConst);
                
            }
            
        }
           
        updateTable();
               */
        
        JLabel label = new JLabel("Главная страница (в разработке)");
        label.setFont(new Font("Zopa", 40, 40));
        tablePanel.add(label);

        
    }
     
    public void updateTable() {
        
        switch ( tableType ) {
            
            case 1: { updateTableCourse(); break; }
            case 2: { updateTableGroup(); break; }
            case 3: { updateTableTeacher(); break; }
            case 4: { updateTableClassroom(); break; }
                
            default:
                
        } 

        tablePanel.updateUI();
              
    }
    
    public void updateTableCourse() {
        
        int groupNum = 0;
        ResultSet rs;
        
        tablePanel.removeAll();
        tablePanel.setLayout(tablePanelLayout);
        
        try {
            
            statement = connection.createStatement();

            selectLessonInfo = "select distinct count(group_num) from study_groups "
                    + "where course_num = " + courseNumber + ";";
            rs = statement.executeQuery(selectLessonInfo);
  
            while (rs.next()) {
                countColumn = Integer.valueOf(rs.getString("count"));
            }
            
            countColumn++;

            groups = new int[countColumn];
            
            
            selectLessonInfo = "select distinct group_num from study_groups "
                    + "where course_num = " + courseNumber + " order by group_num;";
            rs = statement.executeQuery(selectLessonInfo);
  
            while (rs.next()) {
                groups[groupNum] = Integer.valueOf(rs.getString("group_num"));
                groupNum++;
                
            }

        } catch (SQLException e) {
            countColumn = 0;
            groups = new int[0];
            System.out.println(e.getMessage());
                    
        }
        
        updateCourseLabels();
                
        textAreaMatrix = new JTextPane[countString][countColumn];
        cellDataMatrix = new CellData[countString][countColumn];
        
        //System.out.println(countString + " " + countColumn);
        
        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
 
                //System.out.println(i + " " + j);
                textAreaMatrix[i][j] = new JTextPane();
                
                try {
                    statement = connection.createStatement();

                    selectLessonInfo = "select subgroup_number," +
                        "subgroup_count," +
                        "week_split," +
                        "les_type," +
                        "notice, " +
                        "elective_course," +
                        "kind_of_department," +
                        "full_name," +
                        "subject," +
                        "semester from getLessonInfo5(" + dayNumber + "," + i + 
                            "," + courseNumber + "," + groups[j-1] + ");";
                    rs = statement.executeQuery(selectLessonInfo);
  
                    //System.out.println(dayNumber + " " + i + " " + courseNumber + " " + groups[j-1]);
                    
                    while (rs.next()) {
                        if (rs.getString("week_split") != null)
                            weekSplit = Integer.valueOf(rs.getString("week_split"));
                        else
                            weekSplit = 0;
                        
                        if (rs.getString("subgroup_count") != null)
                            subgroupCount = Integer.valueOf(rs.getString("subgroup_count"));
                        else
                            subgroupCount = 1;

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    
                }

                if (weekSplit == 0) 
                    weekCount = 1;
                else 
                    weekCount = 2;
 
                cellDataMatrix[i][j] = new CellData(weekCount, subgroupCount);
                
                
                textAreaMatrix[i][j].removeAll();
                GridLayout textAreaLayout = new GridLayout(weekCount, subgroupCount);
                textAreaMatrix[i][j].setLayout(textAreaLayout);
                
                //System.out.println(subgroupCount + " " + weekCount);
                
                Font ArialMin = new Font("Arial", Font.PLAIN, 10);
                Font ArialBig = new Font("Arial", Font.PLAIN, 15);
                
                for (int k = 0; k < cellDataMatrix[i][j].weekCount; ++k) {
                    for (int s = 0; s < cellDataMatrix[i][j].groupCount; ++s) {
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s] = new JTextPane();
                        cellDataMatrix[i][j].cellTextPanes[k][s].setBorder(BorderFactory.createLineBorder(Color.gray));
                        
                        //Выравнивание по центру
                        StyledDocument doc = cellDataMatrix[i][j].cellTextPanes[k][s].getStyledDocument();
                        SimpleAttributeSet center = new SimpleAttributeSet();
                        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                        doc.setParagraphAttributes(0, doc.getLength(), center, false);

                        
                        try {
                            statement = connection.createStatement();
                            selectSubLessonInfo = "select l.subgroup_num, "
                                + "l.subgroup_count, l.week_split, l.les_type,"
                                + "l.notice, l.elective_course, "
                                + "l.kind_of_department, t.full_name, "
                                + "sub.subject, l.semester from lessons l, "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + dayNumber
                                + " and les.less_num = " + i 
                                + " and s.course_num = " + courseNumber
                                + " and s.group_num = " + groups[j-1] 
                                + " and l.week_split = " + weekHelper(k, weekSplit)
                                + " and l.subgroup_num = " + (s + 1)
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";
                        
                            rs = statement.executeQuery(selectSubLessonInfo);
  
                            while (rs.next()) {
                                
                                teacherFullName = rs.getString("full_name");
                                subject = rs.getString("subject");
                                
                                cellDataMatrix[i][j].teacher[k][s] = teacherFullName;
                                cellDataMatrix[i][j].subject[k][s] = subject;
                                
                                //System.out.println(teacherFullName + " 111 " + subject);

                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        
                        cellDataMatrix[i][j].i = i;
                        cellDataMatrix[i][j].j = j;
                        cellDataMatrix[i][j].groupNumber = groups[j-1];
                        
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].setText("");
                        if (subgroupCount > 1) 
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialMin);
                        else
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialBig);
                        
                        if (teacherFullName != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(subject +  "\n");
                        if (subject != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(cellDataMatrix[i][j].cellTextPanes[k][s].getText() + teacherFullName); 
                        
                        cellGridBagConst.gridx = k;
                        cellGridBagConst.gridy = s;

                        textAreaMatrix[i][j].add(cellDataMatrix[i][j].cellTextPanes[k][s], gridBagConst);
                        
                        teacherFullName = "";
                        subject = "";                        

                        cellDataMatrix[i][j].cellTextPanes[k][s].setEditable(false);
                     
                        final CellData cd = cellDataMatrix[i][j];
                        final int f_i = i;
                        final int f_j = j;
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].addFocusListener(new FocusListener() {
                            
                            public void focusGained(FocusEvent fe) {
                                
                                editPanel.setLessonInfo(cd);
                                for (int m = 1; m < countString; ++m) {
                                    for (int n = 1; n < countColumn; ++n) {
                                        setCellBackground(Color.white, m, n);
                                    }
                                }
                                setCellBackground(Color.yellow, f_i, f_j);
                               
                            }

                            public void focusLost(FocusEvent fe) { }

                        });
   
                    }
                    
                }
 
            }
            
        }

        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
                
                textAreaMatrix[i][j].setEditable(false);
              
                gridBagConst.gridx = j;
                gridBagConst.gridy = i;
                
                tablePanel.add(textAreaMatrix[i][j], gridBagConst);
                
            }
            
        }
        
    }
    
    public void updateTableGroup() {
        
        ResultSet rs;
        
        tablePanel.removeAll();
        tablePanel.setLayout(tablePanelLayout);
        
        textAreaMatrix = new JTextPane[countString][countColumn];
        cellDataMatrix = new CellData[countString][countColumn]; 
        
        updateGroupLabels();
        
        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
 
                textAreaMatrix[i][j] = new JTextPane();
                
                try {
                    statement = connection.createStatement();

                    selectLessonInfo = "select subgroup_number," +
                        "subgroup_count," +
                        "week_split," +
                        "les_type," +
                        "notice, " +
                        "elective_course," +
                        "kind_of_department," +
                        "full_name," +
                        "subject," +
                        "semester from getLessonInfo5(" + j + "," + i + 
                            "," + courseNumber + "," + groupNumber + ");";
                    rs = statement.executeQuery(selectLessonInfo);
  
                    while (rs.next()) {
                        if (rs.getString("week_split") != null)
                            weekSplit = Integer.valueOf(rs.getString("week_split"));
                        else
                            weekSplit = 0;
                        
                        if (rs.getString("subgroup_count") != null)
                            subgroupCount = Integer.valueOf(rs.getString("subgroup_count"));
                        else
                            subgroupCount = 1;
                        //teacherFullName = rs.getString("full_name");
                        //subject = rs.getString("subject");

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    
                }

                if (weekSplit == 0) 
                    weekCount = 1;
                else 
                    weekCount = 2;
 
                cellDataMatrix[i][j] = new CellData(weekCount, subgroupCount);
                
                
                textAreaMatrix[i][j].removeAll();
                GridLayout textAreaLayout = new GridLayout(weekCount, subgroupCount);
                textAreaMatrix[i][j].setLayout(textAreaLayout);
                               
                Font ArialMin = new Font("Arial", Font.PLAIN, 10);
                Font ArialBig = new Font("Arial", Font.PLAIN, 15);
                
                for (int k = 0; k < cellDataMatrix[i][j].weekCount; ++k) {
                    for (int s = 0; s < cellDataMatrix[i][j].groupCount; ++s) {
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s] = new JTextPane();
                        cellDataMatrix[i][j].cellTextPanes[k][s].setBorder(BorderFactory.createLineBorder(Color.gray));
                        
                        //Выравнивание по центру
                        StyledDocument doc = cellDataMatrix[i][j].cellTextPanes[k][s].getStyledDocument();
                        SimpleAttributeSet center = new SimpleAttributeSet();
                        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                        doc.setParagraphAttributes(0, doc.getLength(), center, false);

                        
                        try {
                            statement = connection.createStatement();
                            selectSubLessonInfo = "select l.subgroup_num, "
                                + "l.subgroup_count, l.week_split, l.les_type,"
                                + "l.notice, l.elective_course, "
                                + "l.kind_of_department, t.full_name, "
                                + "sub.subject, l.semester from lessons l, "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + j
                                + " and les.less_num = " + i 
                                + " and s.course_num = " + courseNumber
                                + " and s.group_num = " + groupNumber 
                                + " and l.week_split = " + weekHelper(k, weekSplit)
                                + " and l.subgroup_num = " + (s + 1)
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";
                        
                            rs = statement.executeQuery(selectSubLessonInfo);
  
                            while (rs.next()) {
                                
                                teacherFullName = rs.getString("full_name");
                                subject = rs.getString("subject");
                                
                                cellDataMatrix[i][j].teacher[k][s] = teacherFullName;
                                cellDataMatrix[i][j].subject[k][s] = subject;
                                
                                //System.out.println(teacherFullName + " 111 " + subject);

                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        
                        cellDataMatrix[i][j].i = i;
                        cellDataMatrix[i][j].j = j;
                        
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].setText("");
                        if (subgroupCount > 1) 
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialMin);
                        else
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialBig);
                        
                        if (teacherFullName != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(subject +  "\n");
                        if (subject != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(cellDataMatrix[i][j].cellTextPanes[k][s].getText() + teacherFullName); 
                        
                        cellGridBagConst.gridx = k;
                        cellGridBagConst.gridy = s;

                        textAreaMatrix[i][j].add(cellDataMatrix[i][j].cellTextPanes[k][s], gridBagConst);
                        
                        teacherFullName = "";
                        subject = "";                        

                        cellDataMatrix[i][j].cellTextPanes[k][s].setEditable(false);
                     
                        final CellData cd = cellDataMatrix[i][j];
                        final int f_i = i;
                        final int f_j = j;
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].addFocusListener(new FocusListener() {
                            
                            public void focusGained(FocusEvent fe) {
                                
                                editPanel.setLessonInfo(cd);
                                for (int m = 1; m < countString; ++m) {
                                    for (int n = 1; n < countColumn; ++n) {
                                        setCellBackground(Color.white, m, n);
                                    }
                                }
                                setCellBackground(Color.yellow, f_i, f_j);
                               
                            }

                            public void focusLost(FocusEvent fe) { }

                        });
   
                    }
                    
                }
 
            }
            
        }
        
        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
                
                textAreaMatrix[i][j].setEditable(false);
              
                gridBagConst.gridx = j;
                gridBagConst.gridy = i;
                
                tablePanel.add(textAreaMatrix[i][j], gridBagConst);
                
            }
            
        }
        
    }
    
    public void updateTableTeacher() {
        
                

        ResultSet rs;
        
        tablePanel.removeAll();
        tablePanel.setLayout(tablePanelLayout);
        
        //updateCourseLabels();
                       
        textAreaMatrix = new JTextPane[countString][countColumn];
        cellDataMatrix = new CellData[countString][countColumn];
        
        //System.out.println(countString + " " + countColumn);
        
        updateTeacherLabels();
        
        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
 
                //System.out.println(i + " " + j);
                textAreaMatrix[i][j] = new JTextPane();
                
                try {
                    statement = connection.createStatement();

                    selectLessonInfo = "select l.subgroup_num, "
                                + "l.subgroup_count, l.week_split, l.les_type,"
                                + "l.notice, l.elective_course, "
                                + "l.kind_of_department, t.full_name, "
                                + "sub.subject, l.semester from lessons l, "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + j
                                + " and les.less_num = " + i 
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and t.id = " + teacherNumber
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";
                    
                    rs = statement.executeQuery(selectLessonInfo);
  
                    //System.out.println(i + " " + j + " " + courseNumber + " " + teacherNumber);
                    
                    weekSplit = 0;
                    subgroupCount = 1;
                    
                    while (rs.next()) {

                        if (rs.getString("week_split") != null)
                            weekSplit = Integer.valueOf(rs.getString("week_split"));
                        else
                            weekSplit = 0;
                        
                        if (rs.getString("subgroup_count") != null)
                            subgroupCount = Integer.valueOf(rs.getString("subgroup_count"));
                        else
                            subgroupCount = 1;

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    
                }

                if (weekSplit == 0) 
                    weekCount = 1;
                else 
                    weekCount = 2;
 
                cellDataMatrix[i][j] = new CellData(weekCount, subgroupCount);
                
                System.out.println(weekCount + " " + subgroupCount);
                
                textAreaMatrix[i][j].removeAll();
                GridLayout textAreaLayout = new GridLayout(weekCount, subgroupCount);
                textAreaMatrix[i][j].setLayout(textAreaLayout);
                
                //System.out.println(subgroupCount + " " + weekCount);
                
                Font ArialMin = new Font("Arial", Font.PLAIN, 10);
                Font ArialBig = new Font("Arial", Font.PLAIN, 15);
                
                for (int k = 0; k < cellDataMatrix[i][j].weekCount; ++k) {
                    for (int s = 0; s < cellDataMatrix[i][j].groupCount; ++s) {
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s] = new JTextPane();
                        cellDataMatrix[i][j].cellTextPanes[k][s].setBorder(BorderFactory.createLineBorder(Color.gray));
                        
                        //Выравнивание по центру
                        StyledDocument doc = cellDataMatrix[i][j].cellTextPanes[k][s].getStyledDocument();
                        SimpleAttributeSet center = new SimpleAttributeSet();
                        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                        doc.setParagraphAttributes(0, doc.getLength(), center, false);

                        
                        try {
                            statement = connection.createStatement();
                            selectSubLessonInfo = "select l.subgroup_num, "
                                + "l.subgroup_count, l.week_split, l.les_type,"
                                + "l.notice, l.elective_course, "
                                + "l.kind_of_department, t.full_name, "
                                + "sub.subject, l.semester from lessons l, "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + j
                                + " and les.less_num = " + i 
                                + " and t.id = " + teacherNumber 
                                + " and l.week_split = " + weekHelper(k, weekSplit)
                                + " and l.subgroup_num = " + (s + 1)
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";
                        
                            rs = statement.executeQuery(selectSubLessonInfo);
  
                            while (rs.next()) {
                                
                                teacherFullName = rs.getString("full_name");
                                subject = rs.getString("subject");
                                
                                cellDataMatrix[i][j].teacher[k][s] = teacherFullName;
                                cellDataMatrix[i][j].subject[k][s] = subject;
                                
                                //System.out.println(teacherFullName + " 111 " + subject);

                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        
                        cellDataMatrix[i][j].i = i;
                        cellDataMatrix[i][j].j = j;
                        
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].setText("");
                        if (subgroupCount > 1) 
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialMin);
                        else
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialBig);
                        
                        if (teacherFullName != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(subject +  "\n");
                        if (subject != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(cellDataMatrix[i][j].cellTextPanes[k][s].getText() + teacherFullName); 
                        
                        cellGridBagConst.gridx = k;
                        cellGridBagConst.gridy = s;

                        textAreaMatrix[i][j].add(cellDataMatrix[i][j].cellTextPanes[k][s], gridBagConst);
                        
                        teacherFullName = "";
                        subject = "";                        

                        cellDataMatrix[i][j].cellTextPanes[k][s].setEditable(false);
                     
                        final CellData cd = cellDataMatrix[i][j];
                        final int f_i = i;
                        final int f_j = j;
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].addFocusListener(new FocusListener() {
                            
                            public void focusGained(FocusEvent fe) {
                                
                                editPanel.setLessonInfo(cd);
                                for (int m = 1; m < countString; ++m) {
                                    for (int n = 1; n < countColumn; ++n) {
                                        setCellBackground(Color.white, m, n);
                                    }
                                }
                                setCellBackground(Color.yellow, f_i, f_j);
                               
                            }

                            public void focusLost(FocusEvent fe) { }

                        });
   
                    }
                    
                }
 
            }
            
        }

        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
                
                textAreaMatrix[i][j].setEditable(false);
              
                gridBagConst.gridx = j;
                gridBagConst.gridy = i;
                
                tablePanel.add(textAreaMatrix[i][j], gridBagConst);
                
            }
            
        }
        
        
    }
    
    public void updateTableClassroom() {
        
                
        int groupNum = 0;
        ResultSet rs;
        
        tablePanel.removeAll();
        tablePanel.setLayout(tablePanelLayout);
        
        try {
            
            statement = connection.createStatement();

            selectLessonInfo = "select distinct count(group_num) from study_groups "
                    + "where course_num = " + courseNumber + ";";
            rs = statement.executeQuery(selectLessonInfo);
  
            while (rs.next()) {
                countColumn = Integer.valueOf(rs.getString("count"));
            }
            
            countColumn++;

            groups = new int[countColumn];
            
            
            selectLessonInfo = "select distinct group_num from study_groups "
                    + "where course_num = " + courseNumber + " order by group_num;";
            rs = statement.executeQuery(selectLessonInfo);
  
            while (rs.next()) {
                groups[groupNum] = Integer.valueOf(rs.getString("group_num"));
                groupNum++;
                
            }
            
           // updateCourseLabels();
            
        } catch (SQLException e) {
            countColumn = 0;
            groups = new int[0];
            System.out.println(e.getMessage());
                    
        }
        
                
        textAreaMatrix = new JTextPane[countString][countColumn];
        cellDataMatrix = new CellData[countString][countColumn];
        
        //System.out.println(countString + " " + countColumn);
        
        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
 
                //System.out.println(i + " " + j);
                textAreaMatrix[i][j] = new JTextPane();
                
                try {
                    statement = connection.createStatement();

                    selectLessonInfo = "select subgroup_number," +
                        "subgroup_count," +
                        "week_split," +
                        "les_type," +
                        "notice, " +
                        "elective_course," +
                        "kind_of_department," +
                        "full_name," +
                        "subject," +
                        "semester from getLessonInfo5(" + dayNumber + "," + i + 
                            "," + courseNumber + "," + groups[j-1] + ");";
                    rs = statement.executeQuery(selectLessonInfo);
  
                    System.out.println(dayNumber + " " + i + " " + courseNumber + " " + groups[j-1]);
                    
                    while (rs.next()) {
                        if (rs.getString("week_split") != null)
                            weekSplit = Integer.valueOf(rs.getString("week_split"));
                        else
                            weekSplit = 0;
                        
                        if (rs.getString("subgroup_count") != null)
                            subgroupCount = Integer.valueOf(rs.getString("subgroup_count"));
                        else
                            subgroupCount = 1;

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    
                }

                if (weekSplit == 0) 
                    weekCount = 1;
                else 
                    weekCount = 2;
 
                cellDataMatrix[i][j] = new CellData(weekCount, subgroupCount);
                
                
                textAreaMatrix[i][j].removeAll();
                GridLayout textAreaLayout = new GridLayout(weekCount, subgroupCount);
                textAreaMatrix[i][j].setLayout(textAreaLayout);
                
                System.out.println(subgroupCount + " " + weekCount);
                
                Font ArialMin = new Font("Arial", Font.PLAIN, 10);
                Font ArialBig = new Font("Arial", Font.PLAIN, 15);
                
                for (int k = 0; k < cellDataMatrix[i][j].weekCount; ++k) {
                    for (int s = 0; s < cellDataMatrix[i][j].groupCount; ++s) {
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s] = new JTextPane();
                        cellDataMatrix[i][j].cellTextPanes[k][s].setBorder(BorderFactory.createLineBorder(Color.gray));
                        
                        //Выравнивание по центру
                        StyledDocument doc = cellDataMatrix[i][j].cellTextPanes[k][s].getStyledDocument();
                        SimpleAttributeSet center = new SimpleAttributeSet();
                        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                        doc.setParagraphAttributes(0, doc.getLength(), center, false);

                        
                        try {
                            statement = connection.createStatement();
                            selectSubLessonInfo = "select l.subgroup_num, "
                                + "l.subgroup_count, l.week_split, l.les_type,"
                                + "l.notice, l.elective_course, "
                                + "l.kind_of_department, t.full_name, "
                                + "sub.subject, l.semester from lessons l, "
                                + "curriculum c, study_groups s, subjects sub, "
                                + "teachers t, lesson_time les where l.days= " + dayNumber
                                + " and les.less_num = " + i 
                                + " and s.course_num = " + courseNumber
                                + " and s.group_num = " + groups[j-1] 
                                + " and l.week_split = " + weekHelper(k, weekSplit)
                                + " and l.subgroup_num = " + (s + 1)
                                + " and s.id = c.study_group_id"
                                + " and sub.id = c.subject_id and t.id = c.teacher_id"
                                + " and c.id = l.curriculumid and les.id = l.lesson_time_id;";
                        
                            rs = statement.executeQuery(selectSubLessonInfo);
  
                            while (rs.next()) {
                                
                                teacherFullName = rs.getString("full_name");
                                subject = rs.getString("subject");
                                
                                cellDataMatrix[i][j].teacher[k][s] = teacherFullName;
                                cellDataMatrix[i][j].subject[k][s] = subject;
                                
                                //System.out.println(teacherFullName + " 111 " + subject);

                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        
                        cellDataMatrix[i][j].i = i;
                        cellDataMatrix[i][j].j = j;
                        
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].setText("");
                        if (subgroupCount > 1) 
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialMin);
                        else
                            cellDataMatrix[i][j].cellTextPanes[k][s].setFont(ArialBig);
                        
                        if (teacherFullName != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(subject +  "\n");
                        if (subject != null)
                            cellDataMatrix[i][j].cellTextPanes[k][s].setText(cellDataMatrix[i][j].cellTextPanes[k][s].getText() + teacherFullName); 
                        
                        cellGridBagConst.gridx = k;
                        cellGridBagConst.gridy = s;

                        textAreaMatrix[i][j].add(cellDataMatrix[i][j].cellTextPanes[k][s], gridBagConst);
                        
                        teacherFullName = "";
                        subject = "";                        

                        cellDataMatrix[i][j].cellTextPanes[k][s].setEditable(false);
                     
                        final CellData cd = cellDataMatrix[i][j];
                        final int f_i = i;
                        final int f_j = j;
                        
                        cellDataMatrix[i][j].cellTextPanes[k][s].addFocusListener(new FocusListener() {
                            
                            public void focusGained(FocusEvent fe) {
                                
                                editPanel.setLessonInfo(cd);
                                for (int m = 1; m < countString; ++m) {
                                    for (int n = 1; n < countColumn; ++n) {
                                        setCellBackground(Color.white, m, n);
                                    }
                                }
                                setCellBackground(Color.yellow, f_i, f_j);
                               
                            }

                            public void focusLost(FocusEvent fe) { }

                        });
   
                    }
                    
                }
 
            }
            
        }

        for (int i = 1; i < countString; ++i) {
            
            for (int j = 1; j < countColumn; ++j) {
                
                textAreaMatrix[i][j].setEditable(false);
              
                gridBagConst.gridx = j;
                gridBagConst.gridy = i;
                
                tablePanel.add(textAreaMatrix[i][j], gridBagConst);
                
            }
            
        }
        
        
    }
    
    public int weekHelper(int k, int weekSplit) {
        
        if (weekSplit == 0) 
            return 0;
        else
            return k+1;
        
    }
    
    public void updateCourseLabels() {
        
        northLabel = new JLabel[countColumn];
        westLabel = new JLabel[countString];
        
        for (int i = 1; i < countColumn; ++i) {
            northLabel[i] = new JLabel(groups[i-1] + " группа");
            northLabel[i].setHorizontalAlignment(JLabel.CENTER);
            
            gridBagConst.gridx = i;
            gridBagConst.gridy = 0;
            
            tablePanel.add(northLabel[i], gridBagConst);
        }
        
        for (int i = 1; i < countString; ++i) {
            westLabel[i] = new JLabel(i + " пара");
            westLabel[i].setHorizontalAlignment(JLabel.CENTER);
            
            gridBagConst.gridx = 0;
            gridBagConst.gridy = i;
            
            tablePanel.add(westLabel[i], gridBagConst);
        }
        
    }
    
    public void updateGroupLabels() {
        
        northLabel = new JLabel[countColumn];
        westLabel = new JLabel[countString];
        
        for (int i = 1; i < countColumn; ++i) {
            
            northLabel[i] = new JLabel();
            northLabel[i].setHorizontalAlignment(JLabel.CENTER);
            
            gridBagConst.gridx = i;
            gridBagConst.gridy = 0;
            
            tablePanel.add(northLabel[i], gridBagConst);
        }
        
        northLabel[1].setText("Понедельник");
        northLabel[2].setText("Вторник");
        northLabel[3].setText("Среда");
        northLabel[4].setText("Четверг");
        northLabel[5].setText("Пятница");
        northLabel[6].setText("Суббота");
        
        for (int i = 1; i < countString; ++i) {
            westLabel[i] = new JLabel(i + " пара");
            westLabel[i].setHorizontalAlignment(JLabel.CENTER);
            
            gridBagConst.gridx = 0;
            gridBagConst.gridy = i;
            
            tablePanel.add(westLabel[i], gridBagConst);
        }
        
    }
    
    public void updateTeacherLabels() {
        
        updateGroupLabels();
        
    }
    
    public void updateClassroomLabels() {
        
        updateGroupLabels();
        
    }
      
    public JPanel getPanel() {
        return tablePanel;
    }
    
    public int getTableType() {        
        return tableType;       
    }
    
    public void setCellBackground(Color color, int i, int j) {        
        textAreaMatrix[i][j].setBackground(color);
    }
    
    public void setTableType(int tType) {
        tableType = tType;
    }
    
    public void setDayNumber(int dNumber) {
        dayNumber = dNumber;
    }
    
    public void setLessonNumber(int lNumber) {
        lessonNumber = lNumber;
    }
    
    public void setCourseNumber(int cNumber) {
        courseNumber = cNumber;
    }
    
    public void setGroupNumber(int gNumber) {
        groupNumber = gNumber;
    }
    
    public void setTeacherNumber(int tNumber) {
        teacherNumber = tNumber;
    }
    
    public void setStringCount(int sCount) {
        countString = sCount;
    }
    
    public void setColumnCount(int cCount) {
        countColumn = cCount;
    }
    
    
    public static void main(String[] argv) {
        
        JFrame tableFrame = new JFrame();
        //TablePanel tablePanel = new TablePanel();
        //tablePanel.init();
        //tableFrame.add(tablePanel.getPanel());
        //tableFrame.add(tablePanel.getPanel());
        
        tableFrame.setSize(800, 800);
        tableFrame.setVisible(true);
        tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
    }
}
