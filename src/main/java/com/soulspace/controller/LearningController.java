package com.soulspace.controller;

import com.soulspace.dao.LearningLessonDAO; 
import com.soulspace.dao.EnrollmentDAO; 
import com.soulspace.dao.LessonProgressDAO; 
import com.soulspace.model.Learning;
import com.soulspace.model.LearningLesson;
import com.soulspace.model.Enrollment;
import com.soulspace.service.LearningService;
import com.soulspace.service.CourseProgressService; 
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @Autowired private LearningService learningService;
    @Autowired private LearningLessonDAO learningLessonDAO; 
    @Autowired private CourseProgressService progressService; 
    @Autowired private EnrollmentDAO enrollmentDAO; 
    @Autowired private LessonProgressDAO lessonProgressDAO; 

    // 1. Browse Page (Existing)
    @GetMapping
    public String browseCourses(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false, defaultValue = "All Categories") String category,
            @RequestParam(name = "level", required = false, defaultValue = "All Levels") String level,
            @RequestParam(name = "sort", required = false, defaultValue = "pop") String sort,
            HttpSession session, 
            Model model) {
        
        List<Learning> courses = learningService.searchCourses(search, category, level);
        
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 

        // Create a Map of [CourseID -> ProgressPercent]
        Map<Long, Integer> enrollmentMap = new HashMap<>();
        
        List<Enrollment> userEnrollments = enrollmentDAO.findByUserId(userId);
        if (userEnrollments != null) {
            for (Enrollment e : userEnrollments) {
                enrollmentMap.put(e.getCourse().getId(), e.getProgressPercent());
            }
        }

        model.addAttribute("courses", courses);
        model.addAttribute("enrollmentMap", enrollmentMap); 
        
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedLevel", level);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("selectedSort", sort);
        
        return "learning";
    }

    // 2. Course Detail Page (Existing)
    @GetMapping("/{id}")
    public String courseDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        Learning course = learningService.getModuleById(id);
        if (course == null) return "redirect:/learning";

        List<LearningLesson> curriculum = learningLessonDAO.findByCourseId(id);
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 

        boolean isEnrolled = learningService.isUserEnrolled(userId, id);
        int currentProgress = 0;
        
        List<Long> completedLessonIds = new ArrayList<>();
        
        if(isEnrolled) {
             // Calculate completed lessons
             if (curriculum != null && !curriculum.isEmpty()) {
                 for (LearningLesson lesson : curriculum) {
                     if (progressService.isLessonCompleted(userId, lesson.getId())) {
                         completedLessonIds.add(lesson.getId());
                     }
                 }
                 // Calculate progress dynamically
                 currentProgress = (int) Math.round(((double) completedLessonIds.size() / curriculum.size()) * 100);
             }
        }

        model.addAttribute("course", course);
        model.addAttribute("curriculum", curriculum); 
        model.addAttribute("isEnrolled", isEnrolled);
        model.addAttribute("courseProgress", currentProgress);
        model.addAttribute("completedLessonIds", completedLessonIds);

        return "learning-detail";
    }

    // 3. Enrollment Actions (Existing)
    @PostMapping("/enroll/{id}")
    public String enrollCourse(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 
        learningService.enrollUser(userId, id);
        return "redirect:/learning/" + id;
    }

    @PostMapping("/unenroll/{id}")
    public String unenrollCourse(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 
        
        lessonProgressDAO.deleteProgressByCourse(userId, id);
        
        learningService.unenrollUser(userId, id);
        return "redirect:/learning/" + id;
    }

    // 4. My Courses Page (Existing)
    @GetMapping("/my-courses")
    public String myCourses(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 

        List<Enrollment> enrollments = enrollmentDAO.findByUserId(userId);

        // Recalculate progress for every course
        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                Long courseId = enrollment.getCourse().getId();
                List<LearningLesson> lessons = learningLessonDAO.findByCourseId(courseId);
                
                if (lessons != null && !lessons.isEmpty()) {
                    int totalLessons = lessons.size();
                    int completedCount = 0;
                    
                    // Check actual completion status for each lesson
                    for (LearningLesson lesson : lessons) {
                        if (progressService.isLessonCompleted(userId, lesson.getId())) {
                            completedCount++;
                        }
                    }
                    
                    // Calculate percentage
                    int realProgress = (int) Math.round(((double) completedCount / totalLessons) * 100);
                    
                    // Update the enrollment object in memory
                    enrollment.setProgressPercent(realProgress);
                } else {
                    enrollment.setProgressPercent(0);
                }
            }
        }

        model.addAttribute("enrollments", enrollments);
        return "learning-my-courses";
    }
    
    // 5. Analytics Page (Existing)
    @GetMapping("/analytics")
    public String analytics(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 

        Map<String, Object> stats = progressService.getUserAnalytics(userId);
        model.addAttribute("stats", stats);
        return "learning-analytics";
    }

    // ---------------------------------------------------------
    // NEW METHODS: Play Lesson & Complete Lesson (Fixes 404)
    // ---------------------------------------------------------

    @GetMapping("/play/{courseId}/{lessonId}")
    public String playLesson(
            @PathVariable("courseId") Long courseId, 
            @PathVariable("lessonId") Long lessonId, 
            HttpSession session,
            Model model) {
        
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L;

        // 1. Fetch Course and Curriculum
        Learning course = learningService.getModuleById(courseId);
        List<LearningLesson> curriculum = learningLessonDAO.findByCourseId(courseId);
        
        LearningLesson currentLesson = null;
        int currentLessonIndex = 0;
        
        // 2. Find the specific lesson
        if (curriculum != null) {
            for (int i = 0; i < curriculum.size(); i++) {
                if (curriculum.get(i).getId().equals(lessonId)) {
                    currentLesson = curriculum.get(i);
                    currentLessonIndex = i + 1; // 1-based index for display
                    break;
                }
            }
        }

        // Safety check: redirect if not found
        if (course == null || currentLesson == null) return "redirect:/learning/" + courseId;

        // 3. Check completion status
        boolean isCompleted = progressService.isLessonCompleted(userId, lessonId);

        // 4. Calculate progress dynamically for the player view
        int completedCount = 0;
        int totalLessons = (curriculum != null) ? curriculum.size() : 0;
        
        if (curriculum != null) {
            for (LearningLesson l : curriculum) {
                if (progressService.isLessonCompleted(userId, l.getId())) {
                    completedCount++;
                }
            }
        }
        
        int progressPercent = (totalLessons > 0) ? (int) Math.round(((double) completedCount / totalLessons) * 100) : 0;

        // 5. Add attributes for the view
        model.addAttribute("course", course); // Necessary for the "Back" button
        model.addAttribute("lesson", currentLesson);
        model.addAttribute("currentLessonIndex", currentLessonIndex);
        model.addAttribute("totalLessons", totalLessons);
        model.addAttribute("progressPercent", progressPercent);
        model.addAttribute("lessonCompleted", isCompleted);

        return "learning-play";
    }

    @PostMapping("/complete-lesson")
    public String completeLesson(
            @RequestParam("lessonId") Long lessonId,
            @RequestParam("courseId") Long courseId,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L;

        // 1. Mark current lesson as complete
        progressService.markLessonComplete(userId, lessonId);

        // 2. Find the next lesson to redirect to
        List<LearningLesson> curriculum = learningLessonDAO.findByCourseId(courseId);
        Long nextLessonId = null;
        
        if (curriculum != null) {
            for(int i = 0; i < curriculum.size(); i++) {
                if(curriculum.get(i).getId().equals(lessonId)) {
                    // If there is a next lesson, grab its ID
                    if(i + 1 < curriculum.size()) {
                        nextLessonId = curriculum.get(i + 1).getId();
                    }
                    break;
                }
            }
        }

        // 3. Redirect
        if (nextLessonId != null) {
            return "redirect:/learning/play/" + courseId + "/" + nextLessonId;
        } else {
            // Course finished, go back to details
            return "redirect:/learning/" + courseId;
        }
    }
}