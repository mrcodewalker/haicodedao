-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.27-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for codewalker
CREATE DATABASE IF NOT EXISTS `codewalker` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `codewalker`;

-- Dumping structure for table codewalker.student_failed
CREATE TABLE IF NOT EXISTS `student_failed` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_code` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1624 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table codewalker.student_failed: ~1,623 rows (approximately)
INSERT INTO `student_failed` (`id`, `student_code`) VALUES
	(1, 'AT160311'),
	(2, 'AT170610'),
	(3, 'AT160321'),
	(4, 'AT170527'),
	(5, 'AT170532'),
	(6, 'AT170542'),
	(7, 'AT160549'),
	(8, 'AT170149'),
	(9, 'AT170413'),
	(10, 'AT160315'),
	(11, 'AT160417'),
	(12, 'AT170718'),
	(13, 'AT150124'),
	(14, 'AT150125'),
	(15, 'AT150640'),
	(16, 'AT170637'),
	(17, 'AT170540'),
	(18, 'AT150347'),
	(19, 'AT160159'),
	(20, 'AT160715'),
	(21, 'AT170253'),
	(22, 'AT140151'),
	(23, 'AT170357'),
	(24, 'AT160218'),
	(25, 'AT170149'),
	(26, 'AT170417'),
	(27, 'AT170726'),
	(28, 'AT160237'),
	(29, 'AT170540'),
	(30, 'AT170546'),
	(31, 'AT170126'),
	(32, 'AT170635'),
	(33, 'AT160159'),
	(34, 'AT160311'),
	(35, 'AT170610'),
	(36, 'AT160321'),
	(37, 'AT170527'),
	(38, 'AT170532'),
	(39, 'AT170542'),
	(40, 'AT160549'),
	(41, 'AT170149'),
	(42, 'AT170413'),
	(43, 'AT160315'),
	(44, 'AT160417'),
	(45, 'AT170718'),
	(46, 'AT150124'),
	(47, 'AT150125'),
	(48, 'AT150640'),
	(49, 'AT170637'),
	(50, 'AT170540'),
	(51, 'AT150347'),
	(52, 'AT160159'),
	(53, 'AT160715'),
	(54, 'AT170253'),
	(55, 'AT140151'),
	(56, 'AT170357'),
	(57, 'AT160218'),
	(58, 'AT170149'),
	(59, 'AT170417'),
	(60, 'AT170726'),
	(61, 'AT160237'),
	(62, 'AT170540'),
	(63, 'AT170546'),
	(64, 'AT170126'),
	(65, 'AT170635'),
	(66, 'AT160159'),
	(67, 'AT170539'),
	(68, 'AT170417'),
	(69, 'AT170527'),
	(70, 'AT150139'),
	(71, 'AT170635'),
	(72, 'AT170149'),
	(73, 'AT150262'),
	(74, 'AT160222'),
	(75, 'AT160128'),
	(76, 'AT160638'),
	(77, 'AT141048'),
	(78, 'AT170250'),
	(79, 'AT150161'),
	(80, 'AT150262'),
	(81, 'AT170610'),
	(82, 'AT170527'),
	(83, 'AT170532'),
	(84, 'AT160549'),
	(85, 'AT170557'),
	(86, 'AT180601'),
	(87, 'AT180202'),
	(88, 'AT170104'),
	(89, 'AT180512'),
	(90, 'AT180411'),
	(91, 'AT150416'),
	(92, 'AT141022'),
	(93, 'AT180624'),
	(94, 'AT180626'),
	(95, 'AT180435'),
	(96, 'AT160337'),
	(97, 'AT170242'),
	(98, 'AT180150'),
	(99, 'AT160656'),
	(100, 'AT180650'),
	(101, 'AT180101'),
	(102, 'AT180302'),
	(103, 'AT180212'),
	(104, 'AT180527'),
	(105, 'AT180533'),
	(106, 'AT170540'),
	(107, 'AT180542'),
	(108, 'AT180544'),
	(109, 'AT180545'),
	(110, 'AT150262'),
	(111, 'AT180150'),
	(112, 'AT180549'),
	(113, 'AT180511'),
	(114, 'AT180619'),
	(115, 'AT180641'),
	(116, 'AT180244'),
	(117, 'AT160656'),
	(118, 'AT190315'),
	(119, 'AT190320'),
	(120, 'AT190429'),
	(121, 'AT190234'),
	(122, 'AT190459'),
	(123, 'AT160505'),
	(124, 'AT190220'),
	(125, 'AT170540'),
	(126, 'AT170253'),
	(127, 'AT190401'),
	(128, 'AT190319'),
	(129, 'AT190240'),
	(130, 'AT190505'),
	(131, 'AT190212'),
	(132, 'AT190426'),
	(133, 'AT190132'),
	(134, 'AT190151'),
	(135, 'AT190156'),
	(136, 'CT070313'),
	(137, 'CT040415'),
	(138, 'AT190212'),
	(139, 'AT190261'),
	(140, 'AT190420'),
	(141, 'CT070228'),
	(142, 'CT070241'),
	(143, 'CT070151'),
	(144, 'CT070248'),
	(145, 'CT070207'),
	(146, 'CT070220'),
	(147, 'AT150128'),
	(148, 'AT170133'),
	(149, 'AT190442'),
	(150, 'AT190443'),
	(151, 'AT180640'),
	(152, 'AT190156'),
	(153, 'CT070101'),
	(154, 'AT190315'),
	(155, 'CT070121'),
	(156, 'AT190520'),
	(157, 'AT190426'),
	(158, 'AT190304'),
	(159, 'AT190505'),
	(160, 'CT030212'),
	(161, 'AT160715'),
	(162, 'AT170121'),
	(163, 'AT190524'),
	(164, 'CT070225'),
	(165, 'AT150125'),
	(166, 'AT180225'),
	(167, 'AT170726'),
	(168, 'AT190332'),
	(169, 'AT190333'),
	(170, 'AT170240'),
	(171, 'AT170242'),
	(172, 'CT060337'),
	(173, 'AT190429'),
	(174, 'AT190234'),
	(175, 'CT070209'),
	(176, 'AT190319'),
	(177, 'CT070351'),
	(178, 'AT190151'),
	(179, 'CT070259'),
	(180, 'CT040125'),
	(181, 'CT030338'),
	(182, 'CT050142'),
	(183, 'CT040455'),
	(184, 'AT190505'),
	(185, 'AT170413'),
	(186, 'AT180620'),
	(187, 'AT170402'),
	(188, 'AT170401'),
	(189, 'CT030405'),
	(190, 'AT190110'),
	(191, 'CT070313'),
	(192, 'AT190116'),
	(193, 'AT190416'),
	(194, 'AT170509'),
	(195, 'AT190513'),
	(196, 'CT070120'),
	(197, 'AT190117'),
	(198, 'AT170117'),
	(199, 'AT190323'),
	(200, 'CT070222'),
	(201, 'DT060224'),
	(202, 'DT060128'),
	(203, 'AT170624'),
	(204, 'AT190424'),
	(205, 'DT060232'),
	(206, 'CT060123'),
	(207, 'AT190533'),
	(208, 'CT070138'),
	(209, 'DT060236'),
	(210, 'DT060139'),
	(211, 'AT130245'),
	(212, 'AT190442'),
	(213, 'AT190342'),
	(214, 'AT150146'),
	(215, 'CT070149'),
	(216, 'AT190445'),
	(217, 'CT070249'),
	(218, 'AT170550'),
	(219, 'AT160750'),
	(220, 'AT190354'),
	(221, 'AT190153'),
	(222, 'CT070160'),
	(223, 'AT140650'),
	(224, 'CT070161'),
	(225, 'CT070165'),
	(226, 'CT030457'),
	(227, 'AT200227'),
	(228, 'AT130129'),
	(229, 'DT070229'),
	(230, 'CT060421'),
	(231, 'DT030227'),
	(232, 'AT200249'),
	(233, 'AT200165'),
	(234, 'AT200405'),
	(235, 'AT200443'),
	(236, 'AT200214'),
	(237, 'AT200216'),
	(238, 'AT200223'),
	(239, 'AT200225'),
	(240, 'AT200227'),
	(241, 'AT200246'),
	(242, 'AT140650'),
	(243, 'AT200112'),
	(244, 'AT200118'),
	(245, 'AT200117'),
	(246, 'AT200135'),
	(247, 'AT200139'),
	(248, 'AT200153'),
	(249, 'AT200318'),
	(250, 'AT190524'),
	(251, 'DT070103'),
	(252, 'DT070115'),
	(253, 'DT070123'),
	(254, 'AT170130'),
	(255, 'DT070138'),
	(256, 'CT080201'),
	(257, 'CT080224'),
	(258, 'CT080244'),
	(259, 'CT080252'),
	(260, 'DT070206'),
	(261, 'DT070208'),
	(262, 'DT070227'),
	(263, 'DT070229'),
	(264, 'DT070241'),
	(265, 'DT070242'),
	(266, 'CT020442'),
	(267, 'DT070250'),
	(268, 'AT160503'),
	(269, 'DT060103'),
	(270, 'AT150506'),
	(271, 'DT060224'),
	(272, 'DT030225'),
	(273, 'AT170337'),
	(274, 'AT170550'),
	(275, 'AT200208'),
	(276, 'CT030310'),
	(277, 'DT060105'),
	(278, 'DT060122'),
	(279, 'AT200260'),
	(280, 'AT200167'),
	(281, 'CT080261'),
	(282, 'CT080108'),
	(283, 'CT080114'),
	(284, 'DT070207'),
	(285, 'AT200227'),
	(286, 'CT080126'),
	(287, 'AT200430'),
	(288, 'AT200337'),
	(289, 'AT160334'),
	(290, 'CT080149'),
	(291, 'AT200361'),
	(292, 'AT200260'),
	(293, 'DT050235'),
	(294, 'AT200205'),
	(295, 'DT040106'),
	(296, 'AT190506'),
	(297, 'AT200407'),
	(298, 'AT150506'),
	(299, 'DT030109'),
	(300, 'AT160611'),
	(301, 'CT060108'),
	(302, 'DT040118'),
	(303, 'AT141022'),
	(304, 'AT160230'),
	(305, 'DT070224'),
	(306, 'CT050126'),
	(307, 'DT040130'),
	(308, 'AT190332'),
	(309, 'DT040137'),
	(310, 'AT200242'),
	(311, 'DT060143'),
	(312, 'AT140341'),
	(313, 'AT150347'),
	(314, 'DT040148'),
	(315, 'CT050154'),
	(316, 'AT180601'),
	(317, 'CT080114'),
	(318, 'AT200216'),
	(319, 'DT040212'),
	(320, 'CT030310'),
	(321, 'DT050109'),
	(322, 'CT040413'),
	(323, 'DT050206'),
	(324, 'AT170115'),
	(325, 'CT050414'),
	(326, 'AT190320'),
	(327, 'DT050211'),
	(328, 'CT080219'),
	(329, 'AT141022'),
	(330, 'CT080224'),
	(331, 'AT160422'),
	(332, 'CT060217'),
	(333, 'AT200227'),
	(334, 'AT150228'),
	(335, 'AT150125'),
	(336, 'DT070224'),
	(337, 'DT050117'),
	(338, 'CT050428'),
	(339, 'CT060121'),
	(340, 'AT160337'),
	(341, 'CT080146'),
	(342, 'CT080244'),
	(343, 'AT180640'),
	(344, 'AT170546'),
	(345, 'DT050230'),
	(346, 'AT160651'),
	(347, 'DT040152'),
	(348, 'DT030141'),
	(349, 'CT040250'),
	(350, 'DT070248'),
	(351, 'AT140650'),
	(352, 'CT050404'),
	(353, 'CT050306'),
	(354, 'DT030107'),
	(355, 'CT020408'),
	(356, 'CT050412'),
	(357, 'CT050414'),
	(358, 'CT050415'),
	(359, 'CT050221'),
	(360, 'CT050423'),
	(361, 'CT050127'),
	(362, 'DT030125'),
	(363, 'CT050428'),
	(364, 'CT050132'),
	(365, 'CT050342'),
	(366, 'CT050343'),
	(367, 'CT050449'),
	(368, 'CT050355'),
	(369, 'CT050404'),
	(370, 'CT020408'),
	(371, 'CT040412'),
	(372, 'CT050423'),
	(373, 'CT040224'),
	(374, 'CT050127'),
	(375, 'CT040334'),
	(376, 'CT030437'),
	(377, 'CT040455'),
	(378, 'CT050355'),
	(379, 'CT020105'),
	(380, 'CT020408'),
	(381, 'CT040415'),
	(382, 'CT050311'),
	(383, 'CT030325'),
	(384, 'CT050423'),
	(385, 'CT050127'),
	(386, 'CT030128'),
	(387, 'CT030437'),
	(388, 'CT030452'),
	(389, 'CT040455'),
	(390, 'CT050355'),
	(391, 'CT040408'),
	(392, 'CT060329'),
	(393, 'CT050342'),
	(394, 'CT060344'),
	(395, 'CT050113'),
	(396, 'CT050310'),
	(397, 'CT060329'),
	(398, 'CT060340'),
	(399, 'CT060243'),
	(400, 'AT140650'),
	(401, 'CT060445'),
	(402, 'CT020408'),
	(403, 'CT060406'),
	(404, 'CT060423'),
	(405, 'CT060329'),
	(406, 'CT060432'),
	(407, 'CT060236'),
	(408, 'CT060442'),
	(409, 'CT030306'),
	(410, 'CT060423'),
	(411, 'DT040137'),
	(412, 'DT050225'),
	(413, 'CT060329'),
	(414, 'CT060340'),
	(415, 'DT040255'),
	(416, 'CT070107'),
	(417, 'CT070220'),
	(418, 'CT070143'),
	(419, 'CT070341'),
	(420, 'AT150347'),
	(421, 'AT130258'),
	(422, 'CT070257'),
	(423, 'CT040252'),
	(424, 'DT040103'),
	(425, 'DT040221'),
	(426, 'DT040146'),
	(427, 'DT040103'),
	(428, 'DT040221'),
	(429, 'DT040146'),
	(430, 'DT040103'),
	(431, 'DT040204'),
	(432, 'DT040124'),
	(433, 'DT040237'),
	(434, 'DT040238'),
	(435, 'DT040146'),
	(436, 'DT040247'),
	(437, 'DT050228'),
	(438, 'DT050131'),
	(439, 'DT050214'),
	(440, 'AT150629'),
	(441, 'DT040137'),
	(442, 'DT050207'),
	(443, 'DT050216'),
	(444, 'DT030225'),
	(445, 'DT050228'),
	(446, 'DT050134'),
	(447, 'DT050112'),
	(448, 'DT060107'),
	(449, 'DT060239'),
	(450, 'DT040251'),
	(451, 'AT160311'),
	(452, 'AT170546'),
	(453, 'AT170417'),
	(454, 'AT170542'),
	(455, 'AT170303'),
	(456, 'AT170618'),
	(457, 'AT170421'),
	(458, 'AT170502'),
	(459, 'AT170407'),
	(460, 'AT170421'),
	(461, 'AT170624'),
	(462, 'AT170235'),
	(463, 'AT170134'),
	(464, 'AT150640'),
	(465, 'AT170643'),
	(466, 'AT150251'),
	(467, 'AT170546'),
	(468, 'AT150161'),
	(469, 'AT170527'),
	(470, 'AT170149'),
	(471, 'AT160123'),
	(472, 'AT170624'),
	(473, 'AT140341'),
	(474, 'AT170546'),
	(475, 'AT170618'),
	(476, 'AT180320'),
	(477, 'AT180641'),
	(478, 'AT180338'),
	(479, 'AT140232'),
	(480, 'AT180150'),
	(481, 'AT141048'),
	(482, 'AT170509'),
	(483, 'AT180335'),
	(484, 'AT170540'),
	(485, 'AT150262'),
	(486, 'AT180650'),
	(487, 'AT180101'),
	(488, 'AT180206'),
	(489, 'AT180208'),
	(490, 'CT030212'),
	(491, 'AT180510'),
	(492, 'AT180311'),
	(493, 'AT160315'),
	(494, 'AT180320'),
	(495, 'AT170624'),
	(496, 'AT180520'),
	(497, 'CT060222'),
	(498, 'CT060124'),
	(499, 'CT060427'),
	(500, 'AT170134'),
	(501, 'CT060432'),
	(502, 'AT180641'),
	(503, 'AT170546'),
	(504, 'AT160347'),
	(505, 'CT060437'),
	(506, 'AT180144'),
	(507, 'CT020337'),
	(508, 'CT060435'),
	(509, 'CT060441'),
	(510, 'CT060243'),
	(511, 'AT180101'),
	(512, 'AT170704'),
	(513, 'AT180604'),
	(514, 'AT180208'),
	(515, 'AT180509'),
	(516, 'AT180411'),
	(517, 'AT160224'),
	(518, 'AT180320'),
	(519, 'AT180624'),
	(520, 'AT170624'),
	(521, 'AT180430'),
	(522, 'AT180138'),
	(523, 'AT130245'),
	(524, 'AT180641'),
	(525, 'AT160750'),
	(526, 'AT180447'),
	(527, 'AT190505'),
	(528, 'AT190521'),
	(529, 'AT180320'),
	(530, 'AT190426'),
	(531, 'AT190432'),
	(532, 'AT190437'),
	(533, 'CT070105'),
	(534, 'CT020408'),
	(535, 'AT190414'),
	(536, 'CT070209'),
	(537, 'AT190217'),
	(538, 'AT180219'),
	(539, 'CT070320'),
	(540, 'AT190426'),
	(541, 'CT070133'),
	(542, 'AT190331'),
	(543, 'CT070136'),
	(544, 'AT190130'),
	(545, 'CT070341'),
	(546, 'AT190442'),
	(547, 'AT180640'),
	(548, 'AT190345'),
	(549, 'CT060437'),
	(550, 'AT190149'),
	(551, 'CT070209'),
	(552, 'DT060107'),
	(553, 'AT180110'),
	(554, 'CT070133'),
	(555, 'AT180427'),
	(556, 'AT190429'),
	(557, 'AT150431'),
	(558, 'DT020228'),
	(559, 'AT190234'),
	(560, 'AT190335'),
	(561, 'AT190537'),
	(562, 'AT190442'),
	(563, 'CT060441'),
	(564, 'DT050134'),
	(565, 'AT190102'),
	(566, 'AT190401'),
	(567, 'AT190409'),
	(568, 'CT070314'),
	(569, 'CT070209'),
	(570, 'DT060107'),
	(571, 'CT060305'),
	(572, 'DT010105'),
	(573, 'CT040415'),
	(574, 'AT190413'),
	(575, 'AT190417'),
	(576, 'AT190117'),
	(577, 'CT070220'),
	(578, 'DT040123'),
	(579, 'DT060122'),
	(580, 'CT020124'),
	(581, 'CT070228'),
	(582, 'AT190429'),
	(583, 'AT190331'),
	(584, 'DT030225'),
	(585, 'DT040232'),
	(586, 'DT060233'),
	(587, 'DT040137'),
	(588, 'AT190234'),
	(589, 'CT030241'),
	(590, 'AT190537'),
	(591, 'DT060140'),
	(592, 'CT070340'),
	(593, 'CT070241'),
	(594, 'DT060239'),
	(595, 'CT070341'),
	(596, 'CT070156'),
	(597, 'DT050134'),
	(598, 'CT040252'),
	(599, 'AT190156'),
	(600, 'DT060250'),
	(601, 'CT040455'),
	(602, 'AT200405'),
	(603, 'CT080224'),
	(604, 'DT070123'),
	(605, 'DT070229'),
	(606, 'AT190135'),
	(607, 'CT070341'),
	(608, 'CT060133'),
	(609, 'DT050228'),
	(610, 'AT180641'),
	(611, 'CT050449'),
	(612, 'AT200405'),
	(613, 'AT200116'),
	(614, 'AT200168'),
	(615, 'AT180105'),
	(616, 'AT200405'),
	(617, 'AT180407'),
	(618, 'CT020408'),
	(619, 'AT200217'),
	(620, 'CT080224'),
	(621, 'AT200326'),
	(622, 'AT180438'),
	(623, 'AT170251'),
	(624, 'DT070248'),
	(625, 'AT200405'),
	(626, 'AT200217'),
	(627, 'AT200121'),
	(628, 'CT080224'),
	(629, 'AT200426'),
	(630, 'CT070133'),
	(631, 'DT040244'),
	(632, 'AT190246'),
	(633, 'CT080253'),
	(634, 'CT040257'),
	(635, 'CT070208'),
	(636, 'DT060122'),
	(637, 'CT070136'),
	(638, 'CT070135'),
	(639, 'DT060138'),
	(640, 'DT060140'),
	(641, 'DT060243'),
	(642, 'CT070229'),
	(643, 'CT040131'),
	(644, 'DT060239'),
	(645, 'DT060233'),
	(646, 'CT070206'),
	(647, 'CT040216'),
	(648, 'DT060118'),
	(649, 'DT060145'),
	(650, 'DT060103'),
	(651, 'DT060219'),
	(652, 'CT070118'),
	(653, 'CT070141'),
	(654, 'CT070241'),
	(655, 'DT060250'),
	(656, 'CT070101'),
	(657, 'CT030208'),
	(658, 'CT070112'),
	(659, 'CT040415'),
	(660, 'CT070322'),
	(661, 'CT070334'),
	(662, 'CT040412'),
	(663, 'CT040415'),
	(664, 'CT050127'),
	(665, 'CT050355'),
	(666, 'CT050308'),
	(667, 'CT040415'),
	(668, 'CT050214'),
	(669, 'CT050220'),
	(670, 'CT050120'),
	(671, 'CT050423'),
	(672, 'CT050449'),
	(673, 'CT050351'),
	(674, 'CT050355'),
	(675, 'CT040355'),
	(676, 'CT050423'),
	(677, 'CT050127'),
	(678, 'CT050441'),
	(679, 'CT040439'),
	(680, 'CT040455'),
	(681, 'CT050355'),
	(682, 'CT060201'),
	(683, 'CT030306'),
	(684, 'AT150416'),
	(685, 'CT050120'),
	(686, 'CT040331'),
	(687, 'CT060432'),
	(688, 'CT060437'),
	(689, 'CT060305'),
	(690, 'CT050214'),
	(691, 'CT060432'),
	(692, 'CT060338'),
	(693, 'CT050349'),
	(694, 'CT040408'),
	(695, 'CT060423'),
	(696, 'CT060334'),
	(697, 'CT030158'),
	(698, 'DT040103'),
	(699, 'DT040102'),
	(700, 'DT040221'),
	(701, 'DT040146'),
	(702, 'DT040103'),
	(703, 'DT040146'),
	(704, 'DT040103'),
	(705, 'DT040146'),
	(706, 'DT020148'),
	(707, 'DT040103'),
	(708, 'DT040146'),
	(709, 'DT050101'),
	(710, 'DT010101'),
	(711, 'DT050112'),
	(712, 'DT050134'),
	(713, 'DT060140'),
	(714, 'AT160311'),
	(715, 'AT170610'),
	(716, 'AT160321'),
	(717, 'AT170527'),
	(718, 'AT170532'),
	(719, 'AT170542'),
	(720, 'AT160549'),
	(721, 'AT170149'),
	(722, 'AT170413'),
	(723, 'AT160315'),
	(724, 'AT160417'),
	(725, 'AT170718'),
	(726, 'AT150124'),
	(727, 'AT150125'),
	(728, 'AT150640'),
	(729, 'AT170637'),
	(730, 'AT170540'),
	(731, 'AT150347'),
	(732, 'AT160159'),
	(733, 'AT160715'),
	(734, 'AT170253'),
	(735, 'AT140151'),
	(736, 'AT170357'),
	(737, 'AT160218'),
	(738, 'AT170149'),
	(739, 'AT170417'),
	(740, 'AT170726'),
	(741, 'AT160237'),
	(742, 'AT170540'),
	(743, 'AT170546'),
	(744, 'AT170126'),
	(745, 'AT170635'),
	(746, 'AT160159'),
	(747, 'AT170539'),
	(748, 'AT170417'),
	(749, 'AT170527'),
	(750, 'AT150139'),
	(751, 'AT170635'),
	(752, 'AT170149'),
	(753, 'AT150262'),
	(754, 'AT160222'),
	(755, 'AT160128'),
	(756, 'AT160638'),
	(757, 'AT141048'),
	(758, 'AT170250'),
	(759, 'AT150161'),
	(760, 'AT150262'),
	(761, 'AT170610'),
	(762, 'AT170527'),
	(763, 'AT170532'),
	(764, 'AT160549'),
	(765, 'AT170557'),
	(766, 'AT180601'),
	(767, 'AT180202'),
	(768, 'AT170104'),
	(769, 'AT180512'),
	(770, 'AT180411'),
	(771, 'AT150416'),
	(772, 'AT141022'),
	(773, 'AT180624'),
	(774, 'AT180626'),
	(775, 'AT180435'),
	(776, 'AT160337'),
	(777, 'AT170242'),
	(778, 'AT180150'),
	(779, 'AT160656'),
	(780, 'AT180650'),
	(781, 'AT180101'),
	(782, 'AT180302'),
	(783, 'AT180212'),
	(784, 'AT180527'),
	(785, 'AT180533'),
	(786, 'AT170540'),
	(787, 'AT180542'),
	(788, 'AT180544'),
	(789, 'AT180545'),
	(790, 'AT150262'),
	(791, 'AT180150'),
	(792, 'AT180549'),
	(793, 'AT180511'),
	(794, 'AT180619'),
	(795, 'AT180641'),
	(796, 'AT180244'),
	(797, 'AT160656'),
	(798, 'AT190315'),
	(799, 'AT190320'),
	(800, 'AT190429'),
	(801, 'AT190234'),
	(802, 'AT190459'),
	(803, 'AT160505'),
	(804, 'AT190220'),
	(805, 'AT170540'),
	(806, 'AT170253'),
	(807, 'AT190401'),
	(808, 'AT190319'),
	(809, 'AT190240'),
	(810, 'AT190505'),
	(811, 'AT190212'),
	(812, 'AT190426'),
	(813, 'AT190132'),
	(814, 'AT190151'),
	(815, 'AT190156'),
	(816, 'CT070313'),
	(817, 'CT040415'),
	(818, 'AT190212'),
	(819, 'AT190261'),
	(820, 'AT190420'),
	(821, 'CT070228'),
	(822, 'CT070241'),
	(823, 'CT070151'),
	(824, 'CT070248'),
	(825, 'CT070207'),
	(826, 'CT070220'),
	(827, 'AT150128'),
	(828, 'AT170133'),
	(829, 'AT190442'),
	(830, 'AT190443'),
	(831, 'AT180640'),
	(832, 'AT190156'),
	(833, 'CT070101'),
	(834, 'AT190315'),
	(835, 'CT070121'),
	(836, 'AT190520'),
	(837, 'AT190426'),
	(838, 'AT190304'),
	(839, 'AT190505'),
	(840, 'CT030212'),
	(841, 'AT160715'),
	(842, 'AT170121'),
	(843, 'AT190524'),
	(844, 'CT070225'),
	(845, 'AT150125'),
	(846, 'AT180225'),
	(847, 'AT170726'),
	(848, 'AT190332'),
	(849, 'AT190333'),
	(850, 'AT170240'),
	(851, 'AT170242'),
	(852, 'CT060337'),
	(853, 'AT190429'),
	(854, 'AT190234'),
	(855, 'CT070209'),
	(856, 'AT190319'),
	(857, 'CT070351'),
	(858, 'AT190151'),
	(859, 'CT070259'),
	(860, 'CT040125'),
	(861, 'CT030338'),
	(862, 'CT050142'),
	(863, 'CT040455'),
	(864, 'AT190505'),
	(865, 'AT170413'),
	(866, 'AT180620'),
	(867, 'AT170402'),
	(868, 'AT170401'),
	(869, 'CT030405'),
	(870, 'AT190110'),
	(871, 'CT070313'),
	(872, 'AT190116'),
	(873, 'AT190416'),
	(874, 'AT170509'),
	(875, 'AT190513'),
	(876, 'CT070120'),
	(877, 'AT190117'),
	(878, 'AT170117'),
	(879, 'AT190323'),
	(880, 'CT070222'),
	(881, 'DT060224'),
	(882, 'DT060128'),
	(883, 'AT170624'),
	(884, 'AT190424'),
	(885, 'DT060232'),
	(886, 'CT060123'),
	(887, 'AT190533'),
	(888, 'CT070138'),
	(889, 'DT060236'),
	(890, 'DT060139'),
	(891, 'AT130245'),
	(892, 'AT190442'),
	(893, 'AT190342'),
	(894, 'AT150146'),
	(895, 'CT070149'),
	(896, 'AT190445'),
	(897, 'CT070249'),
	(898, 'AT170550'),
	(899, 'AT160750'),
	(900, 'AT190354'),
	(901, 'AT190153'),
	(902, 'CT070160'),
	(903, 'AT140650'),
	(904, 'CT070161'),
	(905, 'CT070165'),
	(906, 'CT030457'),
	(907, 'AT200227'),
	(908, 'AT130129'),
	(909, 'DT070229'),
	(910, 'CT060421'),
	(911, 'DT030227'),
	(912, 'AT200249'),
	(913, 'AT200165'),
	(914, 'AT200405'),
	(915, 'AT200443'),
	(916, 'AT200214'),
	(917, 'AT200216'),
	(918, 'AT200223'),
	(919, 'AT200225'),
	(920, 'AT200227'),
	(921, 'AT200246'),
	(922, 'AT140650'),
	(923, 'AT200112'),
	(924, 'AT200118'),
	(925, 'AT200117'),
	(926, 'AT200135'),
	(927, 'AT200139'),
	(928, 'AT200153'),
	(929, 'AT200318'),
	(930, 'AT190524'),
	(931, 'DT070103'),
	(932, 'DT070115'),
	(933, 'DT070123'),
	(934, 'AT170130'),
	(935, 'DT070138'),
	(936, 'CT080201'),
	(937, 'CT080224'),
	(938, 'CT080244'),
	(939, 'CT080252'),
	(940, 'DT070206'),
	(941, 'DT070208'),
	(942, 'DT070227'),
	(943, 'DT070229'),
	(944, 'DT070241'),
	(945, 'DT070242'),
	(946, 'CT020442'),
	(947, 'DT070250'),
	(948, 'AT160503'),
	(949, 'DT060103'),
	(950, 'AT150506'),
	(951, 'DT060224'),
	(952, 'DT030225'),
	(953, 'AT170337'),
	(954, 'AT170550'),
	(955, 'AT200208'),
	(956, 'CT030310'),
	(957, 'DT060105'),
	(958, 'DT060122'),
	(959, 'AT200260'),
	(960, 'AT200167'),
	(961, 'CT080261'),
	(962, 'CT080108'),
	(963, 'CT080114'),
	(964, 'DT070207'),
	(965, 'AT200227'),
	(966, 'CT080126'),
	(967, 'AT200430'),
	(968, 'AT200337'),
	(969, 'AT160334'),
	(970, 'CT080149'),
	(971, 'AT200361'),
	(972, 'AT200260'),
	(973, 'DT050235'),
	(974, 'AT200205'),
	(975, 'DT040106'),
	(976, 'AT190506'),
	(977, 'AT200407'),
	(978, 'AT150506'),
	(979, 'DT030109'),
	(980, 'AT160611'),
	(981, 'CT060108'),
	(982, 'DT040118'),
	(983, 'AT141022'),
	(984, 'AT160230'),
	(985, 'DT070224'),
	(986, 'CT050126'),
	(987, 'DT040130'),
	(988, 'AT190332'),
	(989, 'DT040137'),
	(990, 'AT200242'),
	(991, 'DT060143'),
	(992, 'AT140341'),
	(993, 'AT150347'),
	(994, 'DT040148'),
	(995, 'CT050154'),
	(996, 'AT180601'),
	(997, 'CT080114'),
	(998, 'AT200216'),
	(999, 'DT040212'),
	(1000, 'CT030310'),
	(1001, 'DT050109'),
	(1002, 'CT040413'),
	(1003, 'DT050206'),
	(1004, 'AT170115'),
	(1005, 'CT050414'),
	(1006, 'AT190320'),
	(1007, 'DT050211'),
	(1008, 'CT080219'),
	(1009, 'AT141022'),
	(1010, 'CT080224'),
	(1011, 'AT160422'),
	(1012, 'CT060217'),
	(1013, 'AT200227'),
	(1014, 'AT150228'),
	(1015, 'AT150125'),
	(1016, 'DT070224'),
	(1017, 'DT050117'),
	(1018, 'CT050428'),
	(1019, 'CT060121'),
	(1020, 'AT160337'),
	(1021, 'CT080146'),
	(1022, 'CT080244'),
	(1023, 'AT180640'),
	(1024, 'AT170546'),
	(1025, 'DT050230'),
	(1026, 'AT160651'),
	(1027, 'DT040152'),
	(1028, 'DT030141'),
	(1029, 'CT040250'),
	(1030, 'DT070248'),
	(1031, 'AT140650'),
	(1032, 'CT050404'),
	(1033, 'CT050306'),
	(1034, 'DT030107'),
	(1035, 'CT020408'),
	(1036, 'CT050412'),
	(1037, 'CT050414'),
	(1038, 'CT050415'),
	(1039, 'CT050221'),
	(1040, 'CT050423'),
	(1041, 'CT050127'),
	(1042, 'DT030125'),
	(1043, 'CT050428'),
	(1044, 'CT050132'),
	(1045, 'CT050342'),
	(1046, 'CT050343'),
	(1047, 'CT050449'),
	(1048, 'CT050355'),
	(1049, 'CT050404'),
	(1050, 'CT020408'),
	(1051, 'CT040412'),
	(1052, 'CT050423'),
	(1053, 'CT040224'),
	(1054, 'CT050127'),
	(1055, 'CT040334'),
	(1056, 'CT030437'),
	(1057, 'CT040455'),
	(1058, 'CT050355'),
	(1059, 'CT020105'),
	(1060, 'CT020408'),
	(1061, 'CT040415'),
	(1062, 'CT050311'),
	(1063, 'CT030325'),
	(1064, 'CT050423'),
	(1065, 'CT050127'),
	(1066, 'CT030128'),
	(1067, 'CT030437'),
	(1068, 'CT030452'),
	(1069, 'CT040455'),
	(1070, 'CT050355'),
	(1071, 'CT040408'),
	(1072, 'CT060329'),
	(1073, 'CT050342'),
	(1074, 'CT060344'),
	(1075, 'CT050113'),
	(1076, 'CT050310'),
	(1077, 'CT060329'),
	(1078, 'CT060340'),
	(1079, 'CT060243'),
	(1080, 'AT140650'),
	(1081, 'CT060445'),
	(1082, 'CT020408'),
	(1083, 'CT060406'),
	(1084, 'CT060423'),
	(1085, 'CT060329'),
	(1086, 'CT060432'),
	(1087, 'CT060236'),
	(1088, 'CT060442'),
	(1089, 'CT030306'),
	(1090, 'CT060423'),
	(1091, 'DT040137'),
	(1092, 'DT050225'),
	(1093, 'CT060329'),
	(1094, 'CT060340'),
	(1095, 'DT040255'),
	(1096, 'CT070107'),
	(1097, 'CT070220'),
	(1098, 'CT070143'),
	(1099, 'CT070341'),
	(1100, 'AT150347'),
	(1101, 'AT130258'),
	(1102, 'CT070257'),
	(1103, 'CT040252'),
	(1104, 'DT040103'),
	(1105, 'DT040221'),
	(1106, 'DT040146'),
	(1107, 'DT040103'),
	(1108, 'DT040221'),
	(1109, 'DT040146'),
	(1110, 'DT040103'),
	(1111, 'DT040204'),
	(1112, 'DT040124'),
	(1113, 'DT040237'),
	(1114, 'DT040238'),
	(1115, 'DT040146'),
	(1116, 'DT040247'),
	(1117, 'DT050228'),
	(1118, 'DT050131'),
	(1119, 'DT050214'),
	(1120, 'AT150629'),
	(1121, 'DT040137'),
	(1122, 'DT050207'),
	(1123, 'DT050216'),
	(1124, 'DT030225'),
	(1125, 'DT050228'),
	(1126, 'DT050134'),
	(1127, 'DT050112'),
	(1128, 'DT060107'),
	(1129, 'DT060239'),
	(1130, 'DT040251'),
	(1131, 'AT160311'),
	(1132, 'AT160321'),
	(1133, 'AT170527'),
	(1134, 'AT170542'),
	(1135, 'AT160549'),
	(1136, 'AT170149'),
	(1137, 'AT150402'),
	(1138, 'AT160503'),
	(1139, 'AT160222'),
	(1140, 'AT160128'),
	(1141, 'AT170417'),
	(1142, 'AT160715'),
	(1143, 'AT170521'),
	(1144, 'AT170421'),
	(1145, 'AT170225'),
	(1146, 'AT170229'),
	(1147, 'AT170726'),
	(1148, 'AT150431'),
	(1149, 'AT170635'),
	(1150, 'AT170641'),
	(1151, 'AT170539'),
	(1152, 'AT150246'),
	(1153, 'AT150262'),
	(1154, 'AT160756'),
	(1155, 'AT160159'),
	(1156, 'AT170357'),
	(1157, 'AT160715'),
	(1158, 'AT170253'),
	(1159, 'AT140151'),
	(1160, 'AT170357'),
	(1161, 'AT170149'),
	(1162, 'AT170417'),
	(1163, 'AT170726'),
	(1164, 'AT160237'),
	(1165, 'AT170540'),
	(1166, 'AT170546'),
	(1167, 'AT170635'),
	(1168, 'AT160159'),
	(1169, 'AT170539'),
	(1170, 'AT170417'),
	(1171, 'AT170527'),
	(1172, 'AT150139'),
	(1173, 'AT170635'),
	(1174, 'AT170149'),
	(1175, 'AT150262'),
	(1176, 'AT170303'),
	(1177, 'AT160503'),
	(1178, 'AT170215'),
	(1179, 'AT160311'),
	(1180, 'AT160222'),
	(1181, 'AT160128'),
	(1182, 'AT170417'),
	(1183, 'AT160715'),
	(1184, 'AT170726'),
	(1185, 'AT170133'),
	(1186, 'AT160536'),
	(1187, 'AT170635'),
	(1188, 'AT170540'),
	(1189, 'AT170539'),
	(1190, 'AT150251'),
	(1191, 'AT170546'),
	(1192, 'AT170253'),
	(1193, 'AT150262'),
	(1194, 'AT170357'),
	(1195, 'AT160207'),
	(1196, 'AT170149'),
	(1197, 'AT150410'),
	(1198, 'AT160311'),
	(1199, 'AT170149'),
	(1200, 'AT130358'),
	(1201, 'AT180101'),
	(1202, 'AT170618'),
	(1203, 'AT180523'),
	(1204, 'AT170726'),
	(1205, 'AT180338'),
	(1206, 'AT180641'),
	(1207, 'AT170149'),
	(1208, 'AT180544'),
	(1209, 'AT180101'),
	(1210, 'AT180527'),
	(1211, 'AT180533'),
	(1212, 'AT170540'),
	(1213, 'AT180542'),
	(1214, 'AT180544'),
	(1215, 'AT150262'),
	(1216, 'AT180150'),
	(1217, 'AT180549'),
	(1218, 'AT180101'),
	(1219, 'AT180523'),
	(1220, 'AT180338'),
	(1221, 'AT170540'),
	(1222, 'AT170149'),
	(1223, 'AT180544'),
	(1224, 'AT160503'),
	(1225, 'AT170417'),
	(1226, 'AT180523'),
	(1227, 'AT180338'),
	(1228, 'AT180641'),
	(1229, 'AT180544'),
	(1230, 'AT190315'),
	(1231, 'AT190320'),
	(1232, 'AT190429'),
	(1233, 'AT190234'),
	(1234, 'AT190459'),
	(1235, 'AT190401'),
	(1236, 'AT190319'),
	(1237, 'AT190240'),
	(1238, 'AT190132'),
	(1239, 'AT190151'),
	(1240, 'AT190156'),
	(1241, 'CT040415'),
	(1242, 'AT150128'),
	(1243, 'AT190426'),
	(1244, 'AT190505'),
	(1245, 'AT160715'),
	(1246, 'AT190524'),
	(1247, 'AT170726'),
	(1248, 'CT060337'),
	(1249, 'AT190429'),
	(1250, 'AT190234'),
	(1251, 'CT040125'),
	(1252, 'CT030338'),
	(1253, 'CT050142'),
	(1254, 'AT170403'),
	(1255, 'CT060305'),
	(1256, 'CT040415'),
	(1257, 'DT060224'),
	(1258, 'CT060423'),
	(1259, 'AT190234'),
	(1260, 'CT070152'),
	(1261, 'DT060250'),
	(1262, 'AT170403'),
	(1263, 'AT190304'),
	(1264, 'AT190102'),
	(1265, 'DT040102'),
	(1266, 'AT170303'),
	(1267, 'AT190502'),
	(1268, 'AT190105'),
	(1269, 'AT190506'),
	(1270, 'AT190508'),
	(1271, 'AT160207'),
	(1272, 'AT170607'),
	(1273, 'CT070206'),
	(1274, 'AT190408'),
	(1275, 'AT170406'),
	(1276, 'CT030408'),
	(1277, 'DT030105'),
	(1278, 'CT070311'),
	(1279, 'CT070214'),
	(1280, 'CT050314'),
	(1281, 'CT070211'),
	(1282, 'DT040117'),
	(1283, 'AT190209'),
	(1284, 'CT070210'),
	(1285, 'AT160709'),
	(1286, 'DT010105'),
	(1287, 'CT050311'),
	(1288, 'AT190212'),
	(1289, 'AT190261'),
	(1290, 'AT190417'),
	(1291, 'AT190317'),
	(1292, 'AT190217'),
	(1293, 'AT190519'),
	(1294, 'AT190520'),
	(1295, 'CT050117'),
	(1296, 'AT190219'),
	(1297, 'CT030420'),
	(1298, 'CT040220'),
	(1299, 'AT141022'),
	(1300, 'CT070220'),
	(1301, 'CT030325'),
	(1302, 'AT160522'),
	(1303, 'AT190222'),
	(1304, 'CT070223'),
	(1305, 'CT070224'),
	(1306, 'AT180620'),
	(1307, 'AT190126'),
	(1308, 'AT190527'),
	(1309, 'AT180523'),
	(1310, 'AT190426'),
	(1311, 'AT150125'),
	(1312, 'AT170526'),
	(1313, 'CT070228'),
	(1314, 'AT190228'),
	(1315, 'CT070135'),
	(1316, 'AT170532'),
	(1317, 'AT190433'),
	(1318, 'AT190333'),
	(1319, 'AT160147'),
	(1320, 'AT190234'),
	(1321, 'AT190435'),
	(1322, 'AT190236'),
	(1323, 'AT190138'),
	(1324, 'AT190139'),
	(1325, 'AT190540'),
	(1326, 'DT030234'),
	(1327, 'AT190541'),
	(1328, 'AT190141'),
	(1329, 'CT070241'),
	(1330, 'AT190242'),
	(1331, 'DT060239'),
	(1332, 'AT190241'),
	(1333, 'AT190543'),
	(1334, 'DT030235'),
	(1335, 'CT070145'),
	(1336, 'AT190443'),
	(1337, 'DT040243'),
	(1338, 'CT070246'),
	(1339, 'AT170442'),
	(1340, 'CT070152'),
	(1341, 'CT070251'),
	(1342, 'AT170547'),
	(1343, 'CT070155'),
	(1344, 'DT040249'),
	(1345, 'CT060340'),
	(1346, 'CT070254'),
	(1347, 'CT070255'),
	(1348, 'AT190352'),
	(1349, 'AT140148'),
	(1350, 'DT030141'),
	(1351, 'AT170350'),
	(1352, 'AT170253'),
	(1353, 'CT070257'),
	(1354, 'AT190252'),
	(1355, 'AT170555'),
	(1356, 'AT190356'),
	(1357, 'DT060149'),
	(1358, 'AT160556'),
	(1359, 'AT190154'),
	(1360, 'AT190157'),
	(1361, 'CT070262'),
	(1362, 'AT190458'),
	(1363, 'AT190559'),
	(1364, 'CT040455'),
	(1365, 'AT200405'),
	(1366, 'AT200112'),
	(1367, 'DT030207'),
	(1368, 'AT200216'),
	(1369, 'AT200118'),
	(1370, 'DT070115'),
	(1371, 'AT200228'),
	(1372, 'AT200135'),
	(1373, 'CT060423'),
	(1374, 'DT060239'),
	(1375, 'CT060337'),
	(1376, 'DT070250'),
	(1377, 'AT200405'),
	(1378, 'AT200443'),
	(1379, 'AT200214'),
	(1380, 'AT200216'),
	(1381, 'AT200223'),
	(1382, 'AT200225'),
	(1383, 'AT140650'),
	(1384, 'AT200112'),
	(1385, 'AT200118'),
	(1386, 'AT200135'),
	(1387, 'AT200318'),
	(1388, 'AT190524'),
	(1389, 'DT070115'),
	(1390, 'DT070123'),
	(1391, 'AT170130'),
	(1392, 'CT080201'),
	(1393, 'CT080224'),
	(1394, 'CT080244'),
	(1395, 'CT080252'),
	(1396, 'DT070208'),
	(1397, 'DT070227'),
	(1398, 'DT070229'),
	(1399, 'DT070241'),
	(1400, 'DT070242'),
	(1401, 'CT020442'),
	(1402, 'DT070250'),
	(1403, 'AT160503'),
	(1404, 'DT060103'),
	(1405, 'AT150506'),
	(1406, 'DT060224'),
	(1407, 'DT030225'),
	(1408, 'CT030310'),
	(1409, 'DT060105'),
	(1410, 'DT060122'),
	(1411, 'AT200260'),
	(1412, 'AT200405'),
	(1413, 'AT200112'),
	(1414, 'CT020408'),
	(1415, 'AT200216'),
	(1416, 'AT200118'),
	(1417, 'DT070115'),
	(1418, 'AT200417'),
	(1419, 'AT200121'),
	(1420, 'AT200421'),
	(1421, 'DT070221'),
	(1422, 'AT200228'),
	(1423, 'AT200135'),
	(1424, 'DT070229'),
	(1425, 'AT190131'),
	(1426, 'AT200244'),
	(1427, 'AT200453'),
	(1428, 'DT070250'),
	(1429, 'AT200168'),
	(1430, 'DT070103'),
	(1431, 'AT200405'),
	(1432, 'AT200112'),
	(1433, 'DT030207'),
	(1434, 'AT200216'),
	(1435, 'AT200118'),
	(1436, 'DT070115'),
	(1437, 'AT160611'),
	(1438, 'AT170618'),
	(1439, 'AT141022'),
	(1440, 'CT060118'),
	(1441, 'DT060224'),
	(1442, 'AT200135'),
	(1443, 'CT070136'),
	(1444, 'DT070136'),
	(1445, 'DT070140'),
	(1446, 'AT160745'),
	(1447, 'AT150262'),
	(1448, 'DT070250'),
	(1449, 'DT070103'),
	(1450, 'AT200405'),
	(1451, 'AT150506'),
	(1452, 'AT200112'),
	(1453, 'CT070311'),
	(1454, 'AT200118'),
	(1455, 'AT160509'),
	(1456, 'AT200416'),
	(1457, 'DT070115'),
	(1458, 'CT060107'),
	(1459, 'CT040415'),
	(1460, 'AT180511'),
	(1461, 'CT070220'),
	(1462, 'DT070123'),
	(1463, 'DT070124'),
	(1464, 'DT060224'),
	(1465, 'AT200426'),
	(1466, 'DT070125'),
	(1467, 'DT070130'),
	(1468, 'AT200135'),
	(1469, 'CT070136'),
	(1470, 'CT060423'),
	(1471, 'AT200437'),
	(1472, 'AT160533'),
	(1473, 'CT040334'),
	(1474, 'CT060427'),
	(1475, 'DT060237'),
	(1476, 'AT200445'),
	(1477, 'CT040338'),
	(1478, 'DT070136'),
	(1479, 'DT070138'),
	(1480, 'DT070140'),
	(1481, 'CT060337'),
	(1482, 'DT070145'),
	(1483, 'CT020442'),
	(1484, 'AT160159'),
	(1485, 'DT070250'),
	(1486, 'CT050404'),
	(1487, 'CT050306'),
	(1488, 'DT030107'),
	(1489, 'CT020408'),
	(1490, 'CT050412'),
	(1491, 'CT050415'),
	(1492, 'CT050221'),
	(1493, 'CT050423'),
	(1494, 'CT050127'),
	(1495, 'DT030125'),
	(1496, 'CT050132'),
	(1497, 'CT050342'),
	(1498, 'CT050343'),
	(1499, 'CT050449'),
	(1500, 'CT050355'),
	(1501, 'CT020408'),
	(1502, 'CT040412'),
	(1503, 'CT050423'),
	(1504, 'CT050127'),
	(1505, 'CT050355'),
	(1506, 'CT020105'),
	(1507, 'CT020408'),
	(1508, 'CT050412'),
	(1509, 'CT040415'),
	(1510, 'CT050311'),
	(1511, 'CT030325'),
	(1512, 'CT050423'),
	(1513, 'CT050127'),
	(1514, 'CT030128'),
	(1515, 'CT030437'),
	(1516, 'CT030452'),
	(1517, 'CT050355'),
	(1518, 'CT060201'),
	(1519, 'CT020105'),
	(1520, 'CT050404'),
	(1521, 'CT060107'),
	(1522, 'CT060305'),
	(1523, 'CT060214'),
	(1524, 'CT050220'),
	(1525, 'CT060118'),
	(1526, 'CT060321'),
	(1527, 'CT060423'),
	(1528, 'CT060124'),
	(1529, 'CT040331'),
	(1530, 'CT060427'),
	(1531, 'CT030241'),
	(1532, 'CT060432'),
	(1533, 'CT060337'),
	(1534, 'CT060340'),
	(1535, 'CT020442'),
	(1536, 'CT060201'),
	(1537, 'AT130303'),
	(1538, 'CT060302'),
	(1539, 'CT060104'),
	(1540, 'CT040115'),
	(1541, 'CT060107'),
	(1542, 'CT040413'),
	(1543, 'CT060305'),
	(1544, 'AT150315'),
	(1545, 'CT060406'),
	(1546, 'CT060214'),
	(1547, 'CT060415'),
	(1548, 'CT060118'),
	(1549, 'CT060321'),
	(1550, 'CT060222'),
	(1551, 'CT060423'),
	(1552, 'CT060121'),
	(1553, 'CT060422'),
	(1554, 'CT060124'),
	(1555, 'CT020330'),
	(1556, 'CT060432'),
	(1557, 'CT040341'),
	(1558, 'AT150347'),
	(1559, 'CT060437'),
	(1560, 'CT060240'),
	(1561, 'CT060337'),
	(1562, 'CT050247'),
	(1563, 'AT140151'),
	(1564, 'CT040149'),
	(1565, 'CT040153'),
	(1566, 'CT020442'),
	(1567, 'CT060244'),
	(1568, 'CT060246'),
	(1569, 'CT060305'),
	(1570, 'CT040415'),
	(1571, 'CT060118'),
	(1572, 'CT060321'),
	(1573, 'CT060423'),
	(1574, 'CT060437'),
	(1575, 'CT060337'),
	(1576, 'CT050247'),
	(1577, 'CT060340'),
	(1578, 'CT040252'),
	(1579, 'AT140849'),
	(1580, 'CT060201'),
	(1581, 'DT030202'),
	(1582, 'CT040412'),
	(1583, 'CT060305'),
	(1584, 'CT060214'),
	(1585, 'CT060118'),
	(1586, 'CT060321'),
	(1587, 'AT150431'),
	(1588, 'DT050228'),
	(1589, 'AT141048'),
	(1590, 'CT060337'),
	(1591, 'DT050134'),
	(1592, 'CT040126'),
	(1593, 'CT070136'),
	(1594, 'CT030241'),
	(1595, 'DT040103'),
	(1596, 'DT040221'),
	(1597, 'DT040146'),
	(1598, 'DT040103'),
	(1599, 'DT040123'),
	(1600, 'DT040221'),
	(1601, 'DT040146'),
	(1602, 'DT040103'),
	(1603, 'DT040204'),
	(1604, 'DT040124'),
	(1605, 'DT040237'),
	(1606, 'DT040238'),
	(1607, 'DT040146'),
	(1608, 'DT040247'),
	(1609, 'DT050228'),
	(1610, 'DT050131'),
	(1611, 'DT050228'),
	(1612, 'DT050134'),
	(1613, 'DT050207'),
	(1614, 'DT050216'),
	(1615, 'DT030225'),
	(1616, 'DT050228'),
	(1617, 'DT050134'),
	(1618, 'DT030225'),
	(1619, 'DT050228'),
	(1620, 'DT050134'),
	(1621, 'DT060107'),
	(1622, 'DT040247'),
	(1623, 'DT050134');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;