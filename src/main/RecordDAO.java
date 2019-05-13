package main;

public class RecordDAO {
	/*
	 * Simple data storage object for persisting Record Objects in use with GSON.
	 */
	
	public String _id;
	public String email;
	public String firstName;
	public String lastName;
	public String address;
	public String entryDate;
	
	public RecordDAO(String aId, String aEmail, String aFirstName,
					String aLastName, String aAddress, String aEntryDate){
		_id = aId;
		email = aEmail;
		firstName = aFirstName;
		lastName = aLastName;
		address = aAddress;
		entryDate = aEntryDate;
	}
}
