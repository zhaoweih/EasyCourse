-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 2018-06-09 00:14:29
-- 服务器版本： 5.6.40
-- PHP Version: 5.5.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mydatabase`
--

-- --------------------------------------------------------

--
-- 表的结构 `t_course`
--

CREATE TABLE `t_course` (
  `id` int(11) NOT NULL,
  `tec_id` int(11) NOT NULL,
  `teacher_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `course_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `course_num` int(11) NOT NULL DEFAULT '0',
  `teacher_name` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_course`
--

INSERT INTO `t_course` (`id`, `tec_id`, `teacher_id`, `course_name`, `course_num`, `teacher_name`, `password`, `description`) VALUES
(4, 26, '20151120', '牛津和爱因斯坦的搏斗', 10, '赵威豪君', '123456', '讲述牛津和爱因斯坦的斗争'),
(5, 26, '20151120', '牛津和爱因斯坦的搏斗', 2, '赵威豪酱', '123456', '讲述牛津和爱因斯坦的斗争'),
(6, 26, '20151120', '牛津和爱因斯坦的搏斗', 1, '赵威豪', '123456', '讲述牛津和爱因斯坦的斗争'),
(7, 26, '20151120', '牛津和爱因斯坦的搏斗', 0, '赵威豪', '123456', '讲述牛津和爱因斯坦的斗争'),
(8, 26, '20151120', '牛津和爱因斯坦的搏斗', 0, '赵威豪', '123456', '讲述牛津和爱因斯坦的斗争'),
(9, 37, '20151120', '瑜伽艺术', 0, 'luna', '123456', '体验柔韧的瑜伽艺术'),
(10, 37, '20151120', '大学语文', 3, '赵威豪', '123456', '语文课'),
(11, 28, '2015191000', '大学英语', 4, 'Mr.David', '123456', '大学英语');

-- --------------------------------------------------------

--
-- 表的结构 `t_course_noti`
--

CREATE TABLE `t_course_noti` (
  `id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `content` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `date` date NOT NULL,
  `tec_id` int(11) NOT NULL,
  `end_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_course_noti`
--

INSERT INTO `t_course_noti` (`id`, `course_id`, `content`, `date`, `tec_id`, `end_date`) VALUES
(6, 4, '课后作业：查询潮州八景', '2018-05-25', 26, '2018-05-30'),
(9, 4, '完成课后布置的作业', '2018-06-02', 26, '2018-06-02'),
(11, 11, '课后作业完成了吗？', '2018-06-02', 28, '2018-06-05'),
(15, 11, '课后练习', '2018-06-07', 28, '2018-06-16'),
(16, 11, '课前讨论', '2018-06-08', 28, '2018-06-10'),
(17, 11, '课后作业', '2018-06-08', 28, '2018-06-08'),
(18, 4, '思考广义相对论和狭义相对论', '2018-06-09', 37, '2018-06-17'),
(19, 4, '下星期期末考试，请相互告知！', '2018-06-09', 37, '2018-06-10'),
(22, 4, '停课', '2018-06-09', 37, '2018-06-10');

-- --------------------------------------------------------

--
-- 表的结构 `t_course_select`
--

CREATE TABLE `t_course_select` (
  `id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `stu_id` int(11) NOT NULL,
  `student_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `course_name` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `teacher_name` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_course_select`
--

INSERT INTO `t_course_select` (`id`, `course_id`, `stu_id`, `student_id`, `course_name`, `teacher_name`) VALUES
(1, 2, 20, '2015191054', '大学语文', NULL),
(2, 10, 20, '2015191054', '大学语文', '赵威豪'),
(3, 10, 20, '2015191054', '大学语文', '赵威豪'),
(4, 4, 20, '2015191054', '大学语文', '赵威豪'),
(5, 4, 31, '2015191052', '牛津和爱因斯坦的搏斗', '赵威豪君'),
(6, 5, 31, '2015191052', '牛津和爱因斯坦的搏斗', '赵威豪酱'),
(7, 4, 31, '2015191052', '牛津和爱因斯坦的搏斗', '赵威豪君'),
(8, 4, 31, '2015191052', '牛津和爱因斯坦的搏斗', '赵威豪君'),
(9, 4, 31, '2015191052', '牛津和爱因斯坦的搏斗', '赵威豪君'),
(10, 6, 31, '2015191052', '牛津和爱因斯坦的搏斗', '赵威豪'),
(11, 4, 27, '2015191036', '牛津和爱因斯坦的搏斗', '赵威豪君'),
(12, 5, 27, '2015191036', '牛津和爱因斯坦的搏斗', '赵威豪酱'),
(14, 11, 30, '2015191036', '大学英语', 'Mr.David'),
(15, 11, 30, '2015191036', '大学英语', '谭新奎'),
(16, 4, 35, '2015191023', '牛津和爱因斯坦的搏斗', '赵威豪君'),
(17, 11, 27, '2015191036', '大学英语', 'Mr.David');

-- --------------------------------------------------------

--
-- 表的结构 `t_discuss`
--

CREATE TABLE `t_discuss` (
  `id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `teacher_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `tec_id` int(11) DEFAULT NULL,
  `start_date` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_date` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `content` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `img_url` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_discuss`
--

INSERT INTO `t_discuss` (`id`, `course_id`, `teacher_id`, `tec_id`, `start_date`, `end_date`, `status`, `content`, `img_url`) VALUES
(5, 6, '2015191036', 28, '2018-05-19', '2018-06-05', 1, '课前讨论一下', NULL),
(31, 11, '2015191036', 28, '2018-06-07', '2018-06-09', 1, '讨论一下', NULL),
(34, 11, '2015191036', 28, '2018-06-08', '2018-07-01', 1, '课前讨论一下', NULL),
(35, 11, '2015191036', 28, '2018-06-08', '2018-06-08', 1, '分组讨论', NULL),
(37, 4, '20151120', 37, '2018-06-09', '2018-07-02', 1, '最早使用“物理学”这个词的是谁？', NULL),
(38, 4, '20151120', 37, '2018-06-09', '2018-07-01', 1, '“格物穷理”是由谁提出来的？', NULL),
(39, 4, '20151120', 37, '2018-06-09', '2018-07-01', 1, '相对论是关于什么的基本理论，分为什么？', NULL),
(40, 4, '20151120', 37, '2018-06-09', '2018-06-12', 1, '举例经典物理的定律，原理。', NULL),
(41, 4, '20151120', 37, '2018-06-09', '2018-06-12', 1, '如何评价“光量子说”？', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `t_discuss_comment`
--

CREATE TABLE `t_discuss_comment` (
  `id` int(11) NOT NULL,
  `discuss_id` int(11) NOT NULL,
  `content` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `student_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `stu_id` int(11) DEFAULT NULL,
  `date` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_discuss_comment`
--

INSERT INTO `t_discuss_comment` (`id`, `discuss_id`, `content`, `student_id`, `stu_id`, `date`) VALUES
(7, 4, '讨论什么？', '2015191036', 10, '2018-40-07'),
(8, 4, '哈哈哈，没人？', '2015191036', 10, '2018-40-07'),
(9, 3, '不懂？', '2015191036', 10, '2018-41-07'),
(10, 4, '解释一下？', '2015191036', 10, '2018-41-07'),
(11, 4, '什么？', '2015191036', 10, '2018-42-07'),
(29, 3, 'hello', '2015191036', 12, '2018-06-07'),
(30, 3, '哈哈', '2015191036', 12, '2018-06-07'),
(33, 1, '好的', '2015191036', 18, '2018-06-07'),
(38, 33, '讨论一下！', '2015191036', 20, '2018-06-07'),
(39, 28, '哈哈', '2015191036', 26, '2018-06-08'),
(40, 37, '应该是亚里士多德吧！', '2015191036', 66, '2018-06-09'),
(41, 37, '牛顿？', '2015191043', 67, '2018-06-09'),
(42, 37, '难道是爱因斯坦吗？各位', '2015191023', 68, '2018-06-09'),
(43, 38, '朱熹？', '2015191023', 68, '2018-06-09'),
(44, 39, '时间，广义和狭义。', '2015191023', 68, '2018-06-09'),
(45, 38, '鲁迅吗', '2015191036', 24, '2018-06-09'),
(46, 40, '相对论啊，量子说啊！', '2015191023', 68, '2018-06-09'),
(47, 41, '波粒二象性？', '2015191023', 68, '2018-06-09'),
(48, 38, '鲁迅？？', '2015191043', 69, '2018-06-09'),
(49, 37, '亚里士多德，没错了，就他！', '2015191043', 69, '2018-06-09'),
(50, 39, '准确说是基于时间和空间好不好。', '2015191043', 69, '2018-06-09'),
(51, 39, '分为广义相对论和狭义相对论。', '2015191043', 69, '2018-06-09'),
(52, 38, '张载吧，也许是陆九渊，还有可能是王阳明。', '2015191043', 69, '2018-06-09'),
(53, 40, '万有引力啊，热力学说啊', '2015191043', 69, '2018-06-09'),
(54, 40, '热力学第一定律', '2015191043', 69, '2018-06-09'),
(55, 41, '这是一个大胆的假设', '2015191043', 69, '2018-06-09'),
(56, 41, '光原子和电子一样具有粒子性', '2015191043', 69, '2018-06-09'),
(57, 41, '光是以光速C运动着的粒子流，把这种粒子叫光量子', '2015191043', 69, '2018-06-09'),
(58, 39, '楼上说的对', '2015191036', 70, '2018-06-09'),
(59, 41, '厉害666', '2015191036', 70, '2018-06-09'),
(60, 38, '上面出现学霸', '2015191036', 24, '2018-06-09');

-- --------------------------------------------------------

--
-- 表的结构 `t_leave`
--

CREATE TABLE `t_leave` (
  `id` int(11) NOT NULL,
  `student_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `teacher_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `stu_id` int(11) NOT NULL,
  `tec_id` int(11) DEFAULT NULL,
  `content` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  `tec_advise` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `start_num` int(11) DEFAULT NULL,
  `end_num` int(11) DEFAULT NULL,
  `course_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_leave`
--

INSERT INTO `t_leave` (`id`, `student_id`, `teacher_id`, `stu_id`, `tec_id`, `content`, `status`, `tec_advise`, `start_date`, `end_date`, `start_num`, `end_num`, `course_id`) VALUES
(1, '2015191054', '20151120', 20, 25, '5月20日请假', 2, '不行', NULL, NULL, NULL, NULL, NULL),
(2, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '不行', NULL, NULL, NULL, NULL, NULL),
(3, '2015191054', '20151120', 20, 25, '5月20日请假', 2, '你请假太多了', NULL, NULL, NULL, NULL, NULL),
(4, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '好', NULL, NULL, NULL, NULL, NULL),
(5, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '好', NULL, NULL, NULL, NULL, NULL),
(6, '2015191054', '20151120', 20, 25, '5月20日请假', 2, '不行', NULL, NULL, 7, 8, NULL),
(7, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '去吧', '2018-05-18', '2018-05-19', 7, 8, NULL),
(8, '2015191054', '20151120', 20, 25, '5月20日请假', 2, '不行', '2018-05-18', '2018-05-19', 7, 8, NULL),
(9, '2015191054', '20151120', 20, 25, '5月20日请假', 2, '不行', '2018-05-18', '2018-05-19', 7, 8, NULL),
(10, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '你去吧', '2018-05-18', '2018-05-19', 7, 8, NULL),
(11, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '去吧', '2018-05-28', '2018-05-19', 7, 8, NULL),
(12, '2015191054', '20151120', 20, 25, '5月20日请假', 3, '你去吧', NULL, NULL, 7, 8, NULL),
(18, '2015191036', '2015191036', 27, 28, '有事，能否请假', 3, NULL, '2018-05-19', '2018-05-21', 1, 4, NULL),
(19, '2015191025', '2015191036', 33, 28, '有事，能否请假', 3, '同意了', '2018-05-19', '2018-05-19', 1, 1, NULL),
(20, '2015191023', '2015191036', 35, 28, '有事，要请假', 3, '好的', '2018-05-19', '2018-05-20', 1, 7, NULL),
(21, '2015191023', '2015191036', 35, 28, '可以请假吗请假', 2, '不行', '2018-05-21', '2018-05-21', 4, 4, NULL),
(22, '2015191023', '2015191036', 35, 28, '公假，走不开', 3, '行吧', '2018-05-19', '2018-05-22', 2, 7, NULL),
(23, '2015191036', '2015191036', 34, 28, '公假，要请假，不能来', 2, '学习要紧', '2018-05-20', '2018-05-21', 1, 5, NULL),
(24, '2015191036', '2015191036', 34, 28, '有事不能来', 3, '可以', '2018-05-23', '2018-05-20', 6, 6, NULL),
(27, '2015191036', '2015191036', 27, 28, '有事不能来', 2, '不行啊。', '2018-05-20', '2018-05-21', 2, 3, NULL),
(28, '2015191054', '20151120', 20, 25, '我想请假', 3, '好', '2018-05-28', '2018-05-19', 7, 8, NULL),
(29, '2015191036', '2015191036', 27, 28, '有事', 3, '好吧', '2018-05-21', '2018-05-22', 4, 8, NULL),
(30, '2015191052', '20151120', 31, 25, '想去看看世界', 3, '好', '2018-06-04', '2018-06-04', 6, 6, NULL),
(31, '2015191036', '2015191036', 27, 28, '有事', 3, '好的', '2018-06-05', '2018-06-06', 1, 5, NULL),
(32, '2015191036', '20151120', 27, 25, '有事不能来', 3, '好', '2018-06-05', '2018-06-05', 1, 1, NULL),
(33, '2015191036', '2015191036', 27, 28, '有事', 2, NULL, '2018-06-05', '2018-06-05', 1, 1, NULL),
(34, '2015191036', '20151120', 27, 25, '123', 3, '好', '2018-06-08', '2018-06-08', 1, 2, 4),
(35, '2015191036', '2015191036', 27, 28, '123', 3, '好', '2018-06-08', '2018-06-08', 2, 6, 11),
(36, '2015191036', '2015191000', 27, 28, '有事不能来', 3, '好吧', '2018-06-08', '2018-06-09', 1, 4, 11),
(37, '2015191052', '20151120', 31, 25, '肚子疼', 1, NULL, '2018-06-10', '2018-06-08', 6, 7, 4),
(38, '2015191036', '20151120', 27, 25, '公假', 3, '好', '2018-06-09', '2018-06-09', 3, 6, 4),
(39, '2015191036', '20151120', 27, 25, '有事', 2, '不行，什么事？', '2018-06-10', '2018-06-09', 1, 5, 4);

-- --------------------------------------------------------

--
-- 表的结构 `t_quiz`
--

CREATE TABLE `t_quiz` (
  `id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `teacher_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `tec_id` int(11) DEFAULT NULL,
  `quiz_num` int(11) DEFAULT '1',
  `student_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `stu_id` int(11) DEFAULT NULL,
  `student_name` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_quiz`
--

INSERT INTO `t_quiz` (`id`, `course_id`, `teacher_id`, `tec_id`, `quiz_num`, `student_id`, `stu_id`, `student_name`) VALUES
(3, 4, '20151120', NULL, 3, '2015191054', NULL, '赵威豪'),
(4, 4, '20151120', NULL, 2, '2015191052', NULL, '毛灿华'),
(5, 4, '20151120', NULL, 1, '2015191036', NULL, '谭新奎');

-- --------------------------------------------------------

--
-- 表的结构 `t_seat_record`
--

CREATE TABLE `t_seat_record` (
  `id` int(11) NOT NULL,
  `student_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `stu_id` int(11) DEFAULT NULL,
  `class_code` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `class_column` int(11) NOT NULL,
  `class_row` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_seat_record`
--

INSERT INTO `t_seat_record` (`id`, `student_id`, `stu_id`, `class_code`, `class_column`, `class_row`) VALUES
(1, '2015191054', NULL, '20780', 4, 4),
(2, '2015191055', NULL, '20780', 4, 5),
(3, '2015191054', NULL, '20774', 4, 3),
(4, '2015191054', NULL, '20774', 4, 3),
(5, '2015191054', NULL, '20774', 5, 3),
(6, '2015191054', NULL, '20774', 6, 3),
(7, '2015191054', NULL, '20774', 7, 3),
(8, '2015191054', NULL, '20774', 8, 3),
(9, '2015191054', NULL, '20774', 7, 4),
(10, '2015191054', NULL, '20774', 6, 4),
(11, '2015191054', NULL, '20774', 7, 5),
(12, '2015191054', NULL, '20774', 8, 5),
(13, '2015191052', NULL, '20774', 3, 4),
(14, '2015191052', NULL, '20781', 5, 3),
(15, '2015191052', NULL, '20781', 5, 4),
(16, '2015191052', NULL, '20781', 6, 2),
(17, '2015191052', NULL, '20780', 5, 4),
(18, '2015191052', NULL, '20780', 6, 4),
(19, '2015191052', NULL, '20780', 7, 4),
(20, '2015191036', NULL, '20774', 1, 1),
(21, '2015191052', NULL, '20780', 6, 5),
(22, '2015191036', NULL, '20780', 7, 6),
(23, '2015191052', NULL, '20780', 7, 5),
(24, '2015191052', NULL, '20780', 7, 3),
(25, '2015191052', NULL, '20780', 5, 5),
(26, '2015191036', NULL, '20774', 3, 1),
(27, '2015191036', NULL, '20774', 3, 1),
(28, '2015191036', NULL, '20780', 4, 3),
(29, '2015191036', NULL, '20799', 2, 1),
(30, '2015191036', NULL, '20798', 2, 1),
(31, '2015191052', NULL, '20780', 6, 2),
(32, '2015191052', NULL, '20780', 6, 3),
(33, '2015191036', NULL, '20780', 5, 3);

-- --------------------------------------------------------

--
-- 表的结构 `t_seat_select`
--

CREATE TABLE `t_seat_select` (
  `id` int(11) NOT NULL,
  `class_code` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `class_json` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_seat_select`
--

INSERT INTO `t_seat_select` (`id`, `class_code`, `class_json`) VALUES
(4, '20772', '{\"rowNum\":15,\"columnNum\":25,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|N\",\"columnTypes\":\"N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N\",\"columnStates\":\"N|N|N|U|U|U|U|U|U|U|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N\"}]}'),
(5, '20773', '{\"rowNum\":15,\"columnNum\":25,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|N\",\"columnTypes\":\"N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N\",\"columnStates\":\"N|N|N|U|U|U|U|U|U|U|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N\"}]}'),
(7, '20774', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|U|A|A|U|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|U|U|U|U|U|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|U|U|U|U|U|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|U|A|A|U|U|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|U|U|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(8, '20775', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(10, '20776', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(11, '20777', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(12, '20778', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(14, '20779', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(15, '20780', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|U|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|U|U|U|U|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|U|U|U|U|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|U|U|U|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|U|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(17, '20781', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|U|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|U|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|U|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(18, '20789', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(19, '', 'null'),
(20, '20775', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(21, '20799', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|U|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(22, '20798', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|U|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(23, '1111', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}'),
(24, '1111', '{\"rowNum\":7,\"columnNum\":16,\"addRow\":[{\"row\":0,\"rowId\":\"1\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":1,\"rowId\":\"2\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":2,\"rowId\":\"3\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":3,\"rowId\":\"4\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":4,\"rowId\":\"5\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":5,\"rowId\":\"6\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"},{\"row\":6,\"rowId\":\"7\",\"columnIds\":\"N|N|N|N|1|2|3|4|5|6|7|8|9|10|N|N\",\"columnTypes\":\"N|N|N|N|S|S|S|S|S|S|S|S|S|S|N|N\",\"columnStates\":\"N|N|N|N|A|A|A|A|A|A|A|A|A|A|N|N\"}]}');

-- --------------------------------------------------------

--
-- 表的结构 `t_user`
--

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(25) NOT NULL,
  `name` varchar(25) NOT NULL,
  `sex` int(11) NOT NULL DEFAULT '1',
  `school` varchar(25) NOT NULL,
  `date` varchar(25) NOT NULL,
  `education` int(11) NOT NULL DEFAULT '1',
  `department` varchar(25) NOT NULL,
  `class_id` varchar(25) NOT NULL,
  `student_id` varchar(25) DEFAULT NULL,
  `teacher_id` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_user`
--

INSERT INTO `t_user` (`id`, `username`, `password`, `name`, `sex`, `school`, `date`, `education`, `department`, `class_id`, `student_id`, `teacher_id`) VALUES
(20, 'zhaoweihao33', 'abbccc', '赵威豪', 1, '韩山师范学院', '2015', 1, '哈哈', '20151911', '2015191054', NULL),
(23, 'zhaoweihao34', 'abbccc', '赵威豪', 1, '韩山师范学院', '2015', 1, '哈哈', '20151911', '2015191054', NULL),
(24, 'zhaoweihao35', 'abbccc', '赵威豪', 1, '韩山师范学院', '2015', 1, '哈哈', '20151911', '2015191054', NULL),
(25, 'zhaoweihao79', 'abbccc', '赵威豪', 1, '韩山师范学院', '2015', 1, '哈哈', '20151911', '20191000', '20151120'),
(26, 'zhaoweihao80', 'abbccc', '赵威豪', 1, '韩山师范学院', '2015', 1, '哈哈', '20151911', NULL, '20151120'),
(27, 'TXK', 'TXK', '谭新奎', 0, '韩山师范学院', '2015', 1, '教育学系', '20151911', '2015191036', NULL),
(28, 'tea', 'tea', 'Mr.David', 0, '韩山师范学院', '000000', 1, '教育学系', '000000', NULL, '2015191000'),
(29, 'zhaoweihao55', 'abbccc', '赵威豪', 1, '韩山师范学院', '2015', 1, '教育系', '20151911', NULL, '2016666'),
(30, 'TXK2', 'TXK2', '谭新奎', 0, '韩山师范学院', '2015', 1, '教育学系', '20151911', '2015191036', NULL),
(31, 'zhaoweiha8', 'abc123', '毛灿华', 0, '汕头大学', '2015', 1, '外语系', '20151913', '2015191052', NULL),
(32, 'TXK3', 'TXK3', '哈哈哈', 1, '暨南大学', '2010', 1, '外语系', '20151913', '2015191312', NULL),
(33, 'TXK4', 'TXK4', '啊哈', 1, '广东海洋大学', '2018', 1, '政法系', '20151913', '2015191025', NULL),
(34, 'TXK5', 'TXK5', '谭新奎', 1, '暨南大学', '2015', 1, '外语系', '20151911', '2015191043', NULL),
(35, 'TXK6', 'TXK6', '张三', 0, '汕头大学', '2010', 1, '外语系', '2015136', '2015191023', NULL),
(36, 'TXK7', 'TXK7', '啊哈哈', 1, '韩山师范学院', '2012', 1, '中文系', '201111', '2015191035', NULL),
(37, 'zhaowei0', 'abbccc', '赵威豪', 1, '韩山师范学院', '000000', 1, '哈哈', '000000', NULL, '20151120'),
(38, 'TXKK', 'TXKK', '哈哈哈', 0, '韩山师范学院', '2018', 1, '中文系', '20151134', '21046436', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `t_vote`
--

CREATE TABLE `t_vote` (
  `id` int(11) NOT NULL,
  `tec_id` int(11) DEFAULT NULL,
  `teacher_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `choice_num` int(11) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `img_url` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `choice_mode` int(11) DEFAULT '1',
  `choice_max` int(11) DEFAULT '1',
  `choice_json` text COLLATE utf8_unicode_ci NOT NULL,
  `course_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `t_vote`
--

INSERT INTO `t_vote` (`id`, `tec_id`, `teacher_id`, `title`, `choice_num`, `start_date`, `end_date`, `img_url`, `choice_mode`, `choice_max`, `choice_json`, `course_id`) VALUES
(21, NULL, '20151120', 'Java课前测试', 5, NULL, NULL, NULL, 1, 1, '[{\"id\":1,\"title\":\"在JAVA中，负责对字节码解析执行的是\",\"choiceA\":\"应用服务器\",\"choiceB\":\"虚拟机\",\"choiceC\":\"垃圾回收器\",\"choiceD\":\"编译器\",\"numA\":4,\"numB\":5,\"numC\":2,\"numD\":4},{\"id\":2,\"title\":\"LDAP是什么\",\"choiceA\":\"是一种开源产品\",\"choiceB\":\"是一种编程语言\",\"choiceC\":\"是一种访问协议\",\"choiceD\":\"是一种存储数据的目录\",\"numA\":4,\"numB\":4,\"numC\":4,\"numD\":3},{\"id\":3,\"title\":\"下面哪个Set是排序的\",\"choiceA\":\"LinkedHashSet\",\"choiceB\":\"HashSet\",\"choiceC\":\"AbstractSet\",\"choiceD\":\"TreeSet\",\"numA\":3,\"numB\":4,\"numC\":5,\"numD\":3},{\"id\":4,\"title\":\"类的实例方法表示的是什么\",\"choiceA\":\"父类对象的行为\",\"choiceB\":\"类的属性\",\"choiceC\":\"类对象的行为\",\"choiceD\":\"类的行为\",\"numA\":5,\"numB\":3,\"numC\":3,\"numD\":4},{\"id\":5,\"title\":\"要创建一个新目录可以用\",\"choiceA\":\"FileInputStream\",\"choiceB\":\"FileOutputStream\",\"choiceC\":\"RandomAccessFile\",\"choiceD\":\"File\",\"numA\":4,\"numB\":5,\"numC\":1,\"numD\":5}]', 4),
(22, NULL, '20151120', 'maya课前测试', 5, NULL, NULL, NULL, 1, 1, '[{\"id\":1,\"title\":\"maya中设置渲染的模块是\",\"choiceA\":\"Cloth\",\"choiceB\":\"Rendering\",\"choiceC\":\"Dynamics\",\"choiceD\":\"Live\",\"numA\":3,\"numB\":3,\"numC\":3,\"numD\":2},{\"id\":2,\"title\":\"使用maya的最佳分辨率是\",\"choiceA\":\"800*600\",\"choiceB\":\"1024*768\",\"choiceC\":\"1152*864\",\"choiceD\":\"1280*1024\",\"numA\":1,\"numB\":3,\"numC\":5,\"numD\":2},{\"id\":3,\"title\":\"含有动画播放按钮的界面元素是\",\"choiceA\":\"状态栏\",\"choiceB\":\"时间栏\",\"choiceC\":\"范围栏\",\"choiceD\":\"帮助栏\",\"numA\":1,\"numB\":1,\"numC\":5,\"numD\":4},{\"id\":4,\"title\":\"可以通过输入MEL命令来工作的界面元素是\",\"choiceA\":\"状态栏\",\"choiceB\":\"工具架\",\"choiceC\":\"通道框\",\"choiceD\":\"命令行\",\"numA\":2,\"numB\":4,\"numC\":4,\"numD\":1},{\"id\":5,\"title\":\"下面选项中属于maya选择模式的是\",\"choiceA\":\"层级\",\"choiceB\":\"物体\",\"choiceC\":\"元素\",\"choiceD\":\"以上都是\",\"numA\":2,\"numB\":2,\"numC\":4,\"numD\":3}]', 4),
(23, NULL, '20151120', '干活哈哈', 2, NULL, NULL, NULL, 1, 1, '[{\"id\":1,\"title\":\"回家姐姐\",\"choiceA\":\"不会后悔\",\"choiceB\":\"哈哈哈哈\",\"choiceC\":\"哈哈哈哈\",\"choiceD\":\"古古惑惑\",\"numA\":0,\"numB\":0,\"numC\":0,\"numD\":0},{\"id\":2,\"title\":\"干活好几年\",\"choiceA\":\"哈哈那你就\",\"choiceB\":\"哈哈哈哈\",\"choiceC\":\"VB宝宝\",\"choiceD\":\"哼哼唧唧\",\"numA\":0,\"numB\":0,\"numC\":0,\"numD\":0}]', 4);

-- --------------------------------------------------------

--
-- 表的结构 `t_vote_rec`
--

CREATE TABLE `t_vote_rec` (
  `id` int(11) NOT NULL,
  `student_id` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `stu_id` int(11) DEFAULT NULL,
  `vote_id` int(11) NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `t_course`
--
ALTER TABLE `t_course`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_course_noti`
--
ALTER TABLE `t_course_noti`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_course_select`
--
ALTER TABLE `t_course_select`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_discuss`
--
ALTER TABLE `t_discuss`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_discuss_comment`
--
ALTER TABLE `t_discuss_comment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_leave`
--
ALTER TABLE `t_leave`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_quiz`
--
ALTER TABLE `t_quiz`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_seat_record`
--
ALTER TABLE `t_seat_record`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_seat_select`
--
ALTER TABLE `t_seat_select`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_user`
--
ALTER TABLE `t_user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_vote`
--
ALTER TABLE `t_vote`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `t_vote_rec`
--
ALTER TABLE `t_vote_rec`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `t_course`
--
ALTER TABLE `t_course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- 使用表AUTO_INCREMENT `t_course_noti`
--
ALTER TABLE `t_course_noti`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- 使用表AUTO_INCREMENT `t_course_select`
--
ALTER TABLE `t_course_select`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- 使用表AUTO_INCREMENT `t_discuss`
--
ALTER TABLE `t_discuss`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- 使用表AUTO_INCREMENT `t_discuss_comment`
--
ALTER TABLE `t_discuss_comment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- 使用表AUTO_INCREMENT `t_leave`
--
ALTER TABLE `t_leave`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- 使用表AUTO_INCREMENT `t_quiz`
--
ALTER TABLE `t_quiz`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用表AUTO_INCREMENT `t_seat_record`
--
ALTER TABLE `t_seat_record`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- 使用表AUTO_INCREMENT `t_seat_select`
--
ALTER TABLE `t_seat_select`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- 使用表AUTO_INCREMENT `t_user`
--
ALTER TABLE `t_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- 使用表AUTO_INCREMENT `t_vote`
--
ALTER TABLE `t_vote`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- 使用表AUTO_INCREMENT `t_vote_rec`
--
ALTER TABLE `t_vote_rec`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
