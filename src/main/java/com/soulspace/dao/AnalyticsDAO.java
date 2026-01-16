package com.soulspace.dao;

import java.util.Map;

public interface AnalyticsDAO {
    Map<String, Object> getStudentAnalytics(Long userId);
}