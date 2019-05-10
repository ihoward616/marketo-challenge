package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String address;
	private Date entryDate;
	
	public Record (String aId, String aEmail, String aFirstName, String aLastName, String aAddress, String aEntryDate) throws ParseException {
		id = aId;
		email = aEmail;
		firstName = aFirstName;
		lastName = aLastName;
		address = aAddress;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		entryDate = format.parse(aEntryDate);
	}
	
	public Record (RecordDAO aDAO)  throws ParseException {
		id = aDAO._id;
		email = aDAO.email;
		firstName = aDAO.firstName;
		lastName = aDAO.lastName;
		address = aDAO.address;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		entryDate = format.parse(aDAO.entryDate);
	}
	
	public void overwriteWithRecord (Record aRecord) {
			// Check for differences and overwrite current values with values from aRecord.
		String otherEmail = aRecord.getEmail();
		String otherId = aRecord.getId();
		String otherFirstName = aRecord.getFirstName();
		String otherLastName = aRecord.getLastName();
		String otherAddress = aRecord.getAddress();
		Date otherDate = aRecord.getEntryDate();
		
		StringBuffer updateLog = new StringBuffer();
		updateLog.append("Updating values of ");
		updateLog.append(id + ":" + email);
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
		if (!id.equals(otherId)) {
			addChangeToLog(updateLog, "_id", id, otherId);
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
	
	private void addChangeToLog (StringBuffer aStringBuffer, String aKey, String aOldValue, String aNewValue) {
		aStringBuffer.append("Changing ");
		aStringBuffer.append(aKey);
		aStringBuffer.append(" from ");
		aStringBuffer.append(aOldValue);
		aStringBuffer.append(" to ");
		aStringBuffer.append(aNewValue);
		aStringBuffer.append("\n");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
}
