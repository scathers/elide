/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.yahoo.elide.datastores.aggregation.metric;

/**
 * An aggregation class that aggregates to return min.
 */
public class Min implements Aggregation {

    private static final long serialVersionUID = -6582997654294965367L;

    /**
     * Returns a singleton instance of this {@link Aggregation} function.
     *
     * @return the same function that calculates the min
     */
    public static Aggregation getInstance() {
        return INSTANCE;
    }

    private static final Aggregation INSTANCE = new Min();
    private static final String AGG_FUNC_FORMAT = "MIN(%s)";

    @Override
    public String getAggFunctionFormat() {
        return AGG_FUNC_FORMAT;
    }
}