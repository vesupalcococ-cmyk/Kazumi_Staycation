-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 30, 2026 at 04:24 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kazumi_staycation`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_admin`
--

CREATE TABLE `tbl_admin` (
  `ID` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_admin`
--

INSERT INTO `tbl_admin` (`ID`, `username`, `password`) VALUES
(1, 'kazumi', 'admin123');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_guest`
--

CREATE TABLE `tbl_guest` (
  `ID` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contact` varchar(11) NOT NULL,
  `check_in` date NOT NULL,
  `check_out` date NOT NULL,
  `time_in` time DEFAULT NULL,
  `time_out` time DEFAULT NULL,
  `booking_type` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_guest`
--

INSERT INTO `tbl_guest` (`ID`, `full_name`, `email`, `contact`, `check_in`, `check_out`, `time_in`, `time_out`, `booking_type`) VALUES
(1, 'Venice', 'venice@gmail.com', '09123456789', '2026-10-15', '2026-10-16', '14:36:02', '14:36:06', 'Reservation'),
(2, 'caleb', 'caleb@gmail.com', '09232467861', '2026-10-31', '2026-11-02', NULL, NULL, 'Reservation'),
(3, 'edrian', 'edrian@gmail.com', '09123456789', '2026-10-18', '2026-10-19', '18:50:12', '18:55:43', 'Reservation');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_payment`
--

CREATE TABLE `tbl_payment` (
  `payment_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `grand_total` decimal(10,2) NOT NULL,
  `downpayment` decimal(10,2) NOT NULL,
  `balance` decimal(10,2) NOT NULL,
  `payment_method` varchar(20) NOT NULL,
  `reference_number` varchar(13) DEFAULT NULL,
  `payment_date` date NOT NULL,
  `payment_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_payment`
--

INSERT INTO `tbl_payment` (`payment_id`, `guest_id`, `grand_total`, `downpayment`, `balance`, `payment_method`, `reference_number`, `payment_date`, `payment_time`) VALUES
(1, 1, 900.00, 900.00, 0.00, 'GCASH', '0987654321234', '2026-10-14', '02:00:00'),
(2, 2, 5000.00, 4000.00, 1000.00, 'BPI', '1212345467864', '2026-10-31', '05:00:00'),
(3, 3, 2500.00, 2500.00, 0.00, 'GCASH', '1234567890123', '2026-10-14', '02:30:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_admin`
--
ALTER TABLE `tbl_admin`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `tbl_guest`
--
ALTER TABLE `tbl_guest`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `tbl_payment`
--
ALTER TABLE `tbl_payment`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `guest_id` (`guest_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_admin`
--
ALTER TABLE `tbl_admin`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_guest`
--
ALTER TABLE `tbl_guest`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tbl_payment`
--
ALTER TABLE `tbl_payment`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_payment`
--
ALTER TABLE `tbl_payment`
  ADD CONSTRAINT `tbl_payment_ibfk_1` FOREIGN KEY (`guest_id`) REFERENCES `tbl_guest` (`ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
