package com.soulspace.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.dao.EnrollmentDAO;
import com.soulspace.dao.LearningLessonDAO;
import com.soulspace.dao.LessonProgressDAO;
import com.soulspace.model.Enrollment;
import com.soulspace.model.Learning;
import com.soulspace.model.LearningLesson;
import com.soulspace.service.CourseProgressService;
import com.soulspace.service.LearningService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @Autowired private LearningService learningService;
    @Autowired private LearningLessonDAO learningLessonDAO; 
    @Autowired private CourseProgressService progressService; 
    @Autowired private EnrollmentDAO enrollmentDAO; 
    @Autowired private LessonProgressDAO lessonProgressDAO; 

    // --- FACULTY DEMO LOGIN (Access this to see Manage Button) ---
    @GetMapping("/demo/faculty")
    public String demoFacultyLogin(HttpSession session) {
        session.setAttribute("userId", 99L);
        session.setAttribute("role", "FACULTY"); // This enables the sidebar button
        session.setAttribute("user", "Dr. Faculty");
        session.setAttribute("email", "faculty@soulspace.com");
        return "redirect:/learning/manage";
    }

    // --- FACULTY DASHBOARD ---
    @GetMapping("/manage")
    public String manageCourses(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (!"FACULTY".equals(role)) return "redirect:/login";

        List<Learning> allCourses = learningService.getAllModules();
        model.addAttribute("courses", allCourses);
        return "learning-manage";
    }

    // --- STUDENT BROWSE (Default) ---
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

    // --- FACULTY CRUD OPERATIONS ---
    
    @GetMapping("/create")
    public String createCourseForm(HttpSession session, Model model) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("course", new Learning());
        return "learning-form";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        Learning course = learningService.getModuleById(id);
        List<LearningLesson> lessons = learningLessonDAO.findByCourseId(id);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        return "learning-form";
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Learning learning, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        if (learning.getId() != null) {
            Learning existing = learningService.getModuleById(learning.getId());
            learning.setStudentCount(existing.getStudentCount());
            learning.setRating(existing.getRating());
            learning.setDateCreated(existing.getDateCreated());
        } else {
            learning.setStudentCount(0);
            learning.setRating(0.0);
        }
        learningService.saveModule(learning);
        return "redirect:/learning/manage";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        learningService.deleteModule(id);
        return "redirect:/learning/manage";
    }
    
    // --- LESSON CRUD ---

    @GetMapping("/{courseId}/lesson/add")
    public String addLessonForm(@PathVariable("courseId") Long courseId, HttpSession session, Model model) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        LearningLesson lesson = new LearningLesson();
        lesson.setCourseId(courseId);
        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", courseId);
        return "learning-lesson-form";
    }
    
    @GetMapping("/{courseId}/lesson/edit/{lessonId}")
    public String editLessonForm(@PathVariable("courseId") Long courseId, 
                                 @PathVariable("lessonId") Long lessonId, 
                                 HttpSession session, Model model) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        
        LearningLesson lesson = learningLessonDAO.findByCourseId(courseId).stream()
                .filter(l -> l.getId().equals(lessonId)).findFirst().orElse(null);

        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", courseId);
        return "learning-lesson-form";
    }

    @PostMapping("/{courseId}/lesson/save")
    public String saveLesson(@PathVariable("courseId") Long courseId, @ModelAttribute LearningLesson lesson, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        lesson.setCourseId(courseId);
        
        Learning course = learningService.getModuleById(courseId);
        if(lesson.getId() == null) {
            course.getCurriculum().add(lesson);
        } else {
            for(int i=0; i<course.getCurriculum().size(); i++) {
                if(course.getCurriculum().get(i).getId().equals(lesson.getId())) {
                    course.getCurriculum().set(i, lesson);
                    break;
                }
            }
        }
        learningService.saveModule(course);
        return "redirect:/learning/edit/" + courseId;
    }

    @PostMapping("/{courseId}/lesson/delete/{lessonId}")
    public String deleteLesson(@PathVariable("courseId") Long courseId, @PathVariable("lessonId") Long lessonId, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role"))) return "redirect:/login";
        Learning course = learningService.getModuleById(courseId);
        course.getCurriculum().removeIf(l -> l.getId().equals(lessonId));
        learningService.saveModule(course);
        return "redirect:/learning/edit/" + courseId;
    }

    // --- EXISTING STUDENT ENDPOINTS (Details, Enroll, Play, Analytics) ---
    // (Keep your existing methods for detail, enroll, play here...)
    
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
             if (curriculum != null && !curriculum.isEmpty()) {
                 for (LearningLesson lesson : curriculum) {
                     if (progressService.isLessonCompleted(userId, lesson.getId())) {
                         completedLessonIds.add(lesson.getId());
                     }
                 }
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

    @GetMapping("/play/{courseId}/{lessonId}")
    public String playLesson(@PathVariable("courseId") Long courseId, @PathVariable("lessonId") Long lessonId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L;
        Learning course = learningService.getModuleById(courseId);
        List<LearningLesson> curriculum = learningLessonDAO.findByCourseId(courseId);
        LearningLesson currentLesson = null;
        int currentLessonIndex = 0;
        if (curriculum != null) {
            for (int i = 0; i < curriculum.size(); i++) {
                if (curriculum.get(i).getId().equals(lessonId)) {
                    currentLesson = curriculum.get(i);
                    currentLessonIndex = i + 1;
                    break;
                }
            }
        }
        if (course == null || currentLesson == null) return "redirect:/learning/" + courseId;
        boolean isCompleted = progressService.isLessonCompleted(userId, lessonId);
        int completedCount = 0;
        int totalLessons = (curriculum != null) ? curriculum.size() : 0;
        if (curriculum != null) {
            for (LearningLesson l : curriculum) {
                if (progressService.isLessonCompleted(userId, l.getId())) completedCount++;
            }
        }
        int progressPercent = (totalLessons > 0) ? (int) Math.round(((double) completedCount / totalLessons) * 100) : 0;
        model.addAttribute("course", course); 
        model.addAttribute("lesson", currentLesson);
        model.addAttribute("currentLessonIndex", currentLessonIndex);
        model.addAttribute("totalLessons", totalLessons);
        model.addAttribute("progressPercent", progressPercent);
        model.addAttribute("lessonCompleted", isCompleted);
        return "learning-play";
    }

    @PostMapping("/complete-lesson")
    public String completeLesson(@RequestParam("lessonId") Long lessonId, @RequestParam("courseId") Long courseId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L;
        progressService.markLessonComplete(userId, lessonId);
        List<LearningLesson> curriculum = learningLessonDAO.findByCourseId(courseId);
        Long nextLessonId = null;
        if (curriculum != null) {
            for(int i = 0; i < curriculum.size(); i++) {
                if(curriculum.get(i).getId().equals(lessonId)) {
                    if(i + 1 < curriculum.size()) nextLessonId = curriculum.get(i + 1).getId();
                    break;
                }
            }
        }
        if (nextLessonId != null) return "redirect:/learning/play/" + courseId + "/" + nextLessonId;
        else return "redirect:/learning/" + courseId;
    }

    @GetMapping("/my-courses")
    public String myCourses(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 
        List<Enrollment> enrollments = enrollmentDAO.findByUserId(userId);
        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                Long courseId = enrollment.getCourse().getId();
                List<LearningLesson> lessons = learningLessonDAO.findByCourseId(courseId);
                if (lessons != null && !lessons.isEmpty()) {
                    int totalLessons = lessons.size();
                    int completedCount = 0;
                    for (LearningLesson lesson : lessons) {
                        if (progressService.isLessonCompleted(userId, lesson.getId())) completedCount++;
                    }
                    enrollment.setProgressPercent((int) Math.round(((double) completedCount / totalLessons) * 100));
                } else {
                    enrollment.setProgressPercent(0);
                }
            }
        }
        model.addAttribute("enrollments", enrollments);
        return "learning-my-courses";
    }
    
    @GetMapping("/analytics")
    public String analytics(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) userId = 1L; 
        Map<String, Object> stats = progressService.getUserAnalytics(userId);
        model.addAttribute("stats", stats);
        return "learning-analytics";
    }
}