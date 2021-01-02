package Assignment2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.io.*;


/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to 
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution 
with anybody. I affirm that I have read and understood
the Senate Policy on Academic honesty at 
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a 
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name: Mohammed Fulwala
Student Number: 217459744
Course Section: E
*/

/**
* This class generates a transcript for each student, whose information is in the text file.
*/

public class Transcript {
	private ArrayList<Object> grade = new ArrayList<Object>();
	private File inputFile;
	private String outputFile;
	
	/**
	 * This the the constructor for Transcript class that 
	 * initializes its instance variables and call readFie private
	 * method to read the file and construct this.grade.
	 * @param inFile is the name of the input file.
	 * @param outFile is the name of the output file.
	 */
	public Transcript(String inFile, String outFile) {
		inputFile = new File(inFile);	
		outputFile = outFile;	
		grade = new ArrayList<Object>();
		this.readFile();
	}// end of Transcript constructor

	/** 
	 * This method reads a text file and add each line as 
	 * an entry of grade ArrayList.
	 * @exception It throws FileNotFoundException if the file is not found.
	 */
	private void readFile() {
		Scanner sc = null; 
		try {
			sc = new Scanner(inputFile);	
			while(sc.hasNextLine()){
				grade.add(sc.nextLine());
	        }      
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			sc.close();
		}		
	} // end of readFile
	

	/**
	 * This is a method that builds an ArrayList of type Student. It uses the instance variable grade, which contains every line from the text file provided,
	 * and converts it to a String. That string is then passed to the helper method readLineToStudent. 
	 * In the end, the method returns the complete ArrayList of type Student.
	 * @return student, which is an ArrayList of type Student.
	 * @throws InvalidTotalException if the sum of weights is not equal to 100 or the sum of grades of all assessments is greater than 100.
	 */
	public ArrayList<Student> buildStudentArray() {
		ArrayList<Student> student = new ArrayList<>();
		Set<Integer> id = new HashSet<>();
		for (Object obj:grade) {
			String line = (String) obj;
			readLineToStudent(line, id, student);
		}
		return student;
	}


	/**
	 * This is the helper method for buildStudentArray(). 
	 * This method reads one line at a time from the text file, extracts all the details individually, and stores them in that student.
	 * It also makes sure that no student is duplicated. 
	 * @param line, of type String, which is the line passed from buildStudentArray().
	 * @param id, a Set of type Integer, which is an empty set. The purpose is to store the id's of the students as the method runs so we can use it to prevent
	 * duplication of students later on.
	 * @param student, which is an ArrayList of type Student. The purpose is to add the student objects as the method runs. 
	 * This variable is then returned in buildStudentArray().
	 * @throws InvalidTotalException if the sum of weights is not equal to 100 or the sum of grades of all assessments is greater than 100.
	 */
	private void readLineToStudent(String line, Set<Integer> id, ArrayList<Student> student) {
		String[] elements = line.split(",");
		ArrayList<Assessment> assessment = new ArrayList<>();
		ArrayList<Double> grade = new ArrayList<>();
		ArrayList<Integer> weight = new ArrayList<>();
		int previousSize = id.size();
		id.add(Integer.parseInt(elements[2]));
		int newSize = id.size();
		for (int i = 3; i < elements.length - 1; i++) {
			if (elements[i].charAt(2) == '(') { 
				assessment.add(Assessment.getInstance(elements[i].charAt(0), Integer.parseInt(elements[i].substring(1, 2))));
				if (elements[i].charAt(4) == ')') grade.add(Double.parseDouble(elements[i].substring(3, 4)));
				else if (elements[i].charAt(5) == ')') grade.add(Double.parseDouble(elements[i].substring(3, 5)));
				else grade.add(Double.parseDouble(elements[i].substring(3, 6)));
				weight.add(Integer.parseInt(elements[i].substring(1,2)));
			}
			else if (elements[i].charAt(3) == '(') { 
				assessment.add(Assessment.getInstance(elements[i].charAt(0), Integer.parseInt(elements[i].substring(1, 3))));
				if (elements[i].charAt(5) == ')') grade.add(Double.parseDouble(elements[i].substring(4, 5)));
				else if (elements[i].charAt(6) == ')') grade.add(Double.parseDouble(elements[i].substring(4, 6)));
				else grade.add(Double.parseDouble(elements[i].substring(4, 7)));
				weight.add(Integer.parseInt(elements[i].substring(1,3)));
			}
			else { 
				assessment.add(Assessment.getInstance(elements[i].charAt(0), Integer.parseInt(elements[i].substring(1, 4))));
				if (elements[i].charAt(6) == ')') grade.add(Double.parseDouble(elements[i].substring(5, 6)));
				else if (elements[i].charAt(7) == ')') grade.add(Double.parseDouble(elements[i].substring(5, 7)));
				else grade.add(Double.parseDouble(elements[i].substring(5, 8)));
				weight.add(Integer.parseInt(elements[i].substring(1,4)));
			}
			
		}
		Student s = null;
		if (previousSize < newSize) {
			s = new Student(elements[2], elements[elements.length - 1] , new ArrayList<Course>());
			s.addCourse(new Course(elements[0], assessment, Double.parseDouble(elements[1])));
			s.addGrade(grade, weight);
			student.add(s);
		}
		else {
			for (Student loopStudent: student) {
				if (elements[2].equals(loopStudent.getStudentID())) {
					s = loopStudent;
					break;
				} 
			}
			s.addCourse(new Course(elements[0], assessment, Double.parseDouble(elements[1])));
			s.addGrade(grade, weight);
		}
	}

	
	/**
	 * The purpose of this method is to print out the transcript. We use the outline
	 * provided to us, and use a bunch of print statements to print the transcript in a file called output.txt.
	 * 
	 * @param student, which is an ArrayList of type Student. It is used to get the
	 * information required to print the transcript.
	 * @throws FileNotFoundException if there was a problem in creating or writing to the file.
	 */
	public void printTranscript(ArrayList<Student> student) {
		PrintWriter print = null;
		try {
			print = new PrintWriter(getOutputFile());
			for(Student s: student) {
				print.println(s.getName() + "\t" + s.getStudentID());
				print.println("--------------------");
				for (int i = 0; i < s.getCourseTaken().size(); i++) {
					print.printf("%s\t%.1f\n", s.getCourseTaken().get(i).getCode(), s.getFinalGrade().get(i));
				}
				print.println("--------------------");
				print.println("GPA: " + s.weightedGPA());
				print.println();
			}
		}
		catch (Exception e) {
			System.out.println("File not found");
		}
		finally {
			print.close();
		}
	}

	/**
	 * This a getter method for grade
	 * @return ArrayList containing the lines from a text file.
	 */
	public ArrayList<Object> getGrade() {
		return grade;
	}

	/**
	 * This is a mutator for grade.
	 * @param grade
	 */
	public void setGrade(ArrayList<Object> grade) {
		this.grade = grade;
	}

	/**
	 * This is a getter for the input text file.
	 * @return inputFile, which is the input text file.
	 */
	public File getInputFile() {
		return inputFile;
	}

	/**
	 * This is a mutator for the input text file.
	 * @param inputFile
	 */
	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * This is a getter for the output file
	 * @return the path of the output in which the result is to be stored.
	 */
	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * This is a mutator for the output file.
	 * @param outputFile
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

} // end of Transcript


/**
 * This class stores the final grade for each course of the student, the courses taken by each student, their name and student id.
 */

class Student {
	private String studentID;
    private String name;
    private ArrayList<Course> courseTaken = new ArrayList<>();
    private ArrayList<Double> finalGrade = new ArrayList<>();

	/**
	 * This is a default constructor for class Student.
	 */
    public Student() {
		this.studentID = "";
		this.name = "";
		this.courseTaken = null;
    }

	/**
	 * This is a constructor for class Student that makes an object for Student.
	 * @param studentID
	 * @param name
	 * @param courseTaken
	 */
    public Student(String studentID, String name, ArrayList<Course> courseTaken) {
		this.studentID = studentID;
		this.name = name;
		this.courseTaken = courseTaken;
	}
	
	/**
	 * This is a getter for the private instance variable finalGrade.
	 * @return the ArrayList of final grades of the student.
	 */
	public ArrayList<Double> getFinalGrade() {
		ArrayList<Double> finalGrade = new ArrayList<>();
		for (Double d: this.finalGrade) {
			finalGrade.add(new Double(d));
		}
		return finalGrade;
	}

	/**
	 * This is a mutator for the private instance variable finalGrade.
	 * @param finalGrade
	 */
	public void setFinalGrade(ArrayList<Double> finalGrade) {
		this.finalGrade = new ArrayList<>();
		for (Double d: finalGrade) {
			finalGrade.add(new Double(d));
		}
	}

	/**
	 * This is a getter for the private instance variable name.
	 * @return the name of the student.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This is a mutator for the private instance variable name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This is a getter for the private instance variable studentID
	 * @return the studentID of the student
	 */
	public String getStudentID() {
		return this.studentID;
	}

	/**
	 * This is a mutator for the private instance variable studentID
	 * @param studentID
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * This is a getter method for the private instance variable courseTaken
	 * @return the ArrayList of type Courses, which is the courses taken by a student
	 */
	public ArrayList<Course> getCourseTaken() {
		ArrayList<Course> courseTaken = new ArrayList<>();
		for (Course c: this.courseTaken) {
			courseTaken.add(c);
		}
		return courseTaken;
	}

	/**
	 * This is a mutator method for the private instance variable courseTaken.
	 * @param courseTaken
	 */
	public void setCourseTaken(ArrayList<Course> courseTaken) {
		this.courseTaken = new ArrayList<>();
		for (Course c: courseTaken) {
			this.courseTaken.add(c);
		}
	}

	/**
	 * This method gets the total grade of a course by taking the grade of individial assessments and its weight and getting the sum of the grades
	 * of all assessments.
	 * @param grade, which is an ArrayList of type Double. It contains the grade of individual assessments.
	 * @param weight, which is an ArrayList of type Integer. It contains the weight of individual assessments.
	 * @throws InvalidTotalException if the sum of weights of every assessment of the course is not 100 OR
	 * the sum of grades of every assessment is greater than 100.
	 */
    public void addGrade(ArrayList<Double> grade, ArrayList<Integer> weight) {
		int sumWeight = 0;
		double sumGrade = 0.0;
		for (int i = 0; i < grade.size(); i++) {
			sumWeight += weight.get(i);
			sumGrade += Math.round((grade.get(i) * weight.get(i) / 100) * 10.0) / 10.0;
		}
		try {
			if (sumWeight != 100 || sumGrade > 100) throw new InvalidTotalException("Sum of numbers or grades are invalid");
		}
		catch(InvalidTotalException e) {
			System.out.println(e.getMessage());
		}
		this.finalGrade.add(sumGrade);
    }

	/**
	 * This method calculates the total GPA of the student by using the GPA scale table. It gets the grade point of every course and multiplies
	 * it by the credits of that course.
	 * @return the average GPA of every course of the student. 
	 */
    public double weightedGPA(){
		double sum = 0;
		int creditSum = 0;
		for (int i = 0; i < finalGrade.size(); i++) {
			int gradePoint = 0;
			if (finalGrade.get(i) >= 90) gradePoint = 9;
			else if (finalGrade.get(i) >= 80) gradePoint = 8;
			else if (finalGrade.get(i) >= 75) gradePoint = 7;
			else if (finalGrade.get(i) >= 70) gradePoint = 6;
			else if (finalGrade.get(i) >= 65) gradePoint = 5;
			else if (finalGrade.get(i) >= 60) gradePoint = 4;
			else if (finalGrade.get(i) >= 55) gradePoint = 3;
			else if (finalGrade.get(i) >= 50) gradePoint = 2;
			else if (finalGrade.get(i) >= 47) gradePoint = 1;
			else gradePoint = 0;
			sum += courseTaken.get(i).getCredit() * gradePoint; 
			creditSum += courseTaken.get(i).getCredit();
		}
		return Math.round((sum / creditSum) * 10.0) / 10.0;
    }

	/**
	 * This method takes a course as an input and adds it to the instance ArrayList of type Course.
	 * @param course
	 */
    public void addCourse(Course course) {
		this.courseTaken.add(course);
	}
	
}

/**
 * This class stores the assessments for each course, the course code, and credits for that course code.
 */

class Course {
	private String code;
    private ArrayList<Assessment> assignment = new ArrayList<>();
    private double credit;


	/**
	 * This is a default constructor for class Course.
	 */
    public Course() {
		this.code = "";
		this.credit = 0.0;
		this.assignment = null;
    }

	/**
	 * This is a constructor for class Course that makes an object of Course.
	 * @param code
	 * @param assignment
	 * @param credit
	 */
    public Course(String code, ArrayList<Assessment> assignment, double credit) {
		this.code = code;
		this.credit = credit;
		for (Assessment a: assignment) {
			this.assignment.add(a);
		}
    }


	/**
	 * This is a copy constructor for class Course.
	 * @param course, which is a reference to a course object
	 */
    public Course(Course course) {
		this.code = course.code;
		this.credit = course.credit;
		this.assignment = course.assignment;
	}

	/**
	 * This is a getter method for the private instance variable code.
	 * @return the course code of the course
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * This is a mutator method for the private instance variable code.
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * This is a getter method for the private instance variable assignment.
	 * @return an ArrayList of assessments.
	 */
	public ArrayList<Assessment> getAssignment() {
		ArrayList<Assessment> assessment = new ArrayList<>();
		for (Assessment a: this.assignment) {
			assessment.add(a);
		}
		return assessment;
	}

	/**
	 * This is a mutator method for the private instance variable assignment.
	 * @param assignment
	 */
	public void setAssignment(ArrayList<Assessment> assignment) {
		this.assignment = new ArrayList<>();
		for (Assessment a: assignment) {
			this.assignment.add(a);
		}
	}

	/**
	 * This is a getter method for the private instance variable credit.
	 * @return the credits of a course.
	 */
	public double getCredit() {
		return credit;
	}

	/**
	 * This is a mutator method for the private instance variable credit.
	 * @param credit
	 */
	public void setCredit(double credit) {
		this.credit = credit;
	}
	

	/**
	 * This method checks if the courses are equal based on their course code, the credits of the course and the number of assessments.
	 */
	@Override
    public boolean equals(Object object) {
        if (this == object) {
			return true;
		}
		if (!(object instanceof Course)) {
			return false;
		}
		Course course = (Course) object;
		return this.getCredit() == course.getCredit() && this.getCode() == course.getCode() && this.assignment.equals(course.assignment);
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(credit, code, assignment);
	}
}


/**
 * This class stores the type of assessments and its weight.
 */
class Assessment {
	private char type;
    private int weight;

	/**
	 * This is a default constructor for class Assessments 
	 */
    private Assessment() {
		this.type = ' ';
		this.weight = 0;

    }

	/**
	 * This is a constructor for class Assessment that makes an object of Assessment
	 * @param type
	 * @param weight
	 */
    private Assessment(char type, int weight) {
		this.type = type;
		this.weight = weight;
    }
	
	/**
	 * This is a getter method for the private instance variable type.
	 * @return the type of assessment.
	 */
	public char getType() {
		return type;
	}

	/**
	 * This is the mutator method for the private instance variable type.
	 * @param type
	 */
	public void setType(char type) {
		this.type = type;
	}

	/**
	 * This is a getter method for the private instance variable weight.
	 * @return the weight of the assessment.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * This is a mutator method for the private instance variable weight.
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * This static factory method does the same thing as the constructor of this class.
	 * @param type is the type of assessment.
	 * @param weight is the weight of the assessment.
	 * @return a reference to a book object created by the input parameters. 
	 */
	public static Assessment getInstance(char type, int weight) {
		return new Assessment(type, weight);

	}


	/**
	 * This method checks if the assessments are equal based on the type of assessments and the weight of the assessments.
	 */
	@Override
    public boolean equals(Object object) {
        if (this == object) {
			return true;
		}
		if (!(object instanceof Assessment)) {
			return false;
		}
		Assessment assess = (Assessment) object;
		return this.getType() == assess.getType() && this.getWeight() == assess.getWeight();
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(type, weight);
	}

}


/**
 * This is an exception class that is invoked when the sum of weights are not equal to 100 OR 
 * the sum of grades of every assessment is greater than 100.
 */
class InvalidTotalException extends Exception {
	public InvalidTotalException() {
		super();
	}
	public InvalidTotalException(String message) {
		super(message);
	}
}