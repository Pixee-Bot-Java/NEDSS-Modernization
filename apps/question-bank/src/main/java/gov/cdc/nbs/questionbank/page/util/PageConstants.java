package gov.cdc.nbs.questionbank.page.util;

public class PageConstants {
	
	private PageConstants() {}
	
	// Page State Change
	public static final String SAVE_DRAFT_SUCCESS = "Draft was saved successfully.";
	public static final String SAVE_DRAFT_FAIL = " Failed to update to draft status: ";
	
	//Page Create
	public static final String ADD_PAGE_MESSAGE= " page has been successfully added"; 
	public static final String ADD_PAGE_FAIL = "could not successfully add page";
	public static final String ADD_PAGE_NAME_EMPTY = "Page name is required.";
	public static final String ADD_PAGE_TEMPLATENAME_EXISTS = "A Template with Template Name %s already exists in the system";
	public static final String ADD_PAGE_CONDITION_EMPTY = "At least one condition is required.";
	public static final String ADD_PAGE_EVENTTYPE_EMPTY = "EventType is required.";
	public static final String ADD_PAGE_TEMPLATE_EMPTY = "Template is required.";
	public static final String ADD_PAGE_DATAMART_NAME_EXISTS = "A Page with Data Mart Name %s already exists in the system";
	public static final String ADD_PAGE_MMG_EMPTY = "MMG is required.";
	
	
	
	
	// General
	public static final String NOT_EXISTS= "could not find template with given Id";
	

}
