package com.factual.driver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Represents a top level Factual query. Knows how to represent the query as URL
 * encoded key value pairs, ready for the query string in a GET request. (See
 * {@link #toUrlQuery()})
 * 
 * @author aaron
 */
public class Query implements Filterable {

  private boolean includeRowCount;

  /**
   * Holds all parameters for this Query.
   */
  protected final Parameters queryParams = new Parameters();

  /**
   * Sets a full text search query. Factual will use this value to perform a
   * full text search against various attributes of the underlying table, such
   * as entity name, address, etc.
   * 
   * @param term
   *          the text for which to perform a full text search.
   * @return this Query
   */
  public Query search(String term) {
    addParam(Constants.SEARCH, term);
    return this;
  }

  protected String search() {
    Object val;
    return ((val = queryParams.getParam(Constants.SEARCH)) != null) ? val.toString() : null;
  }

  /**
   * Chooses an existence threshold. For on the latest enumuration of values 
   *  that can be passed into the threshold value, please see the Read API
   *  documentation: http://developer.factual.com/api-docs/#Read, in particular
   *  the threshold parameter.
   *  
   * @param threshold
   * @return
   */
  public Query threshold(String threshold) {
      addParam(Constants.THRESHOLD, threshold);
      return this;
    }

  /**
   * Sets the maximum amount of records to return from this Query.
   * @param limit the maximum amount of records to return from this Query.
   * @return this Query
   */
  public Query limit(long limit) {
    if (limit >= 0)
      addParam(Constants.QUERY_LIMIT, limit);
    else
      throw new RuntimeException("Limit must be >= 0");
    return this;
  }

  /**
   * Sets the fields to select. This is optional; default behaviour is generally
   * to select all fields in the schema.
   * 
   * @param fields
   *          the fields to select.
   * @return this Query
   */
  public Query only(String... fields) {
    for (String field : fields) {
      queryParams.addCommaSeparatedParam(Constants.QUERY_SELECT, field);
    }
    return this;
  }

  /**
   * @return array of select fields set by only(), null if none.
   */
  public String[] getSelectFields() {
    return queryParams.getCommaSeparatedParam(Constants.QUERY_SELECT);
  }

  /**
   * For any arbitrary custom sorts.
   * 
   * @param sort
   * @return
   */
  public Query sort(String sort) {
      addParam(Constants.QUERY_SORT, sort);
      return this;
  }

  /**
   * Sets this Query to sort field in ascending order.
   * 
   * @param field
   *          the field to sort in ascending order.
   * @return this Query
   */
  public Query sortAsc(String field) {
    queryParams.addCommaSeparatedParam(Constants.QUERY_SORT, field + ":asc");
    return this;
  }

  /**
   * Sets this Query to sort field in descending order.
   * 
   * @param field
   *          the field to sort in descending order.
   * @return this Query
   */
  public Query sortDesc(String field) {
    queryParams.addCommaSeparatedParam(Constants.QUERY_SORT, field + ":desc");
    return this;
  }

  /**
   * Sets how many records in to start getting results (i.e., the page offset)
   * for this Query.
   * 
   * @param offset
   *          the page offset for this Query.
   * @return this Query
   */
  public Query offset(long offset) {
    if (offset >= 0)
      addParam(Constants.QUERY_OFFSET, offset);
    else
      throw new RuntimeException("Offset must be >= 0");
    return this;
  }

  /**
   * The response will include a count of the total number of rows in the table
   * that conform to the request based on included filters. This will increase
   * the time required to return a response. The default behavior is to NOT
   * include a row count.
   * 
   * @return this Query, marked to return total row count when run.
   */
  public Query includeRowCount() {
    return includeRowCount(true);
  }

  /**
   * When true, the response will include a count of the total number of rows in
   * the table that conform to the request based on included filters.
   * Requesting the row count will increase the time required to return a
   * response. The default behavior is to NOT include a row count.
   * 
   * @param includeRowCount
   *          true if you want the results to include a count of the total
   *          number of rows in the table that conform to the request based on
   *          included filters.
   * @return this Query.
   */
  public Query includeRowCount(boolean includeRowCount) {
    this.includeRowCount = includeRowCount;
    return this;
  }

  /**
   * Begins construction of a new row filter.
   * 
   * @param field
   *          the name of the field on which to filter.
   * @return A partial representation of the new row filter.
   * @deprecated use {@link #field(String)}
   */
  @Deprecated
  public QueryBuilder<Query> criteria(String field) {
    return new QueryBuilder<Query>(this, field);
  }

  /**
   * Begins construction of a new row filter for this Query.
   * 
   * @param field
   *          the name of the field on which to filter.
   * @return A partial representation of the new row filter.
   */
  public QueryBuilder<Query> field(String field) {
    return new QueryBuilder<Query>(this, field);
  }

  /**
   * Adds a filter so that results can only be (roughly) within the specified
   * geographic circle.
   * 
   * @param circle The circle within which to bound the results.
   * @return this Query.
   */
  public Query within(Shape shape) {
    queryParams.setParam(Constants.FILTER_GEO, shape);
    return this;
  }

  /**
   * Adds a filter to return results that are geographically near the
   * specified Point.
   * 
   * @param point The point for which all results should be geographically near.
   * @return this Query.
   */
  public Query near(Point point) {
    queryParams.setParam(Constants.FILTER_GEO, point);
    return this;
  }

  /**
   * Used to nest AND'ed predicates.
   */
  public Query and(Query... queries) {
    queryParams.popFilters(Constants.FILTER_AND, queries);
    return this;
  }

  /**
   * Used to nest OR'ed predicates.
   */
  public Query or(Query... queries) {
    queryParams.popFilters(Constants.FILTER_OR, queries);
    return this;
  }

  /**
   * Adds <tt>filter</tt> to this Query.
   */
  @Override
  public void add(Filter filter) {
    queryParams.add(filter);
  }

  /**
   * @param user
   *          An (optional) arbitrary token for correlating read and boost
   *          requests to a single app/session/etc. Factual does not use this
   *          token to track users. The function of this information is only to
   *          help evaluate how a boost relates to a search.
   * @return this Query
   */
  public Query user(String user) {
    addParam(Constants.USER, user);
    return this;
  }

  protected String user() {
    Object val;
    return ((val = queryParams.getParam(Constants.USER)) != null) ? val.toString() : null;
  }

  /**
   * Set a parameter and value pair for specifying url parameters, specifically those not yet available as convenience methods.
   * @param key the field name of the parameter to add
   * @param value the field value that will be serialized using value.toString()
   * @return this Query
   */
  private Query addParam(String key, Object value) {
    queryParams.setParam(key, value);
    return this;
  }

  /**
   * Builds and returns the query string to represent this Query when talking to
   * Factual's API. Provides proper URL encoding and escaping.
   * <p>
   * Example output:
   * <pre>
   * filters=%7B%22%24and%22%3A%5B%7B%22region%22%3A%7B%22%24in%22%3A%5B%22MA%22%2C%22VT%22%2C%22NH%22%5D%7D%7D%2C%7B%22%24or%22%3A%5B%7B%22first_name%22%3A%7B%22%24eq%22%3A%22Chun%22%7D%7D%2C%7B%22last_name%22%3A%7B%22%24eq%22%3A%22Kok%22%7D%7D%5D%7D%5D%7D
   * </pre>
   * <p>
   * (After decoding, the above example would be used by the server as:)
   * <pre>
   * filters={"$and":[{"region":{"$in":["MA","VT","NH"]}},{"$or":[{"first_name":{"$eq":"Chun"}},{"last_name":{"$eq":"Kok"}}]}]}
   * </pre>
   * 
   * @return the query string to represent this Query when talking to Factual's
   *         API.
   */
  protected Map<String, Object> toUrlParams() {
    Parameters additional = null;
    if (includeRowCount) {
      additional = new Parameters();
      additional.setParam(Constants.INCLUDE_COUNT,true);
    }
    return queryParams.toUrlParams(additional);
  }

  @Override
  public String toString() {
    try {
      return URLDecoder.decode(UrlUtil.toUrlQuery(toUrlParams()), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Filter> getFilterList() {
    return queryParams.getFilterList();
  }

  public String toUrlQuery() {
    return UrlUtil.toUrlQuery(toUrlParams());
  }

  /**
   * Sets this Query to perform a blended sort on the specified field with
   * the specified weight value. See API docs for valid blended sort fields.
   * 
   * @param field
   * @param weight
   * @return
   */
  public Query blendField(String field, int weight) {
    queryParams.setJsonMapParam(Constants.QUERY_SORT, field, weight);
    return this;
  }

  /**
   * Sets this Query to perform a blended sort on rank with the specified
   * weight.
   * 
   * @param weight
   * @return
   */
  public Query blendRank(int weight) {
    return blendField("placerank", weight);
  }

  /**
   * Sets this Query to perform a blended sort on distance with the specified
   * weight. This will only have an effect if a geo filter is used with this
   * query.
   * 
   * @param weight
   * @return
   */
  public Query blendDistance(int weight) {
    return blendField("distance", weight);
  }
}
