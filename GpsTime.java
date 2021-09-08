/*  
 * Name: Steven Rivera 
 * 
 * Java program that
 */
package homework; 
import java.time.LocalDate;

public class GpsTime {
	private double longitude;
	private double latitude;
	private LocalDate when;
	
	//ToDo 1     implement the required constructor (see other file)
	public GpsTime( double longitude, double latitude, LocalDate date) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.when = date;
	}
	
	//ToDo 2      update the hashcode function below
	//            You can try your answer from hw5, or make up a new one
	public int hashCode() {
		
		int hash = 17;
		hash = (31 * hash + ((Double)longitude).hashCode());
		hash = (31 * hash + ((Double)latitude).hashCode());
		hash = 31 * hash + when.hashCode();
		return hash;  // fix this
		
	}

	//ToDo 3    use the textbook 'recipe' to implement the equals function for this class
	public boolean equals( Object x) {
		if(this == x) return true;
		if (x == null) return false;
		if(x.getClass() != this.getClass()) {
			return false;
		}
		
		GpsTime time = (GpsTime) x;
		if(this.longitude != time.longitude) return false;
		if(this.latitude != time.latitude) return false;
		if((!this.when.equals(time.when))) return false;
		
		return true;
		
		// fix this-
 
	}
	public String toString() {
		return  Double.toString(longitude)+ " "+Double.toString(latitude)+" "+when.toString();
	}
}
