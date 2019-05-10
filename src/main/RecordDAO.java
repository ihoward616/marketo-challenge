package main;

	//Simple storage object for persisting Record Objects.

public class RecordDAO {
	public String _id;
	public String email;
	public String firstName;
	public String lastName;
	public String address;
	public String entryDate;
	
	public RecordDAO(String aId, String aEmail, String aFirstName, String aLastName, String aAddress, String aEntryDate){
		_id = aId;
		email = aEmail;
		firstName = aFirstName;
		lastName = aLastName;
		address = aAddress;
		entryDate = aEntryDate;
	}
}
