package Assignment2;

import java.util.ArrayList;

public class TranscriptTester {

	public static void main(String[] args) {
		Transcript transcript = new Transcript("C:\\Users\\moham\\Git VSCode\\EECS2030_F2020_Assignment2\\wronginput.txt", "C:\\Users\\moham\\Git VSCode\\EECS2030_F2020_Assignment2\\output.txt");
		ArrayList<Student> student = transcript.buildStudentArray();
		transcript.printTranscript(student);
	}

} 
