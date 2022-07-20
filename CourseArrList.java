package prereqchecker;

import java.util.*;


public class CourseArrList {
    
    ArrayList<CourseArrList> courseprereqs;
    boolean marked = false; 
    boolean finishedCourse = false; 
    String cname; 
    
    //Constructor 
    public CourseArrList(String name){
        cname = name;
        courseprereqs = new ArrayList<CourseArrList>();
    }

    public void addP(CourseArrList pre){
        courseprereqs.add(pre);
    }
}