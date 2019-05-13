package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 *  Simple application which uses GSON to parse a JSON file into Records, and then compares records against each other
 *  resolving duplicates as it goes, until only the most recent unique Record data is left. It then saves that
 *  collection of records as "output.json".
 */
public class Application {
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static void main (String[] args) {
		if (args.length > 0) {
			String fileName = args[0];
			Application application = new Application();
			application.execute(fileName);
		}
	}
	
	private RecordDAO[] extractRecords(String aJson) {
		//Use Gson to parse records.
		RecordDAO[] recordDAOs = null;
		if (aJson != null) {
			recordDAOs = gson.fromJson(aJson, RecordsDAO.class).leads;
		}
		return recordDAOs;
	}

	
	// Go across all record data objects and resolve conflicts.
	private void execute(String aFileName) {
		String json = readFile(aFileName);
		RecordDAO[] records = extractRecords(json);
		
		RecordDAO[] paredRecords = resolveDuplicates(records);
		RecordsDAO output = new RecordsDAO(paredRecords);
		saveOutputToJson(output);
	}

	// Load passed in filepath and read to string json.
	private String readFile(String aFileName){
		String json = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(aFileName));
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
		} catch (IOException ioe){
			ioe.printStackTrace();
			System.exit(1);
		}
		return json;
	}

	/*
	 Iterate through record objects checking objects against existing emails and ids.
	 If email matches another id we resolve data to the latest date.
	 If dates match simply replace existing data with new.
	 */
	public RecordDAO[] resolveDuplicates (RecordDAO[] aRecordDAOs) {
		RecordDAO[] result = null;
		HashMap<String, Record> recordsById = new HashMap<String, Record>();
		// Email is key, id is value.
		HashMap<String, String> recordEmailIds = new HashMap<String, String>();
		
		try {
			for (RecordDAO dao : aRecordDAOs) {
				Record record = new Record(dao);
				String recordId = record.getId();
				String recordEmail = record.getEmail();
				boolean isIdCollision = recordsById.containsKey(recordId);
				boolean hasCollision = isIdCollision ||	recordEmailIds.containsKey(recordEmail);
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
							/*
							 If the collision is an email collision and we are overwriting an existing record
							 we need to update the id table accordingly by replacing the existing record with
							 the new one and update the id in the email table.
							 */
							if (recordsById.containsKey(existingRecordId)) {
								recordsById.remove(existingRecordId);
								recordEmailIds.put(recordEmail, recordId);
							}
							recordsById.put(recordId, record);
						}
					}
				} else {
					/*
					If there is no collision we store the record in our hash table
					and associate the email with the record id in the email ids table.
					*/
					recordsById.put(recordId, record);
					recordEmailIds.put(recordEmail, recordId);
				}
			}
			
			// Initialize recordDAOs for serialization.
			result = new RecordDAO[recordsById.size()];
			int i = 0;
			for (Record IcRecord : recordsById.values()) {
				result[i] = IcRecord.toRecordDAO();
				i++;
			}
		} catch (ParseException parseException) {
			parseException.printStackTrace();
			System.exit(1);
		}
		return result;
	}
	
	//Save output records as "output.json".
	private void saveOutputToJson(RecordsDAO aOutput) {
		try {
			String ouputPath = System.getProperty("user.dir");
		    Files.write(Paths.get(ouputPath + "/output.json"), gson.toJson(aOutput).getBytes(),
		    		StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	
}
