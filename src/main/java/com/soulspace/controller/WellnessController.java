package com.soulspace.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wellness")
public class WellnessController {

    @GetMapping
    public String showWellnessTracker(Model model) {
        // 1. Stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("currentMoodText", "😊 Good");
        stats.put("moodTrend", "+15% from last week");
        stats.put("sleepAvg", 7.5);
        stats.put("sleepTrend", "+0.5 hrs improved");
        model.addAttribute("stats", stats);

        // 2. Chart Data (Mock)
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> point = new HashMap<>();
            point.put("percentage", (int)(Math.random() * 60) + 40); // 40-100%
            point.put("label", "Day " + (i+1));
            point.put("value", "Score");
            chartData.add(point);
        }
        model.addAttribute("chartData", chartData);

        // 3. Recent Activity
        List<Map<String, String>> activities = new ArrayList<>();
        Map<String, String> act1 = new HashMap<>();
        act1.put("type", "mood");
        act1.put("title", "Mood Logged");
        act1.put("timeAgo", "2 hours ago");
        act1.put("value", "Good");
        activities.add(act1);
        
        model.addAttribute("recentActivities", activities);

        return "wellness";
    }

    @PostMapping("/log")
    public String logMood(
        @RequestParam("mood") String mood, 
        @RequestParam("note") String note) {
        
        System.out.println("Logged mood: " + mood + ", Note: " + note);
        return "redirect:/wellness";
    }
}