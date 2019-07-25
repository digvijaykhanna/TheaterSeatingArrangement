package showclix;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Driver{
	public static void main(String[] args){
		// Defining reader and writer objects
		BufferedReader reader = null;
		PrintWriter writer = null;
		
		try{
			// Defining file from which input should be taken
			reader = new BufferedReader(new FileReader("Input.txt"));
			
			// Creating seating chart of 3 rows and 11 columns
			SeatingChart chart = new SeatingChart(3, 11);
			
			// Taking initial reservations from input and reserving the seats
			String[] reservedSeats = reader.readLine().split(" ");			
			for(int i=0; i<reservedSeats.length; i++){
				chart.reserveSeat(reservedSeats[i]);
			}
			
			// Defining output file to write results
			writer = new PrintWriter(new FileWriter("Output.txt"));
			String line = null;
			while((line = reader.readLine()) != null){
				int ticket = Integer.parseInt(line);				
				if(ticket > 10){
					// If more than 10 tickets are requested then the input must not be processed
					writer.println("Maximum 10 tickets can only be requested.");
				}
				else{
					// For 10 or less tickets, find and reserve if consecutive seats are available
					writer.println(chart.findBestConsecutiveSeats(ticket));
				}
			}
			
			// Write total number of seats available after reservation requests are processed
			writer.println(chart.findTotalAvailableSeats());
		}
		catch(IOException e){
			System.out.println("Exception thrown:"+e.getMessage());
		}
		finally{
			if(reader != null){
				try{
					// Closing reader and writer objects
					reader.close();
					writer.close();
				}
				catch(Exception ex){
					System.out.println("Error occurred while closing the reader/writer."+ex.getMessage());
				}
			}
		}
	}
}