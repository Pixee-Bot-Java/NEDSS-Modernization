package gov.cdc.nbs.questionbank.valueset.read;

import gov.cdc.nbs.questionbank.valueset.ValueSetFinder;
import gov.cdc.nbs.questionbank.valueset.ValueSetSearchResponseMapper;
import gov.cdc.nbs.questionbank.valueset.request.ValueSetSearchRequest;
import gov.cdc.nbs.questionbank.valueset.response.ValueSetSearchResponse;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValueSetFinderTest {

  @Mock
  JdbcTemplate jdbcTemplate;

  @InjectMocks
  ValueSetFinder valueSetFinder;

  @Captor
  private ArgumentCaptor<PreparedStatementSetter> setterCaptor;


  public ValueSetFinderTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void searchValueSet_validSearchCriteria_returnListOfValueSet() {
    ValueSetSearchRequest searchRequest = new ValueSetSearchRequest("setNm", "setCd", "desc");
    List<ValueSetSearchResponse> expectedResponse = getListOfValueSetSearchResponses();
    when(jdbcTemplate.query(anyString(), (PreparedStatementSetter) any(),
        any(ValueSetSearchResponseMapper.class))).thenReturn(
        expectedResponse);
    when(jdbcTemplate.query(any(String.class), any(PreparedStatementSetter.class), any(SingleColumnRowMapper.class)))
        .thenReturn(Collections.singletonList(10));
    Pageable pageable = mock(Pageable.class);
    Sort sort = mock(Sort.class);
    when(sort.isSorted()).thenReturn(true);
    when(sort.toString()).thenReturn("searchField");
    when(pageable.getSort()).thenReturn(sort);
    Page<ValueSetSearchResponse> actualResponse = valueSetFinder.findValueSet(searchRequest, pageable);
    assertEquals(expectedResponse, actualResponse.toList());
  }

  @Test
  void searchValueSet_inValidSearchCriteria_returnEmptyList() {
    ValueSetSearchRequest searchRequest = new ValueSetSearchRequest("setNm", "setCd", "desc");
    when(jdbcTemplate.query(anyString(), (PreparedStatementSetter) any(), any(RowMapper.class))).thenReturn(
        Collections.EMPTY_LIST);
    when(jdbcTemplate.query(any(String.class), any(PreparedStatementSetter.class), any(SingleColumnRowMapper.class)))
        .thenReturn(Collections.singletonList(0));
    Pageable pageable = Pageable.ofSize(5);
    Page<ValueSetSearchResponse> actualResponse = valueSetFinder.findValueSet(searchRequest, pageable);
    assertEquals(0, actualResponse.getTotalElements());
  }

  @Test
  void testValueSearchParametersSet() {
    ValueSetSearchRequest searchRequest = new ValueSetSearchRequest("setNm", "setCd", "desc");
    List<ValueSetSearchResponse> expectedResponse = getListOfValueSetSearchResponses();
    when(jdbcTemplate.query(anyString(), setterCaptor.capture(), any(RowMapper.class)))
        .thenAnswer(invocation -> {
          PreparedStatementSetter setter = setterCaptor.getValue();
          PreparedStatement preparedStatement = mock(PreparedStatement.class);
          setter.setValues(preparedStatement);
          return expectedResponse;
        });

    when(jdbcTemplate.query(any(String.class), setterCaptor.capture(), any(SingleColumnRowMapper.class)))
        .thenAnswer(invocation -> {
          PreparedStatementSetter setter = setterCaptor.getValue();
          PreparedStatement preparedStatement = mock(PreparedStatement.class);
          setter.setValues(preparedStatement);
          return Collections.singletonList(10);
        });

    Pageable pageable = Pageable.ofSize(5);
    Page<ValueSetSearchResponse> actualResponse = valueSetFinder.findValueSet(searchRequest, pageable);
    assertEquals(expectedResponse, actualResponse.toList());
  }

  @Test
  void testMapRow() throws SQLException {
    ValueSetSearchResponseMapper mapper = new ValueSetSearchResponseMapper();
    ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.getString(1)).thenReturn("LOCAL");
    when(resultSet.getString(2)).thenReturn("setCd");
    when(resultSet.getString(3)).thenReturn("setNm");
    when(resultSet.getString(4)).thenReturn("desc");
    when(resultSet.getString(5)).thenReturn("Active");

    ValueSetSearchResponse response = mapper.mapRow(resultSet, 1);
    assertEquals("LOCAL", response.type());
    assertEquals("setCd", response.valueSetCode());
    assertEquals("setNm", response.valueSetName());
    assertEquals("desc", response.valueSetDescription());
    assertEquals("Active", response.valueSetStatus());
  }



  private List<ValueSetSearchResponse> getListOfValueSetSearchResponses() {
    List<ValueSetSearchResponse> response = new ArrayList<>();
    response.add(new ValueSetSearchResponse("LOCAL", "setCode_1",
        "setnm_1", "descText_1", "Active",100l));

    response.add(new ValueSetSearchResponse("LOCAL", "setCode_2",
        "setnm_2", "descText_2", "Active",123l));
    return response;

  }
}
