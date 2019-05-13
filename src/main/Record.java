package main;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Record {
	
	/*
	 * Simple object representing a record parsed from JSON.
	 * Contains logic for overwriting values and being re-serialized through a DAO object.
	 */
	
	private String _id;
	private String email;
	private String firstName;
	private String lastName;
	private String address;
	private transient OffsetDateTime entryDate;
	private static transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");
	
	public Record (String aId, String aEmail, String aFirstName, String aLastName,
					String aAddress, String aEntryDate) throws ParseException {
		_id = aId;
		email = aEmail;
		firstName = aFirstName;
		lastName = aLastName;
		address = aAddress;
		entryDate = OffsetDateTime.parse(aEntryDate);
	}

	//Create from DAO object parsed from JSON.
	public Record (RecordDAO aDAO)  throws ParseException {
		_id = aDAO._id;
		email = aDAO.email;
		firstName = aDAO.firstName;
		lastName = aDAO.lastName;
		address = aDAO.address;
		entryDate = OffsetDateTime.parse(aDAO.entryDate, formatter);
	}

	// Check for differences and overwrite current values with values from aRecord.
	public void overwriteWithRecord (Record aRecord) {
		String otherEmail = aRecord.getEmail();
		String otherId = aRecord.getId();
		String otherFirstName = aRecord.getFirstName();
		String otherLastName = aRecord.getLastName();
		String otherAddress = aRecord.getAddress();
		OffsetDateTime otherDate = aRecord.getEntryDate();
		
		StringBuffer updateLog = new StringBuffer();
		updateLog.append("Updating values of ");
		updateLog.append(_id + ":" + email);
		updateLog.append(" with values from ");
		updateLog.append(otherId + ":" + otherEmail);
		updateLog.append("\n");
		
		//Update values and log changes.
		if (!firstName.equals(otherFirstName)) {
			addChangeToLog(updateLog, "firstName", firstName, otherFirstName);
			setFirstName(otherFirstName);
		}
		if (!lastName.equals(otherLastName)) {
			addChangeToLog(updateLog, "lastName", lastName, otherLastName);
			setLastName(otherLastName);
		}
		if (!_id.equals(otherId)) {
			addChangeToLog(updateLog, "_id", _id, otherId);
			setId(otherId);
		}
		if (!email.equals(otherEmail)) {
			addChangeToLog(updateLog, "email", email, otherEmail);
			setEmail(otherEmail);
		}
		if (!address.equals(otherAddress)) {
			addChangeToLog(updateLog, "address", address, otherAddress);
			setAddress(otherAddress);
		}
		if (!entryDate.equals(otherDate)) {
			addChangeToLog(updateLog, "entryDate", entryDate.toInstant().toString(), otherDate.toInstant().toString());
			setEntryDate(otherDate);
		}
		updateLog.append("\n");
		System.out.print(updateLog.toString());
	}
	
	//Helper method for building a log of changes.
	private void addChangeToLog (StringBuffer aStringBuffer, String aKey, String aOldValue, String aNewValue) {
		aStringBuffer.append("Changing ");
		aStringBuffer.append(aKey);
		aStringBuffer.append(" from ");
		aStringBuffer.append(aOldValue);
		aStringBuffer.append(" to ");
		aStringBuffer.append(aNewValue);
		aStringBuffer.append("\n");
	}
	
	// Function to output GSON usable data object for persistence. 
	public RecordDAO toRecordDAO() {
		String dateString = formatter.format(entryDate);
		RecordDAO result = new RecordDAO(_id, email, firstName, lastName, address, dateString);
		return result;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public OffsetDateTime getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(OffsetDateTime entryDate) {
		this.entryDate = entryDate;
	}
}
