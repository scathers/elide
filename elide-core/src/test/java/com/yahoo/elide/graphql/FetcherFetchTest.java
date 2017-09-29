/*
 * Copyright 2017, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */

package com.yahoo.elide.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import graphql.ExecutionResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test the Fetch operation.
 */
public class FetcherFetchTest extends PersistentResourceFetcherTest {
    private static String NOT_IMPLEMENTED = "not implemented";

    @Test
    public void testRootSingle() throws JsonProcessingException {
        String graphQLRequest = "{"
                + "book(ids: [\"1\"]) { "
                + "id "
                + "title "
                + "authors {"
                + "id "
                + "name "
                + "}"
                + "}"
                + "}";
        String expectedResponse = "{"
                + "\"book\":[{"
                + "\"id\":\"1\","
                + "\"title\":\"Libro Uno\","
                + "\"authors\":[{"
                + "\"id\":\"1\","
                + "\"name\":\"Mark Twain\""
                + "}]"
                + "}]"
                + "}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testRootMultipleIds() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "book(ids: [\"1\", \"2\"]) { "
                + "id "
                + "title "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"book\":[{"
                + "\"id\":\"1\","
                + "\"title\":\"Libro Uno\""
                + "},"
                + "{"
                + "\"id\":\"2\","
                + "\"title\":\"Libro Dos\""
                + "}]"
                + "}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testRootCollection() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "book { "
                + "id "
                + "title "
                + "genre "
                + "language "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"book\":[{"
                + "\"id\":\"1\","
                + "\"title\":\"Libro Uno\","
                + "\"genre\":null,"
                + "\"language\":null"
                + "},"
                + "{"
                + "\"id\":\"2\","
                + "\"title\":\"Libro Dos\","
                + "\"genre\":null,"
                + "\"language\":null"
                + "},"
                + "{\"id\":\"3\",\"title\":\"Doctor Zhivago\",\"genre\":null,\"language\":null}"
                + "]"
                + "}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testNestedSingle() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "author(ids: [\"1\"]) { "
                + "name "
                + "penName { "
                + "name "
                + "} "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"author\":[{"
                + "\"name\":\"Mark Twain\","
                + "\"penName\":{"
                + "\"name\":\"The People's Author\""
                + "}"
                + "}]"
                + "}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testNestedCollection() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "author(ids: [\"1\"]) { "
                + "books(ids: [\"1\"]) { "
                + "id "
                + "title "
                + "} "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"author\":[{"
                + "\"books\":[{"
                + "\"id\":\"1\","
                + "\"title\":\"Libro Uno\""
                + "}]"
                + "}]"
                + "}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testRootCollectionSort() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "book(sort: \"-title\") { "
                + "id "
                + "title "
                + "} "
                + "}";
        String expectedResponse = "{\"book\":[{\"id\":\"1\",\"title\":\"Libro Uno\"},{\"id\":\"2\","
                + "\"title\":\"Libro Dos\"},{\"id\":\"3\",\"title\":\"Doctor Zhivago\"}]}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testRootCollectionMultiSort() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "book(sort: \"-publisher.id,id\") { "
                + "id "
                + "title "
                + "publisher {"
                + "  id"
                + "}"
                + "} "
                + "}";
        String expectedResponse = "{\"book\":[{\"id\":\"3\",\"title\":\"Doctor Zhivago\",\"publisher\":{\"id\":\"2\"}},"
                + "{\"id\":\"1\",\"title\":\"Libro Uno\",\"publisher\":{\"id\":\"1\"}},{\"id\":\"2\","
                + "\"title\":\"Libro Dos\",\"publisher\":{\"id\":\"1\"}}]}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testNestedCollectionSort() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "author(ids: [\"1\"]) { "
                + "books(sort: \"title\") { "
                + "id "
                + "title "
                + "} "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"author\":[{"
                + "\"books\":[{"
                + "\"id\":\"2\","
                + "\"title\":\"Libro Dos\""
                + "},{"
                + "\"id\":\"1\","
                + "\"title\":\"Libro Uno\""
                + "}]"
                + "}]"
                + "}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testRootCollectionPaginate() throws JsonProcessingException {
        /* Only first argument */
        String graphQLRequest = "{ "
                + "book(first: \"1\") { "
                + "id "
                + "title "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"book\":[{"
                + "\"id\":\"1\","
                + "\"title\":\"Libro Uno\""
                + "}]"
                + "}";

        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        assertQueryEquals(graphQLRequest, expectedResponse);

        /* Both first and offset argument */
        graphQLRequest = "{ "
                + "book(first: \"1\", offset: \"1\") { "
                + "id "
                + "title "
                + "} "
                + "}";
        expectedResponse = "{"
                + "\"book\":[{"
                + "\"id\":\"2\","
                + "\"title\":\"Libro Dos\""
                + "}]"
                + "}";
        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testNestedCollectionPaginate() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "author(ids: [\"1\"]) { "
                + "books(first: \"1\", offset: \"1\") { "
                + "id "
                + "title "
                + "} "
                + "} "
                + "}";
        String expectedResponse = "{"
                + "\"author\":[{"
                + "\"books\":[{"
                + "\"id\":\"2\","
                + "\"title\":\"Libro Dos\""
                + "}]"
                + "}]"
                + "}";
        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testNestedCollectionFilter() throws JsonProcessingException {
        String graphQLRequest = "{ author(ids: [\"1\"]) { books(filter: \"title==\\\"Libro U*\\\"\") { id title } } }";
        String expectedResponse = "{\"author\":[{\"books\":[{\"id\":\"1\",\"title\":\"Libro Uno\"}]}]}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testRootCollectionFilter() throws JsonProcessingException {
        String graphQLRequest = "{ book(filter: \"title==\\\"Libro U*\\\"\") { id title } }";
        String expectedResponse = "{\"book\":[{\"id\":\"1\",\"title\":\"Libro Uno\"}]}";

        assertQueryEquals(graphQLRequest, expectedResponse);
    }

    @Test
    public void testFailuresWithBody() throws JsonProcessingException {
        String graphQLRequest = "{ "
                + "book(ids: [\"1\"], data: [{\"id\": \"1\"}]) { "
                + "id "
                + "title "
                + "} "
                + "}";
        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        Assert.assertTrue(!result.getErrors().isEmpty());
    }

    @Test
    public void testPageTotalsRoot() throws JsonProcessingException {
        String graphQLRequest = "{"
                + "book { "
                + "id "
                + "title "
                + "__bookTotalRecords "
                + "}"
                + "}";
        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        List<Map<String, Object>> results = getValuesForRelationship(result, "book");
        for (Map<String, Object> resultValues : results) {
            Assert.assertEquals(resultValues.get("__bookTotalRecords"), 3L);
        }
    }

    @Test
    public void testPageTotalsRootWithFilter() throws JsonProcessingException {
        String graphQLRequest = "{"
                + "book(filter: \"title==\\\"Libro*\\\"\") { "
                + "id "
                + "title "
                + "__bookTotalRecords "
                + "}"
                + "}";
        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        List<Map<String, Object>> results = getValuesForRelationship(result, "book");
        for (Map<String, Object> resultValues : results) {
            Assert.assertEquals(resultValues.get("__bookTotalRecords"), 2L);
        }
    }

    @Test
    public void testPageTotalsRootWithPagination() throws JsonProcessingException {
        String graphQLRequest = "{"
                + "book(first: \"1\", offset: \"1\") { "
                + "id "
                + "title "
                + "__bookTotalRecords "
                + "}"
                + "}";
        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        List<Map<String, Object>> results = getValuesForRelationship(result, "book");
        for (Map<String, Object> resultValues : results) {
            Assert.assertEquals(resultValues.get("__bookTotalRecords"), 3L);
        }
    }

    @Test
    public void testPageTotalsRootWithIds() throws JsonProcessingException {
        String graphQLRequest = "{"
                + "book(ids: [\"1\"]) { "
                + "id "
                + "title "
                + "__bookTotalRecords "
                + "}"
                + "}";
        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        List<Map<String, Object>> results = getValuesForRelationship(result, "book");
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.get(0).get("__bookTotalRecords"), 1L);
    }

    @Test
    public void testPageTotalsRelationship() throws JsonProcessingException {
        String graphQLRequest = "{"
                + "author { "
                + "id "
                + "books { "
                + "id "
                + "__bookTotalRecords "
                + "}"
                + "}"
                + "}";
        ExecutionResult result = api.execute(graphQLRequest, requestScope);
        List<Map<String, Object>> results = getValuesForRelationship(result, "author");
        Map<String, Object> author1 = results.get(0);
        Map<String, Object> author2 = results.get(1);
        List<Map<String, Object>> books = (List) author1.get("books");
        // Author1 has 2 books
        for (Map<String, Object> book : books) {
            Assert.assertEquals(book.get("__bookTotalRecords"), 2L);
        }

        books = (List) author2.get("books");
        // Author2 has 1 book
        for (Map<String, Object> book : books) {
            Assert.assertEquals(book.get("__bookTotalRecords"), 1L);
        }
    }

    private List<Map<String, Object>> getValuesForRelationship(ExecutionResult result, String relName) {
        Map<String, List<Map<String, Object>>> resultMap = (Map) result.getData();
        return resultMap.get(relName);
    }
}