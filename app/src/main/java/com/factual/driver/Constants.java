package com.factual.driver;

public class Constants {

  // Circle
  protected static final String CIRCLE = "$circle";
  protected static final String CENTER = "$center";
  protected static final String METERS = "$meters";

  // Rectangle
  protected static final String RECTANGLE = "$rect";
  
  
  // Point
  protected static final String POINT = "$point";

  // Response
  protected static final String RESPONSE = "response";
  protected static final String TOTAL_ROW_COUNT = "total_row_count";
  protected static final String INCLUDED_ROWS = "included_rows";
  protected static final String STATUS = "status";
  protected static final String VERSION = "version";

  // Schema
  protected static final String SCHEMA_COLUMN_NAME = "name";
  protected static final String SCHEMA_COLUMN_DESCRIPTION = "description";
  protected static final String SCHEMA_COLUMN_LABEL = "label";
  protected static final String SCHEMA_COLUMN_DATATYPE = "datatype";
  protected static final String SCHEMA_COLUMN_FACETED = "faceted";
  protected static final String SCHEMA_COLUMN_SORTABLE = "sortable";
  protected static final String SCHEMA_COLUMN_SEARCHABLE = "searchable";
  protected static final String SCHEMA_VIEW = "view";
  protected static final String SCHEMA_FIELDS = "fields";
  protected static final String SCHEMA_TITLE = "title";
  protected static final String SCHEMA_DESCRIPTION = "description";
  protected static final String SCHEMA_SEARCH_ENABLED = "search_enabled";
  protected static final String SCHEMA_GEO_ENABLED = "geo_enabled";

  // Crosswalk
  protected static final String CROSSWALK_FACTUAL_ID = "factual_id";
  protected static final String CROSSWALK_LIMIT = "limit";
  protected static final String CROSSWALK_NAMESPACE = "namespace";
  protected static final String CROSSWALK_NAMESPACE_ID = "namespace_id";
  protected static final String CROSSWALK_ONLY = "only";

  protected static final String CROSSWALK_DATA = "data";
  protected static final String CROSSWALK_URL = "url";

  // Diffs
  protected static final String DIFFS_START_DATE = "start-date";
  protected static final String DIFFS_END_DATE = "end-date";
  protected static final String DIFFS_DATA = "data";

  // Filters
  protected static final String FILTERS = "filters";
  protected static final String FILTER_GEO = "geo";
  protected static final String FILTER_AND = "$and";
  protected static final String FILTER_OR = "$or";

  // Common query
  protected static final String INCLUDE_COUNT = "include_count";
  protected static final String SEARCH = "q";
  protected static final String THRESHOLD = "threshold";
  protected static final String USER = "user";

  // Query
  protected static final String QUERY_LIMIT = "limit";
  protected static final String QUERY_OFFSET = "offset";
  protected static final String QUERY_SORT = "sort";
  protected static final String QUERY_SELECT = "select";

  protected static final String QUERY_DATA = "data";

  // Facet
  protected static final String FACET_MIN_COUNT_PER_FACET_VALUE = "min_count";
  protected static final String FACET_MAX_VALUES_PER_FACET = "limit";
  protected static final String FACET_SELECT = "select";

  protected static final String FACET_DATA = "data";

  // Resolve
  protected static final String RESOLVE_VALUES = "values";

  // Submit
  protected static final String SUBMIT_VALUES = "values";
  protected static final String SUBMIT_FACTUAL_ID = "factual_id";
  protected static final String SUBMIT_COMMIT_ID = "commit_id";
  protected static final String SUBMIT_NEW_ENTITY = "new_entity";

  // Insert
  protected static final String INSERT_VALUES = "values";
  protected static final String INSERT_FACTUAL_ID = "factual_id";
  protected static final String INSERT_COMMIT_ID = "commit_id";
  protected static final String INSERT_NEW_ENTITY = "new_entity";

  // Clear
  protected static final String CLEAR_FIELDS = "fields";
  protected static final String CLEAR_COMMIT_ID = "commit_id";
  protected static final String CLEAR_FACTUAL_ID = "factual_id";

  // Boost
  protected static final String BOOST_FACTUAL_ID = "factual_id";


}
