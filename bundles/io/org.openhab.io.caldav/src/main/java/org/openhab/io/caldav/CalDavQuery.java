/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.caldav;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Query container for requesting calDAV events.
 *
 * @author Robert Delbrück
 * @since 1.8.0
 *
 */
public class CalDavQuery {
    private List<String> calendarIds = new ArrayList<String>();
    private DateTime from;
    private DateTime to;
    private Sort sort;
    private String filterName;
    private List<String> filterCategory;
    private boolean filterCategoryMatchesAny = false;

    public CalDavQuery() {
        super();
    }

    public CalDavQuery(String calendarId) {
        this.calendarIds.add(calendarId);
    }

    public CalDavQuery(List<String> calendarId) {
        this.calendarIds.addAll(calendarId);
    }

    public CalDavQuery(String calendarId, DateTime from) {
        this(calendarId);
        this.from = from;
    }

    public CalDavQuery(List<String> calendarId, DateTime from) {
        this(calendarId);
        this.from = from;
    }

    public CalDavQuery(List<String> calendarId, DateTime from, DateTime to) {
        this(calendarId, from);
        this.to = to;
    }

    public CalDavQuery(List<String> calendarId, DateTime from, Sort sort) {
        this(calendarId, from);
        this.sort = sort;
    }

    public List<String> getCalendarIds() {
        return calendarIds;
    }

    public void setCalendarIds(List<String> calendarIds) {
        this.calendarIds = calendarIds;
    }

    public DateTime getFrom() {
        return from;
    }

    public void setFrom(DateTime from) {
        this.from = from;
    }

    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public void setFilterCategoryMatchesAny(boolean filterCategoryMatchesAny) {
        this.filterCategoryMatchesAny = filterCategoryMatchesAny;
    }

    public boolean getFilterCategoryMatchesAny() {
        return filterCategoryMatchesAny;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<String> getFilterCategory() {
        return filterCategory;
    }

    public void setFilterCategory(List<String> filterCategory) {
        this.filterCategory = filterCategory;
    }

    @Override
    public String toString() {
        return "CalDavQuery [calendarIds=" + calendarIds + ", from=" + from + ", to=" + to + ", sort=" + sort
                + ", filterName=" + filterName + "]";
    }

    public enum Sort {
        ASCENDING,
        DESCENDING
    }
}
