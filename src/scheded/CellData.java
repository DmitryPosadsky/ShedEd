package scheded;

import javax.swing.JTextPane;

public class CellData {
    
    public int weekCount;
    public int groupCount;
    
    public int i;
    public int j;
    
    public int groupNumber; // Номер группы для editPanel => ok => course.
    
    JTextPane[][] cellTextPanes;
    
    String[][] teacher;
    String[][] subject;
    String[][] classroom;
    
    
    public CellData() {
        
        weekCount = 1;
        groupCount = 1;
        
        cellTextPanes = new JTextPane[weekCount][groupCount];
        
        teacher = new String[weekCount][groupCount];
        subject = new String[weekCount][groupCount];
        classroom = new String[weekCount][groupCount];
        
    }
    
    public CellData(int wCount, int gCount) {
        
        weekCount = wCount;
        groupCount = gCount;
        
        cellTextPanes = new JTextPane[weekCount][groupCount];
        
        teacher = new String[weekCount][groupCount];
        subject = new String[weekCount][groupCount];
        classroom = new String[weekCount][groupCount];
    }
    
    public CellData getCellData() {
        
        return this;
        
    }
    
}
