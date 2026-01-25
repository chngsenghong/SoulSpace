-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 25, 2026 at 07:35 AM
-- Server version: 12.2.0-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `soulspace_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `email_notifications` tinyint(1) DEFAULT 1,
  `push_notifications` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `first_name`, `last_name`, `bio`, `role`, `email_notifications`, `push_notifications`) VALUES
(1, 'student@example.com', 'password123', 'John', 'Student', 'Developing my mental resilience and learning focus techniques.', 'STUDENT', 1, 0),
(2, 'pro@example.com', 'password123', 'Sarah', 'Jenkins', 'Clinical Psychologist specializing in Cognitive Behavioral Therapy (CBT) and trauma recovery.', 'PROFESSIONAL', 1, 1),
(3, 'michael@example.com', 'password123', 'Michael', 'Thompson', 'Experienced psychiatrist focused on holistic wellness and mood disorders.', 'PROFESSIONAL', 1, 0),
(4, 'emily@example.com', 'password123', 'Emily', 'Chen', 'Certified mindfulness coach and meditation expert with 10 years of experience.', 'PROFESSIONAL', 1, 1),
(5, 'david@example.com', 'password123', 'David', 'Wilson', 'Specialist in trauma recovery, anxiety management, and adolescent mental health.', 'PROFESSIONAL', 1, 0),
(6, 'linda@example.com', 'password123', 'Linda', 'Gomez', 'Counselor focused on relationship dynamics and family therapy.', 'PROFESSIONAL', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `professional_id` bigint(20) NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_time` time NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `professional_notes` text DEFAULT NULL,
  `follow_up_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`id`, `user_id`, `professional_id`, `appointment_date`, `appointment_time`, `status`, `type`, `professional_notes`, `follow_up_date`) VALUES
(1, 1, 2, '2026-01-20', '10:00:00', 'COMPLETED', 'VIDEO', 'John is showing progress in identifying cognitive distortions. Recommended 4-7-8 breathing.', '2026-02-01'),
(2, 1, 3, '2026-01-22', '14:30:00', 'COMPLETED', 'IN_PERSON', 'Discussed sleep hygiene and stress management. Patient is following the BA plan.', NULL),
(3, 1, 2, '2026-01-26', '11:00:00', 'CONFIRMED', 'VIDEO', NULL, NULL),
(4, 1, 4, '2026-02-05', '09:00:00', 'PENDING', 'VIDEO', NULL, NULL),
(5, 1, 5, '2026-02-10', '15:00:00', 'PENDING', 'IN_PERSON', NULL, NULL),
(6, 1, 2, '2026-01-15', '16:00:00', 'CANCELLED', 'VIDEO', 'User requested cancellation due to family event.', NULL),
(7, 1, 2, '2026-01-24', '09:00:00', 'PENDING', 'VIDEO', NULL, NULL),
(8, 1, 2, '2026-01-25', '14:00:00', 'CONFIRMED', 'IN_PERSON', NULL, NULL),
(9, 1, 2, '2026-01-10', '11:30:00', 'COMPLETED', 'IN_PERSON', 'Initial intake session. Student experiencing mild school-related anxiety.', '2026-01-20');

-- --------------------------------------------------------

--
-- Table structure for table `learning_modules`
--

CREATE TABLE `learning_modules` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `lessons` int(11) DEFAULT 0,
  `rating` double DEFAULT 0,
  `student_count` int(11) DEFAULT 0,
  `link_url` varchar(255) DEFAULT NULL,
  `date_created` date DEFAULT NULL,
  `ai_summary` varchar(500) DEFAULT NULL,
  `ai_tags` varchar(255) DEFAULT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `learning_modules` (`id`, `title`, `author`, `description`, `category`, `level`, `type`, `duration`, `lessons`, `rating`, `student_count`, `link_url`, `date_created`, `ai_summary`, `ai_tags`, `price`) VALUES
(1, 'Cognitive Behavioral Therapy Fundamentals', 'Dr. Michael Thompson', 'Master the basics of CBT to transform negative thought patterns.', 'Therapy', 'Intermediate', 'Course', '8 hours', 32, 4.8, 2340, NULL, '2026-01-15', 'Structured approach to CBT focusing on behavioral transformation.', '#CBT #Therapy', 0),
(2, 'Mindfulness & Meditation Mastery', 'Maya Chen', 'Learn practical mindfulness techniques to reduce stress.', 'Mindfulness', 'Beginner', 'Course', '4 hours', 18, 4.9, 2156, NULL, '2026-01-15', 'Practical techniques for stress reduction suitable for beginners.', '#Mindfulness #Wellness', 0);

-- --------------------------------------------------------

--
-- Table structure for table `student_enrollments`
--

CREATE TABLE `student_enrollments` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `course_id` bigint(20) NOT NULL,
  `enrollment_date` date DEFAULT NULL,
  `progress_percent` int(11) DEFAULT 0,
  `is_completed` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `student_enrollments` (`id`, `user_id`, `course_id`, `enrollment_date`, `progress_percent`, `is_completed`) VALUES
(12, 1, 1, '2026-01-16', 70, 0),
(14, 1, 2, '2026-01-16', 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL,
  `sender_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `content` text NOT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `timestamp` datetime NOT NULL,
  `conversation_status` varchar(255) DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `sender_id`, `receiver_id`, `content`, `is_read`, `timestamp`, `conversation_status`) VALUES
(1, 1, 2, 'Hello Dr. Jenkins, I have been feeling a bit overwhelmed lately with the upcoming exams.', 1, '2026-01-24 10:00:00', 'ACTIVE'),
(2, 2, 1, 'I understand, John. Remember the 4-7-8 breathing technique we discussed in our last session.', 1, '2026-01-24 10:05:00', 'ACTIVE'),
(3, 1, 3, 'Dr. Thompson, I would like to move our next session to Wednesday if possible.', 1, '2026-01-20 15:00:00', 'ARCHIVED'),
(4, 3, 1, 'That works for me. See you then, John.', 1, '2026-01-20 15:10:00', 'ARCHIVED'),
(5, 1, 2, 'I tried the technique, and it really helped calm me down during my study break.', 1, '2026-01-24 11:30:00', 'ACTIVE'),
(6, 2, 1, 'That is great to hear! Consistency is key. Keep practicing it daily.', 1, '2026-01-24 11:45:00', 'ACTIVE'),
(7, 1, 4, 'Hello Emily, I am interested in joining your mindfulness workshop next week.', 1, '2026-01-23 09:00:00', 'ACTIVE'),
(8, 4, 1, 'Hi John! I would love to have you. I will send you the details shortly.', 1, '2026-01-23 09:15:00', 'ACTIVE'),
(9, 1, 5, 'Dr. Wilson, I am having some trouble with the anxiety management exercises.', 1, '2026-01-22 14:00:00', 'ACTIVE'),
(10, 5, 1, 'No problem at all, we can go over them in detail during our next session on the 30th.', 1, '2026-01-22 14:30:00', 'ACTIVE');


-- --------------------------------------------------------


--
-- Indexes for dumped tables
--

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

ALTER TABLE `appointments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `professional_id` (`professional_id`);

ALTER TABLE `learning_modules`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `student_enrollments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `course_id` (`course_id`),
  ADD KEY `user_id` (`user_id`);

ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`);

--
-- AUTO_INCREMENT for dumped tables
--

ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `appointments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

ALTER TABLE `learning_modules`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

ALTER TABLE `student_enrollments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

ALTER TABLE `messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Constraints for dumped tables
--

ALTER TABLE `appointments`
  ADD CONSTRAINT `fk_appointments_professional` FOREIGN KEY (`professional_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_appointments_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `student_enrollments`
  ADD CONSTRAINT `fk_student_enrollments_course` FOREIGN KEY (`course_id`) REFERENCES `learning_modules` (`id`),
  ADD CONSTRAINT `fk_student_enrollments_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `messages`
  ADD CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_messages_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
