package junit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.Application;
import main.Record;
import main.RecordDAO;

public class DuplicateResolutionTest {
	private RecordDAO[] records;
	private static Application application = new Application();
	
	// Test duplicate Id case.
	@Test
	public void testDuplicateId(){
		records = new RecordDAO[2];
		setupDuplicateIdRecords();
		RecordDAO[] results = application.resolveDuplicates(records);
		// After resolution we should have reduced our conflicts into one record.
		assertEquals(1, results.length);
		
		RecordDAO result = results[0];
		// Now check record values.
		assertEquals("jkj238238jdsnfsj23", result._id);
		assertEquals("roo@bar.com", result.email);
		assertEquals("James", result.firstName);
		assertEquals("Smith", result.lastName);
		assertEquals("123 Street St", result.address);
		assertEquals("2014-05-07T17:33:20+00:00", result.entryDate);
	}

	// Test duplicate Email case.
	@Test
	public void testDuplicateEmail(){
		records = new RecordDAO[2];
		setupDuplicateEmailRecords();
		RecordDAO[] results = application.resolveDuplicates(records);
		// After resolution we should have reduced our conflicts into one record.
		assertEquals(1, results.length);
		
		RecordDAO result = results[0];
		// Now check record values.
		assertEquals("jkj238238jdsnfsj21", result._id);
		assertEquals("foo@bar.com", result.email);
		assertEquals("James", result.firstName);
		assertEquals("Smith", result.lastName);
		assertEquals("123 Street St", result.address);
		assertEquals("2014-05-07T17:33:20+00:00", result.entryDate);
	}
	
	// Test duplicate Id and Email case.
	@Test
	public void testDuplicateIdAndEmail(){
		records = new RecordDAO[3];
		setupDuplicateEmailAndIdRecords();
		RecordDAO[] results = application.resolveDuplicates(records);
		// After resolution we should have reduced our conflicts into one record.
		assertEquals(1, results.length);
		
		RecordDAO result = results[0];
		// Now check record values.
		assertEquals("jkj238238jdsnfsj22", result._id);
		assertEquals("foo@bar.com", result.email);
		assertEquals("James", result.firstName);
		assertEquals("Smith", result.lastName);
		assertEquals("123 Tumbledown Ln", result.address);
		assertEquals("2014-05-07T17:33:20+00:00", result.entryDate);
	}
	
	private void setupDuplicateIdRecords() {
		RecordDAO record1 = new RecordDAO("jkj238238jdsnfsj23", "foo@bar.com", "John", "Smith",
									"123 Street St", "2014-05-07T17:30:20+00:00");
		RecordDAO record2 = new RecordDAO("jkj238238jdsnfsj23", "roo@bar.com", "James", "Smith",
									"123 Street St", "2014-05-07T17:33:20+00:00");
		records[0] = record1;
		records[1] = record2;
	}
		
	private void setupDuplicateEmailRecords(){
		RecordDAO record1 = new RecordDAO("jkj238238jdsnfsj23", "foo@bar.com", "John", "Smith",
									"123 Street St", "2014-05-07T17:30:20+00:00");
		RecordDAO record2 = new RecordDAO("jkj238238jdsnfsj21", "foo@bar.com", "James", "Smith",
									"123 Street St", "2014-05-07T17:33:20+00:00");
		records[0] = record1;
		records[1] = record2;
	}
	
	private void setupDuplicateEmailAndIdRecords(){
		RecordDAO record1 = new RecordDAO("jkj238238jdsnfsj23", "boo@bar.com", "John", "Smith",
									"123 Street St", "2014-05-07T17:30:20+00:00");
		RecordDAO record2 = new RecordDAO("jkj238238jdsnfsj23", "foo@bar.com", "Jack", "Smoth",
									"123 Street St", "2014-05-07T17:30:20+00:00");
		RecordDAO record3 = new RecordDAO("jkj238238jdsnfsj22", "foo@bar.com", "James", "Smith",
									"123 Tumbledown Ln", "2014-05-07T17:33:20+00:00");
		records[0] = record1;
		records[1] = record2;
		records[2] = record3;
	}
	
	
}
