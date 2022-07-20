package prereqchecker;

import java.util.*;

/******************************************************************* 
Ahnaf Rahim 
ar1878@scarletmail.rutgers.edu
********************************************************************/

public class NewGraphClass 
{
    //hashmap to hold all the of courseIds and prereqs
    private HashMap<String, CourseArrList> coursesAndPreReqs;
   // boolean isPossible = false;

	// constructor
	public NewGraphClass(String file) {
        //make the hashmap
		coursesAndPreReqs = new HashMap<String, CourseArrList>();

        //read into the file and inputting the keys and values into the hashmaps 
        StdIn.setFile(file);
        int c1 = StdIn.readInt();
        StdIn.readLine();

        for(int i = 0 ; i < c1; i++)
        {
            String id = StdIn.readLine();
            coursesAndPreReqs.put(id, new CourseArrList(id));
        }

        int c2 = StdIn.readInt();
        StdIn.readLine();

        for(int i = 0; i < c2; i++)
        {
            coursesAndPreReqs.get(StdIn.readString()).addP(coursesAndPreReqs.get(StdIn.readString()));
        }
	}
    
    /**********************************************************************************************
     AdjList Methods 
    *********************************************************************************************/
    
    public void adjlistPrint(String file){
        //read the file 
        StdOut.setFile(file);

        //iterate through the hashmap in order to print the values of the hashmap
        for(CourseArrList course : coursesAndPreReqs.values()) {
            //printing the coursename
            StdOut.print(course.cname+" ");
            //going thru the values aka prereqs
            for(CourseArrList prereq : course.courseprereqs) {
                //printing the prereq name
                StdOut.print(prereq.cname+" ");
            }

            StdOut.println();
        }
    }
    /**********************************************************************************************
     ValidPrereq Methods 
    *********************************************************************************************/
    public void dfs(CourseArrList g, CourseArrList v){

        v.finishedCourse = true;
        if(v.courseprereqs.size() > 0){
            //iterate thru the prereqs
            for(CourseArrList prereq : v.courseprereqs){
                if(!prereq.finishedCourse) dfs(g,prereq); //recursive call for dfs 
            }
        }

        if(v.equals(g)) isPossible = false;
        
    }

    public void isValid(String f1, String f2){

        //read into the file 
        StdIn.setFile(f1);
        CourseArrList course1 = coursesAndPreReqs.get(StdIn.readLine());
        CourseArrList course2 = coursesAndPreReqs.get(StdIn.readLine());

        isPossible = true;
        //dfs through the graph
        dfs(course1, course2);
        StdOut.setFile(f2);

        //return YES or NO depending on if you can take it 
        if(isPossible == true) StdOut.println("YES");
        else StdOut.println("NO");
    }
    //set the boolean associated with that course ID to true. Now that we've explored its direct prerequisites, it's been properly visited / marked.
    boolean isPossible = true; 
   

    /**********************************************************************************************
     Eligible Methods 
    *********************************************************************************************/
    public void dfstwo(CourseArrList g2){
        g2.finishedCourse = true; 
        if(g2.courseprereqs.size() > 0){
            //iterate thru the prereqs
            for(CourseArrList prereq : g2.courseprereqs)
            {
                if(!prereq.finishedCourse) dfstwo(prereq); //recursive call for dfs 
            }
        }
    }

    public void areEligible(String f1, String f2){

        //eligible bool
        boolean eligible = true; 

        //reading into the file and get the number of courses 
        StdIn.setFile(f1);
        int numCourses = StdIn.readInt();
        StdIn.readLine();

        //use get method to put into hashmap
         for(int i = 0; i < numCourses; i++){
            dfstwo(coursesAndPreReqs.get(StdIn.readLine()));
        }

        //read file and check if the person will be eligible or not 
        StdOut.setFile(f2);
        for(CourseArrList cid : coursesAndPreReqs.values()){
            if(!cid.finishedCourse){ 
                //you are eligible ot take the course 
                eligible = true; 

            //iterating through the prereqs to see if you have finished them 
            for(CourseArrList pRq : cid.courseprereqs){
                if(!pRq.finishedCourse){
                    eligible = false; 
                    break;
                }
            }
        
            if(eligible == true) StdOut.println(cid.cname);
            }
        }
    }

    /**********************************************************************************************
     NeedToTake Methods 
    *********************************************************************************************/
    
    public void traverse(CourseArrList course){
        //print names of course not finished 
        if(!course.finishedCourse) StdOut.println(course.cname);
        
        course.finishedCourse=true;

        //following similar logic to dfs from before 
            if(course.courseprereqs.size()>0){
                for(CourseArrList prereq : course.courseprereqs){
                    if(!prereq.finishedCourse) traverse(prereq); //recur on the traverse method 
                }
            }
        }
    
    public void preReqsNeededToTake(String f1, String f2){
        
        //read into the file 
        StdIn.setFile(f1);
        //the target course we are aiming to take 
        CourseArrList target = coursesAndPreReqs.get(StdIn.readLine());
        //An integer d (the number of taken courses) which we are reading here 
        int numtakenCourses = StdIn.readInt();
        StdIn.readLine();

        //iterate through these courses 
        for(int i = 0; i < numtakenCourses; i++){
            dfstwo(coursesAndPreReqs.get(StdIn.readLine()));
        }

        StdOut.setFile(f2);
        target.finishedCourse=true;
        traverse(target);
    }

    /**********************************************************************************************
     SchedulePlan Methods 
    *********************************************************************************************/
    public int classPlan(HashMap<CourseArrList,Integer> planner, int sem, CourseArrList course){
       
        course.finishedCourse = true;
        int max = sem;

        planner.put(course, sem);
        if(course.courseprereqs.size() > 0){
            //go thru the prereqs of the courses 
            for(CourseArrList prereq : course.courseprereqs)
            {
                if(!prereq.finishedCourse || (planner.containsKey(prereq) && planner.get(prereq) <= sem)){
                max = Math.max(max, classPlan(planner, sem + 1, prereq));
                }
            }
        }
        return max;
    }

    public void plannedSchedule(String file1, String file2){

        //read into the file and create the target course 
        StdIn.setFile(file1);
        CourseArrList target = coursesAndPreReqs.get(StdIn.readLine());
        //An integer e (the number of taken courses)
        int e = StdIn.readInt();
        StdIn.readLine();

        //all prereqs gotta be completed and stuff 
        for(int i = 0; i < e; i++){
            String a = StdIn.readLine();
            dfstwo(coursesAndPreReqs.get(a));
        }

        HashMap<CourseArrList, Integer> courseplanner = new HashMap<CourseArrList, Integer>();
        int max = classPlan(courseplanner, -1, target);
        courseplanner.remove(target);
        ArrayList<ArrayList<CourseArrList>> planner = new ArrayList<ArrayList<CourseArrList>>();

        for(int i = 0; i <= max; i++){
            planner.add(new ArrayList<CourseArrList>());
        }

        for(Map.Entry<CourseArrList, Integer> map : courseplanner.entrySet()){
            planner.get(map.getValue()).add(map.getKey());
        }

        StdOut.setFile(file2);
        StdOut.println(planner.size());

        for(int i = planner.size() - 1; i >= 0; i--){
            for(CourseArrList c : planner.get(i)){
                StdOut.print(c.cname + " ");
            }
            StdOut.println();
       }
    }
}

    



