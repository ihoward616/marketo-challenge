package main;

public class RecordsDAO {
	/*
	 * Data class for holding records parsed from JSON.
	 * To be used with GSON parsing.
	 */
	
	public RecordDAO[] leads;
	public RecordsDAO(RecordDAO[] aLeads){
		leads = aLeads;
	}
}
