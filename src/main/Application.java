package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;

public class Application {
	
	public static void main (String[] args) {
		//Load JSON and parse into record objects.
		//go through record objects
		// check objects against existing emails and ids.
		// if email matches another id we resolve data to the latest date.
		// if dates match simply replace existing data with new.
		
		ArrayList<Record> records = new ArrayList<Record>();
		HashMap<String, Record> recordsById = new HashMap<String, Record>();
		HashMap<String, String> recordEmailIds = new HashMap<String, String>();
		String json = null;
		
		//Load passed in filepath and read to string json.
		if (args.length > 0) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				try {
				    StringBuilder builder = new StringBuilder();
				    String line = reader.readLine();
	
				    while (line != null) {
				    	builder.append(line);
				    	builder.append(System.lineSeparator());
				        line = reader.readLine();
				    }
				    json = builder.toString();
				} finally {
					reader.close();
				}
			} catch (Exception e){
				System.out.print(e);
			}
		}

		//Use Gson to parse records.
		Gson gson = new Gson();
		RecordDAO[] recordDAOs = null;
		if (json != null) {
			recordDAOs = gson.fromJson(json, Records.class).leads;
		}
		
		// Go across all record data objects and resolve conflicts.
		try {
			for (RecordDAO dao : recordDAOs) {
				Record record = new Record(dao);
				String recordId = record.getId();
				String recordEmail = record.getEmail();
				boolean isIdCollision = recordsById.containsKey(recordId);
				boolean hasCollision = isIdCollision ||
										recordEmailIds.containsKey(recordEmail);
				if (hasCollision){
					// Gather pertinent information about the existing record based on collision type.
					Record existingRecord;
					String existingRecordId;
					if (isIdCollision) {
						existingRecordId = recordId;
						existingRecord = recordsById.get(recordId);
					} else {
						existingRecordId = recordEmailIds.get(recordEmail);
						existingRecord = recordsById.get(existingRecordId);
					}
					String existingRecordEmail = existingRecord.getEmail();
					
					// Determine which record is newer.
					
					int isFirstMoreRecent = existingRecord.getEntryDate().compareTo(record.getEntryDate());
					if (isFirstMoreRecent == 1) {
						// If existingRecord is newer, record should be overwritten.
						record.overwriteWithRecord(existingRecord);
					} else {
						// If both dates are the same, or record is newer, existingRecord should be overwritten.
						existingRecord.overwriteWithRecord(record);
						
						// Update tables to reflect change.
						if (isIdCollision) {
							// If the collision is an ID collision we need to update our email table in case the
							// email has changed.
							if (!recordEmail.equals(existingRecordEmail)) {
								if (recordEmailIds.containsKey(existingRecordEmail)) {
									recordEmailIds.remove(existingRecordEmail);
								}
								recordEmailIds.put(recordEmail, recordId);
							}
						} else {
							// If the collision is an email collision and we are overwriting an existing record
							// we need to update the id table accordingly by replacing the existing record with
							// the new one.
							if (recordsById.containsKey(existingRecordId)) {
								System.out.println("Removing record id " + existingRecordId);
								recordsById.remove(existingRecordId);
								if () {
									
								}
							}
							recordsById.put(recordId, record);
							
						}
					}
				} else {
					// If there is no collision we store the record in our hash table
					// and associate the email with the record id in the email ids table.
					recordsById.put(recordId, record);
					recordEmailIds.put(recordEmail, recordId);
				}
			}	
			
		} catch (ParseException parseException) {
			System.out.print(parseException.toString());
			System.exit(1);
		}
	}
	
	
	
}
