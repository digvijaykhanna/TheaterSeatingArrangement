package showclix;

import java.lang.Math;

public class SeatingChart{
	private char[][] chart;
	
	// Creates seating chart for given rows and columns
	public SeatingChart(int rows, int columns){
		chart = new char[rows][columns];
	}
	
	// Reserves seat for given seat
	public void reserveSeat(String seat){
		int row = Integer.parseInt(seat.substring(seat.indexOf('R')+1, seat.indexOf('C')));
		int column = Integer.parseInt(seat.substring(seat.indexOf('C')+1, seat.length()));
		
		chart[row-1][column-1] = 'R';
	}
	
	// Returns true if seat is already reserved and false otherwise
	public boolean isSeatReserved(String seat){
		int row = Integer.parseInt(seat.substring(seat.indexOf('R')+1, seat.indexOf('C')));
		int column = Integer.parseInt(seat.substring(seat.indexOf('C')+1, seat.length()));
		
		return chart[row-1][column-1] == 'R';
	}
	
	// Returns total number of available seats
	public int findTotalAvailableSeats(){
		int availableSeats = 0;
		int rows = chart.length;
		int columns = chart[0].length;
		
		for(int i=1; i<=rows; i++){
			for(int j=1; j<=columns; j++){
				if(chart[i-1][j-1] != 'R'){
					availableSeats++;
				}
			}
		}
		return availableSeats;
	}
	
	// Calls method to find best consecutive seats, reserves the seats and returns the result
	public String findBestConsecutiveSeats(int request){
		int rows = chart.length;
		int columns = chart[0].length;
		
		int bestSeat = (int) Math.ceil(columns/2.0);
		boolean isFound = false;
		
		int i=1;
		String result = "Not Available";
		int distance = 0;
		
		result = findBestSeat(rows, bestSeat, columns, request);
		if(!result.equals("Not Available")){
			String[] makeReservation = result.split("-");
			for(int j=0; j<makeReservation.length; j++){
				reserveSeat(makeReservation[j]);
			}
			if(result.indexOf('-') != -1)
				result = result.substring(0, result.indexOf('-')) + " - " + result.substring(result.lastIndexOf('-')+1, result.length());
		}
		return result;
	}
	
	// Finds the best available distance and calls function to find consecutive seats from best available distance
	private String findBestSeat(int rows, int bestSeat, int totalSeats, int request){
		int distance = 1;
		String result = "Not Available";
		while(distance <= bestSeat){
			if(distance==1){
				result = findConsecutiveSeatsRecursive(1, bestSeat, totalSeats, request);
				distance++;
			}
			
			for(int i=1; i<=rows; i++){				
				if(distance!=1){
					result = findConsecutiveSeatsRecursive(i, bestSeat, totalSeats, request);
				}
				int columnDistance = distance - i;
				if(result.equals("Not Available") && (bestSeat+columnDistance) <= totalSeats && !isSeatReserved("R" + i + "C" + (bestSeat+columnDistance))){
					result = findConsecutiveSeatsRecursive(i, bestSeat+columnDistance, totalSeats, request);
				}
				
				if(result.equals("Not Available") && (bestSeat-columnDistance) >= 1 && !isSeatReserved("R" + i + "C" + (bestSeat-columnDistance))){
					result = findConsecutiveSeatsRecursive(i, bestSeat-columnDistance, totalSeats, request);
				}
				if(!result.equals("Not Available")){
					break;
				}
			}
			if(!result.equals("Not Available")){
				break;
			}
			distance++;
		}
		return result;
	}
	
	// Finds requested consecutive seats based on best available distance
	private String findConsecutiveSeatsRecursive(int row, int currentSeat, int totalSeats, int request){
		if(currentSeat >= 1 && currentSeat <= chart[0].length){
			String seat = "R" + row + "C" + currentSeat;
			if(!isSeatReserved(seat)){
				if(--request == 0){
					return seat;
				}
				
				boolean flag1 = true, flag2 = true;
				int distance = 1;
				while(request != 0){
					String seat1 = "R" + row + "C" + (currentSeat-distance);
					String seat2 = "R" + row + "C" + (currentSeat+distance);
					
					if(flag1 && (currentSeat-distance) >= 1){
						if(!isSeatReserved(seat1)){
							seat = seat1 + "-" + seat;
							request--;
						}
						else{
							flag1 = false;
						}
					}
					if(flag2 && (currentSeat+distance) <= chart[0].length){
						if(!isSeatReserved(seat2)){
							seat = seat + "-" + seat2;
							request--;
						}
						else{
							flag2 = false;
						}
					}
					
					if((!flag1 & !flag2) || ((currentSeat-distance) < 1 & (currentSeat+distance) > chart[0].length)){
						return "Not Available";
					}
					distance++;
				}
				
				return seat;				
			}
			return "Not Available";
		}
		return "Not Available";
	}
}