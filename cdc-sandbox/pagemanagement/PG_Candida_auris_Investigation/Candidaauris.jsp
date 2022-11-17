<?xml version="1.0" encoding="UTF-8"?>
<!-- ### DMB:BEGIN JSP PAGE GENERATE ###- - -->
<!--##Investigation Business Object##-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page isELIgnored ="false" %>
<%@ page import="java.util.*" %>
<%@ page import="gov.cdc.nedss.util.NEDSSConstants" %>
<%@ page import="gov.cdc.nedss.pagemanagement.wa.dao.PageManagementDAOImpl" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>

<!-- ################### A PAGE TAB ###################### - - -->
<%
Map map = new HashMap();
if(request.getAttribute("SubSecStructureMap") != null){
map =(Map)request.getAttribute("SubSecStructureMap");
}
%>
<%
String tabId = "editCandidaauris";
tabId = tabId.replace("]","");
tabId = tabId.replace("[","");
tabId = tabId.replaceAll(" ", "");
int subSectionIndex = 0;
int sectionIndex = 0;
String sectionId = "";
String [] sectionNames  = {"Travel and Healthcare","Laboratory Information","Previous History","Exposure History"};
;
%>
<tr><td>
<div class="view" id="<%= tabId %>" style="text-align:center;">
<table style="width:100%;" class="sectionsToggler" role="presentation">
<tr><td><ul class="horizontalList">
<li style="margin-right:5px;"><b>Go to: </b></li>
<li><a href="javascript:gotoSection('<%= sectionNames[sectionIndex].replaceAll(" ", "") %>')"><%= sectionNames[sectionIndex++] %></a></li>
<li class="delimiter"> | </li>
<li><a href="javascript:gotoSection('<%= sectionNames[sectionIndex].replaceAll(" ", "") %>')"><%= sectionNames[sectionIndex++] %></a></li>
<li class="delimiter"> | </li>
<li><a href="javascript:gotoSection('<%= sectionNames[sectionIndex].replaceAll(" ", "") %>')"><%= sectionNames[sectionIndex++] %></a></li>
<li class="delimiter"> | </li>
<li><a href="javascript:gotoSection('<%= sectionNames[sectionIndex].replaceAll(" ", "") %>')"><%= sectionNames[sectionIndex++] %></a></li>
</ul> </td> </tr>
<tr>
<td style="padding-top:1em;">
<a class="toggleHref" href="javascript:toggleAllSectionsDisplay('<%= tabId %>')"/>Collapse Sections</a>
</td>
</tr>
</table>
<%  sectionIndex = 0; %>

<!-- ################# SECTION ################  -->
<nedss:container id='<%= sectionNames[sectionIndex].replaceAll(" ", "") %>' name="<%= sectionNames[sectionIndex++] %>" isHidden="F" classType="sect">

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_34" name="Travel and Healthcare" isHidden="F" classType="subSect" >

<!--processing Static Comment-->
<tr><td colspan="2" align="left"><span id="NBS_UI_49L">&nbsp;&nbsp;In the YEAR prior to specimen collection:</span> </td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="91515_7L" title="Indicate if the patient received overnight healthcare within the United States, but outside of the patient's resident state in the year prior to the date of specimen collection.">
Was Overnight Healthcare Received within the USA, but outside the patient's state of residence?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(91515_7)" styleId="91515_7" title="Indicate if the patient received overnight healthcare within the United States, but outside of the patient's resident state in the year prior to the date of specimen collection.">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="TRAVEL38L" title="Did the patient travel internationally in the year prior to the date of specimen collection?">
Did the patient travel internationally?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(TRAVEL38)" styleId="TRAVEL38" title="Did the patient travel internationally in the year prior to the date of specimen collection?" onchange="ruleEnDisTRAVEL388784();ruleEnDis90366_68785();pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Multi-Select Coded Question-->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="TRAVEL05L" title="List the names of the country(ies) outside of the United States the patient traveled to in the year prior to the date of specimen collection, if the patient traveled outside of the United States during that time.">
International Destination(s):</span>
</td>
<td>
<div class="multiSelectBlock">
<i> (Use Ctrl to select more than one) </i> <br/>
<html:select property="pageClientVO.answerArray(TRAVEL05)" styleId="TRAVEL05" title="List the names of the country(ies) outside of the United States the patient traveled to in the year prior to the date of specimen collection, if the patient traveled outside of the United States during that time."
multiple="true" size="4"
onchange="displaySelectedOptions(this, 'TRAVEL05-selectedValues')" >
<nedss:optionsCollection property="codedValue(PSL_CNTRY)" value="key" label="value" /> </html:select>
<div id="TRAVEL05-selectedValues" style="margin:0.25em;">
<b> Selected Values: </b>
</div>
</div></td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="90366_6L" title="This data element is used to capture if the patient received overnight healthcare outside of the United States in the year prior to the date of specimen collection.">
Received Overnight Healthcare Outside the USA:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(90366_6)" styleId="90366_6" title="This data element is used to capture if the patient received overnight healthcare outside of the United States in the year prior to the date of specimen collection." onchange="ruleEnDis90366_68785();pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Multi-Select Coded Question-->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="91514_0L" title="This data element is used to capture if the patient received overnight healthcare outside of the United States in the year prior to the date of specimen collection.">
Countries in which overnight healthcare was received:</span>
</td>
<td>
<div class="multiSelectBlock">
<i> (Use Ctrl to select more than one) </i> <br/>
<html:select property="pageClientVO.answerArray(91514_0)" styleId="91514_0" title="This data element is used to capture if the patient received overnight healthcare outside of the United States in the year prior to the date of specimen collection."
multiple="true" size="4"
onchange="displaySelectedOptions(this, '91514_0-selectedValues')" >
<nedss:optionsCollection property="codedValue(PSL_CNTRY)" value="key" label="value" /> </html:select>
<div id="91514_0-selectedValues" style="margin:0.25em;">
<b> Selected Values: </b>
</div>
</div></td></tr>
</nedss:container>
</nedss:container>

<!-- ################# SECTION ################  -->
<nedss:container id='<%= sectionNames[sectionIndex].replaceAll(" ", "") %>' name="<%= sectionNames[sectionIndex++] %>" isHidden="F" classType="sect">

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_36" name="Laboratory Testing" isHidden="F" classType="subSect" >

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="INV740L" title="Was laboratory testing done to confirm the diagnosis?">
Was laboratory testing done to confirm the diagnosis?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(INV740)" styleId="INV740" title="Was laboratory testing done to confirm the diagnosis?" onchange="ruleEnDisINV7408786();pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>
</nedss:container>

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_37" name="Lab Testing Repeating Block" isHidden="F" classType="subSect"  addedClass="batchSubSection">
<tr> <td colspan="2" width="100%">
<table role="presentation" width="100%"  border="0" align="center">
<tr><td>.</td>
<td   width="100%">
<div class="infoBox errors" style="display: none;visibility: none;" id="NBS_UI_37errorMessages">
<b> <a name="NBS_UI_37errorMessages_errorMessagesHref"></a> Please fix the following errors:</b> <br/>
</div>
<table role="presentation"  class="dtTable" align="center" >
<thead >
<tr> <%String subSecNm = "NBS_UI_37"; String batchrec[][]= null;   Iterator mapIt = map.entrySet().iterator();
while(mapIt .hasNext())
{
Map.Entry mappairs = (Map.Entry)mapIt .next();
if(mappairs.getKey().toString().equals(subSecNm)){
batchrec =(String[][]) mappairs.getValue();
break;
}
}%>
<%int wid =100/11; %>
<td style="background-color: #EFEFEF; border:1px solid #666666" width="9%" colspan="3"> &nbsp;</td>
<% for(int i=0;i<batchrec.length;i++){%>
<%    if(batchrec[i][2] != null && batchrec[i][2].equals("Y"))  {%>
<% String per = batchrec[i][4];
int aInt = (Integer.parseInt(per)) *91/100;
%>
<th width="<%=aInt%>%"><font color="black"><%=batchrec[i][3]%></font></th>
<%} %>
<%} %>
</tr>
</thead>
<tbody id="questionbodyNBS_UI_37">
<tr id="patternNBS_UI_37" class="odd" style="display:none">
<td style="display:none"><input name="rowKey" type="hidden" value=""></input></td>
<td style="width:3%;text-align:center;">
<input id="viewNBS_UI_37" type="image" src="page_white_text.gif" tabIndex="0" onclick="viewClicked(this.id,'NBS_UI_37');return false" 		name="image" align="middle" cellspacing="2" cellpadding="3" border="55" class="cursorHand" title="View" alt="View">
</td><td style="width:3%;text-align:center;">
<input id="editNBS_UI_37" type="image" src="page_white_edit.gif" tabIndex="0" onclick="editClicked(this.id,'NBS_UI_37');return false" name="image" align="middle" cellspacing="2" 		cellpadding="3" border="55" class="cursorHand" title="Edit" alt="Edit">
</td><td style="width:3%;text-align:center;">
<input id="deleteNBS_UI_37" type="image" src="cross.gif" tabIndex="0" onclick="deleteClicked(this.id,'NBS_UI_37','patternNBS_UI_37','questionbodyNBS_UI_37');return false" name="image" align="middle" 	cellspacing="2" cellpadding="3" border="55" class="cursorHand" title="Delete" alt="Delete">	</td>
<% for(int i=0;i<batchrec.length;i++){%>
<% String validdisplay =batchrec[i][0]; %>
<%    if(batchrec[i][4] != null && batchrec[i][2].equals("Y"))  {%>
<% String per = batchrec[i][4];
int aInt = (Integer.parseInt(per)) *91/100;
%>
<td width="<%=aInt%>%" align="left">
<span id="table<%=batchrec[i][0]%>"> </span>
</td>
<%} %>
<%} %>
</tr>
</tbody>
<tbody id="questionbodyNBS_UI_37">
<tr id="nopatternNBS_UI_37" class="odd" style="display:">
<td colspan="<%=batchrec.length+1%>"> <span>&nbsp; No Data has been entered.
</span>
</td>
</tr>
</tbody>
</table>
</td>
<td width="5%"> &nbsp; </td>
</tr>
</Table>
</td>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="NBS674L" title="Please enter the performing lab specimen ID number for this lab test.">
Performing Laboratory Specimen ID:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(NBS674)" size="25" maxlength="25" title="Please enter the performing lab specimen ID number for this lab test." styleId="NBS674" onkeyup="unhideBatchImg('NBS_UI_37');"/>
</td> </tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="FDD_Q_1141L" title="State laboratory specimen identification number.">
State Lab Specimen ID:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(FDD_Q_1141)" size="25" maxlength="25" title="State laboratory specimen identification number." styleId="FDD_Q_1141" onkeyup="unhideBatchImg('NBS_UI_37');"/>
</td> </tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="INV949L" title="NCBI SRA Accession Number (SRX#), the accession number generated by NCBI�s Sequence Read Archive when sequence data are uploaded to NCBI. This provides both the sequence data and metadata on how the sample was sequenced.">
NCBI SRA Accession Number (SRX #):</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(INV949)" size="25" maxlength="25" title="NCBI SRA Accession Number (SRX#), the accession number generated by NCBI�s Sequence Read Archive when sequence data are uploaded to NCBI. This provides both the sequence data and metadata on how the sample was sequenced." styleId="INV949" onkeyup="unhideBatchImg('NBS_UI_37');"/>
</td> </tr>

<!--processing Date Question-->
<tr><td class="fieldName">
<span class="
InputFieldLabel" id="LAB163L" title="Date of collection of laboratory specimen used for diagnosis of health event reported in this case report. Time of collection is an optional addition to date.">
Specimen Collection Date:</span>
</td>
<td>
<html:text name="PageForm"  property="pageClientVO.answer(LAB163)" maxlength="10" size="10" styleId="LAB163" onkeyup="unhideBatchImg('NBS_UI_37');DateMask(this,null,event)" styleClass="NBS_UI_37" title="Date of collection of laboratory specimen used for diagnosis of health event reported in this case report. Time of collection is an optional addition to date."/>
<html:img src="calendar.gif" alt="Select a Date" onclick="getCalDate('LAB163','LAB163Icon'); unhideBatchImg('NBS_UI_37');return false;" styleId="LAB163Icon" onkeypress="showCalendarEnterKey('LAB163','LAB163Icon',event)"></html:img>
</td> </tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class="NBS_UI_37 InputFieldLabel" id="667469L" title="Indicate the type of specimen tested.">
Specimen Type:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(667469)" styleId="667469" title="Indicate the type of specimen tested." onchange="unhideBatchImg('NBS_UI_37');">
<nedss:optionsCollection property="codedValue(SPECIMEN_C_AURIS)" value="key" label="value" /> </html:select>
</td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class="NBS_UI_37 InputFieldLabel" id="LAB165L" title="If specimen type is unspecified swab, please provide anatomical site of swab.">
Anatomical Site of Swab (if Specimen Type is unspecified swab):</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(LAB165)" styleId="LAB165" title="If specimen type is unspecified swab, please provide anatomical site of swab." onchange="unhideBatchImg('NBS_UI_37');enableOrDisableOther('LAB165');pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(SPECIMEN_SOURCE_C_AURIS)" value="key" label="value" /> </html:select>
</td></tr>
<!--Other entry allowed for this Coded Question-->
<tr><td class="fieldName">
<span class="InputDisabledLabel otherEntryField" title="If specimen type is unspecified swab, please provide anatomical site of swab." id="LAB165OthL">Other Anatomical Site of Swab (if Specimen Type is unspecified swab):</span></td>
<td><html:text name="PageForm" disabled="true" property="pageClientVO.answer(LAB165Oth)" size="40" maxlength="40" title="Other If specimen type is unspecified swab, please provide anatomical site of swab." onkeyup="unhideBatchImg('NBS_UI_37')" styleId="LAB165Oth"/></td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span style="color:#CC0000">*</span>
<span class="requiredInputFieldNBS_UI_37 InputFieldLabel" id="INV290L" title="Epidemiologic interpretation of the type of test(s) performed for this case.">
Test Type:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(INV290)" styleId="INV290" title="Epidemiologic interpretation of the type of test(s) performed for this case." onchange="unhideBatchImg('NBS_UI_37');enableOrDisableOther('INV290');pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(LAB_TEST_TYPE_C_AURIS)" value="key" label="value" /> </html:select>
</td></tr>
<!--Other entry allowed for this Coded Question-->
<tr><td class="fieldName">
<span class="InputDisabledLabel otherEntryField" title="Epidemiologic interpretation of the type of test(s) performed for this case." id="INV290OthL">Other Test Type:</span></td>
<td><html:text name="PageForm" disabled="true" property="pageClientVO.answer(INV290Oth)" size="40" maxlength="40" title="Other Epidemiologic interpretation of the type of test(s) performed for this case." onkeyup="unhideBatchImg('NBS_UI_37')" styleId="INV290Oth"/></td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class="NBS_UI_37 InputFieldLabel" id="INV291L" title="Epidemiologic interpretation of the results of the test(s) performed for this case. This is a qualitative test result (e.g, positive, negative).">
Test Result:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(INV291)" styleId="INV291" title="Epidemiologic interpretation of the results of the test(s) performed for this case. This is a qualitative test result (e.g, positive, negative)." onchange="unhideBatchImg('NBS_UI_37');">
<nedss:optionsCollection property="codedValue(TEST_RESULT_C_AURIS)" value="key" label="value" /> </html:select>
</td></tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="NBS482L" title="Specify the test method (Text) for the type of test performed for this case (e.g., test brand or software, or type of PCR).">
Test Method:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(NBS482)" size="50" maxlength="50" title="Specify the test method (Text) for the type of test performed for this case (e.g., test brand or software, or type of PCR)." styleId="NBS482" onkeyup="unhideBatchImg('NBS_UI_37');"/>
</td> </tr>
<% String disableSubmitButton="no";
if(request.getAttribute("disableSubmitButton") != null){
disableSubmitButton= request.getAttribute("disableSubmitButton").toString();
}
%>
<%if(disableSubmitButton.equals("yes")) {%>
<tr id="AddButtonToggleNBS_UI_37">
<td colspan="2" align="right">
<input type="button" value="     Add     "   disabled="disabled" onclick="if (pgNBS_UI_37BatchAddFunction()) writeQuestion('NBS_UI_37','patternNBS_UI_37','questionbodyNBS_UI_37')"/>&nbsp;&nbsp;
&nbsp;
</td>
</tr>
<%} else {%>
<tr id="AddButtonToggleNBS_UI_37">
<td colspan="2" align="right">
<input type="button" value="     Add     "  onclick="if (pgNBS_UI_37BatchAddFunction()) writeQuestion('NBS_UI_37','patternNBS_UI_37','questionbodyNBS_UI_37');"/>&nbsp;&nbsp;
&nbsp;
</td>
</tr>
<%} %>
<tr id="UpdateButtonToggleNBS_UI_37"
style="display:none">
<td colspan="2" align="right">
<input type="button" value="   Update   "    onclick="if (pgNBS_UI_37BatchAddFunction()) writeQuestion('NBS_UI_37','patternNBS_UI_37','questionbodyNBS_UI_37');"/>&nbsp;		&nbsp;
&nbsp;
</td>
</tr>
<tr id="AddNewButtonToggleNBS_UI_37"
style="display:none">
<td colspan="2" align="right">
<input type="button" value="  Add New  "  onclick="clearClicked('NBS_UI_37')"/>&nbsp;	&nbsp;&nbsp;
</td>
</tr>
</nedss:container>

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_42" name="Specimen Information" isHidden="F" classType="subSect" >

<!--processing Static Comment-->
<tr><td colspan="2" align="left"><span id="NBS_UI_46L">&nbsp;&nbsp;If multiple specimens were collected, Location/County/State should be for the first positive specimen associated with this incident of disease</span> </td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="90041_5L" title="Indicate the physical location type of the patient when the specimen was collected.">
Location of Specimen Collection:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(90041_5)" styleId="90041_5" title="Indicate the physical location type of the patient when the specimen was collected." onchange="enableOrDisableOther('90041_5');pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(SPECIMEN_COLLECTION_SETTING_TYPE_C_AURIS)" value="key" label="value" /></html:select>
</td></tr>
<!--Other entry allowed for this Coded Question-->
<tr><td class="fieldName">
<span class="InputDisabledLabel otherEntryField" title="Indicate the physical location type of the patient when the specimen was collected." id="90041_5OthL">Other Location of Specimen Collection:</span></td>
<td><html:text name="PageForm" disabled="true" property="pageClientVO.answer(90041_5Oth)" size="40" maxlength="40" title="Other Indicate the physical location type of the patient when the specimen was collected." styleId="90041_5Oth"/></td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="68488_6L" title="State of facility where specimen was collected">
State of facility where specimen was collected:</span>
</td>
<td>

<!--processing State Coded Question-->
<html:select name="PageForm" property="pageClientVO.answer(68488_6)" styleId="68488_6" title="State of facility where specimen was collected">
<html:optionsCollection property="stateList" value="key" label="value" /> </html:select>
</td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="89202_6L" title="County of facility where specimen was collected">
County of facility where specimen was collected:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(89202_6)" styleId="89202_6" title="County of facility where specimen was collected" onchange="enableOrDisableOther('89202_6');pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(PHVS_COUNTY_FIPS_6-4)" value="key" label="value" /></html:select>
</td></tr>
<!--Other entry allowed for this Coded Question-->
<tr><td class="fieldName">
<span class="InputDisabledLabel otherEntryField" title="County of facility where specimen was collected" id="89202_6OthL">Other County of facility where specimen was collected:</span></td>
<td><html:text name="PageForm" disabled="true" property="pageClientVO.answer(89202_6Oth)" size="40" maxlength="40" title="Other County of facility where specimen was collected" styleId="89202_6Oth"/></td></tr>
</nedss:container>
</nedss:container>

<!-- ################# SECTION ################  -->
<nedss:container id='<%= sectionNames[sectionIndex].replaceAll(" ", "") %>' name="<%= sectionNames[sectionIndex++] %>" isHidden="F" classType="sect">

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_45" name="Previously Counted Case" isHidden="F" classType="subSect" >

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="INV1109L" title="Was patient previously counted as a colonization/screening case?">
Previously counted as a colonization or screening case?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(INV1109)" styleId="INV1109" title="Was patient previously counted as a colonization/screening case?">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>
</nedss:container>

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_47" name="Case IDs" isHidden="F" classType="subSect" >

<!--processing Static Comment-->
<tr><td colspan="2" align="left"><span id="NBS_UI_39L">&nbsp;&nbsp;Please provide the related Previously Reported Case ID(s) if patient was previously counted as a colonization/screening case of C. auris or a CP-CRE case.</span> </td></tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="INV1110_1L" title="If patient was previously counted as a colonization/screening case (clinical C. auris only) or a CP-CRE case, please provide the related case ID(s).">
Previously Reported State Case Number 1:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(INV1110_1)" size="30" maxlength="30" title="If patient was previously counted as a colonization/screening case (clinical C. auris only) or a CP-CRE case, please provide the related case ID(s)." styleId="INV1110_1"/>
</td> </tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="INV1110_2L" title="If patient was previously counted as a colonization/screening case (clinical C. auris only) or a CP-CRE case, please provide the related case ID(s).">
Previously Reported State Case Number 2:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(INV1110_2)" size="30" maxlength="30" title="If patient was previously counted as a colonization/screening case (clinical C. auris only) or a CP-CRE case, please provide the related case ID(s)." styleId="INV1110_2"/>
</td> </tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="INV1110_3L" title="If patient was previously counted as a colonization/screening case (clinical C. auris only) or a CP-CRE case, please provide the related case ID(s).">
Previously Reported State Case Number 3:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(INV1110_3)" size="30" maxlength="30" title="If patient was previously counted as a colonization/screening case (clinical C. auris only) or a CP-CRE case, please provide the related case ID(s)." styleId="INV1110_3"/>
</td> </tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="INV1110_4L" title="If patient was previously counted as a colonization/screening case, please provide the related case ID(s)">
Previously Reported State Case Number 4:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(INV1110_4)" size="30" maxlength="30" title="If patient was previously counted as a colonization/screening case, please provide the related case ID(s)" styleId="INV1110_4"/>
</td> </tr>

<!--processing Text Question-->
<tr> <td class="fieldName">
<span class="InputFieldLabel" id="INV1110_5L" title="If patient was previously counted as a colonization/screening case, please provide the related case ID(s).">
Previously Reported State Case Number 5:</span>
</td>
<td>
<html:text name="PageForm" property="pageClientVO.answer(INV1110_5)" size="30" maxlength="30" title="If patient was previously counted as a colonization/screening case, please provide the related case ID(s)." styleId="INV1110_5"/>
</td> </tr>
</nedss:container>

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_41" name="History of Infection" isHidden="F" classType="subSect" >

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="INV1270L" title="Does the patient have a history of infection or colonization with another MDRO?">
History of infection or colonization with another MDRO?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(INV1270)" styleId="INV1270" title="Does the patient have a history of infection or colonization with another MDRO?" onchange="ruleEnDisINV12708783();enableOrDisableOther('INV831');pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Multi-Select Coded Question-->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="INV831L" title="If patient has a history of infection or colonization with another MDRO, indicate the MDRO.">
If patient has a history of infection or colonization with another MDRO, indicate the MDRO:</span>
</td>
<td>
<div class="multiSelectBlock">
<i> (Use Ctrl to select more than one) </i> <br/>
<html:select property="pageClientVO.answerArray(INV831)" styleId="INV831" title="If patient has a history of infection or colonization with another MDRO, indicate the MDRO."
multiple="true" size="4"
onchange="displaySelectedOptions(this, 'INV831-selectedValues');enableOrDisableOther('INV831')" >
<nedss:optionsCollection property="codedValue(TYPE_COINFECTION_C_AURIS)" value="key" label="value" /> </html:select>
<div id="INV831-selectedValues" style="margin:0.25em;">
<b> Selected Values: </b>
</div>
</div></td></tr>
<!--Other entry allowed for this Coded Question-->
<tr><td class="fieldName">
<span class="InputDisabledLabel otherEntryField" title="If patient has a history of infection or colonization with another MDRO, indicate the MDRO." id="INV831OthL">Other If patient has a history of infection or colonization with another MDRO, indicate the MDRO:</span></td>
<td><html:text name="PageForm" disabled="true" property="pageClientVO.answer(INV831Oth)" size="40" maxlength="40" title="Other If patient has a history of infection or colonization with another MDRO, indicate the MDRO." styleId="INV831Oth"/></td></tr>
</nedss:container>
</nedss:container>

<!-- ################# SECTION ################  -->
<nedss:container id='<%= sectionNames[sectionIndex].replaceAll(" ", "") %>' name="<%= sectionNames[sectionIndex++] %>" isHidden="F" classType="sect">

<!-- ########### SUB SECTION ###########  -->
<nedss:container id="NBS_UI_44" name="Exposure History" isHidden="F" classType="subSect" >

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="448621002L" title="Did patient have a tracheostomy tube at the time of specimen collection?">
At the time of specimen collection, did patient have a tracheostomy tube?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(448621002)" styleId="448621002" title="Did patient have a tracheostomy tube at the time of specimen collection?">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="250870006L" title="Was patient on a ventilator at the time of specimen collection?">
At the time of specimen collection, was patient on a ventilator?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(250870006)" styleId="250870006" title="Was patient on a ventilator at the time of specimen collection?">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Coded Question  -->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="INV636L" title="Did the patient have a stay in a long-term care facility in the 90 days before specimen collection date?">
In the 90 days prior to specimen collection date, did the patient stay in a long-term care facility?:</span>
</td>
<td>
<html:select name="PageForm" property="pageClientVO.answer(INV636)" styleId="INV636" title="Did the patient have a stay in a long-term care facility in the 90 days before specimen collection date?" onchange="ruleEnDisINV6368787();enableOrDisableOther('INV1120');pgSelectNextFocus(this);">
<nedss:optionsCollection property="codedValue(YNU)" value="key" label="value" /></html:select>
</td></tr>

<!--processing Multi-Select Coded Question-->
<tr><td class="fieldName">
<span class=" InputFieldLabel" id="INV1120L" title="If patient had a stay in a long-term care facility in the 90 days before specimen collection date, indicate the type of long-term care facility.">
Long-term Care Facility Type:</span>
</td>
<td>
<div class="multiSelectBlock">
<i> (Use Ctrl to select more than one) </i> <br/>
<html:select property="pageClientVO.answerArray(INV1120)" styleId="INV1120" title="If patient had a stay in a long-term care facility in the 90 days before specimen collection date, indicate the type of long-term care facility."
multiple="true" size="4"
onchange="displaySelectedOptions(this, 'INV1120-selectedValues');enableOrDisableOther('INV1120')" >
<nedss:optionsCollection property="codedValue(LONG_TERM_CARE_FACILITY_TYPE_C_AURIS)" value="key" label="value" /> </html:select>
<div id="INV1120-selectedValues" style="margin:0.25em;">
<b> Selected Values: </b>
</div>
</div></td></tr>
<!--Other entry allowed for this Coded Question-->
<tr><td class="fieldName">
<span class="InputDisabledLabel otherEntryField" title="If patient had a stay in a long-term care facility in the 90 days before specimen collection date, indicate the type of long-term care facility." id="INV1120OthL">Other Long-term Care Facility Type:</span></td>
<td><html:text name="PageForm" disabled="true" property="pageClientVO.answer(INV1120Oth)" size="40" maxlength="40" title="Other If patient had a stay in a long-term care facility in the 90 days before specimen collection date, indicate the type of long-term care facility." styleId="INV1120Oth"/></td></tr>
</nedss:container>
</nedss:container>
<div class="tabNavLinks">
<a href="javascript:navigateTab('previous')"> Previous </a>&nbsp;&nbsp;&nbsp;
<a href="javascript:navigateTab('next')"> Next </a>
<input name="endOfTab" type="hidden"/>
</div>
</div> </td></tr>
