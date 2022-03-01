-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Feb 18, 2022 alle 07:57
-- Versione del server: 10.4.21-MariaDB
-- Versione PHP: 8.0.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tweb`
--

DELIMITER $$
--
-- Funzioni
--
CREATE DEFINER=`root`@`localhost` FUNCTION `fun` () RETURNS INT(11) BEGIN
  DECLARE inc INT;
  SET inc = 1;
  label: 
WHILE inc < 21 DO
	INSERT INTO slot(slot) VALUES (inc);
    SET inc = inc + 1;
END WHILE label;
  RETURN inc;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struttura della tabella `corso`
--

CREATE TABLE `corso` (
  `id` int(11) NOT NULL,
  `nome` varchar(40) NOT NULL,
  `attivo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `corso`
--

INSERT INTO `corso` (`id`, `nome`, `attivo`) VALUES
(1, 'Tecnologie Web', 1),
(2, 'Interazione Uomo-Macchina', 1),
(3, 'Programmazione III', 1),
(4, 'Linguaggi Formali e Traduttori', 1),
(10, 'Test', 0),
(11, 'Test', 0),
(12, 'Test', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `corso_docente`
--

CREATE TABLE `corso_docente` (
  `id` int(11) NOT NULL,
  `docente_id` int(11) NOT NULL,
  `corso_id` int(11) NOT NULL,
  `attivo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `corso_docente`
--

INSERT INTO `corso_docente` (`id`, `docente_id`, `corso_id`, `attivo`) VALUES
(1, 1, 1, 1),
(2, 1, 3, 1),
(3, 3, 2, 1),
(4, 3, 4, 1),
(6, 2, 2, 1),
(15, 2, 3, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `docente`
--

CREATE TABLE `docente` (
  `id` int(11) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `cognome` varchar(30) NOT NULL,
  `mfx` int(1) NOT NULL,
  `attivo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `docente`
--

INSERT INTO `docente` (`id`, `nome`, `cognome`, `mfx`, `attivo`) VALUES
(1, 'Liliana', 'Ardissono', 1, 1),
(2, 'Marino', 'Segnan', 0, 1),
(3, 'Viviana', 'Patti', 1, 1),
(4, 'Unknow', 'Unknow', 2, 0),
(8, 'jdfdfgfdg', 'gdfhdhf', 0, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `prenotazione`
--

CREATE TABLE `prenotazione` (
  `username` varchar(30) NOT NULL,
  `insegnamento_id` int(11) NOT NULL,
  `slot` int(11) NOT NULL,
  `stato` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `prenotazione`
--

INSERT INTO `prenotazione` (`username`, `insegnamento_id`, `slot`, `stato`) VALUES
('admin', 1, 1, 2),
('admin', 1, 2, 2),
('admin', 2, 1, 2),
('admin', 3, 5, 2),
('admin', 4, 4, 2),
('admin', 6, 11, 2),
('admin', 6, 13, 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `slot`
--

CREATE TABLE `slot` (
  `slot` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `slot`
--

INSERT INTO `slot` (`slot`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10),
(11),
(12),
(13),
(14),
(15),
(16),
(17),
(18),
(19),
(20);

-- --------------------------------------------------------

--
-- Struttura della tabella `utente`
--

CREATE TABLE `utente` (
  `username` varchar(30) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `cognome` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `ruolo` int(1) NOT NULL,
  `mfx` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `utente`
--

INSERT INTO `utente` (`username`, `nome`, `cognome`, `password`, `ruolo`, `mfx`) VALUES
('admin', 'Amministratore', 'Admin', 'pwd', 1, 2),
('andre', 'Andrea', 'Brovia', 'pwd', 0, 0),
('simo', 'Simone', 'Geraci', 'pwd', 0, 0),
('user', 'Utente', 'Fittizio', 'pwd', 0, 2);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `corso`
--
ALTER TABLE `corso`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `corso_docente`
--
ALTER TABLE `corso_docente`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `docente_id` (`docente_id`,`corso_id`),
  ADD KEY `corso_id` (`corso_id`);

--
-- Indici per le tabelle `docente`
--
ALTER TABLE `docente`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `prenotazione`
--
ALTER TABLE `prenotazione`
  ADD PRIMARY KEY (`username`,`insegnamento_id`,`slot`,`stato`),
  ADD KEY `insegnamento_id` (`insegnamento_id`);

--
-- Indici per le tabelle `slot`
--
ALTER TABLE `slot`
  ADD PRIMARY KEY (`slot`);

--
-- Indici per le tabelle `utente`
--
ALTER TABLE `utente`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `corso`
--
ALTER TABLE `corso`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT per la tabella `corso_docente`
--
ALTER TABLE `corso_docente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT per la tabella `docente`
--
ALTER TABLE `docente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `corso_docente`
--
ALTER TABLE `corso_docente`
  ADD CONSTRAINT `corso_docente_ibfk_1` FOREIGN KEY (`docente_id`) REFERENCES `docente` (`id`),
  ADD CONSTRAINT `corso_docente_ibfk_2` FOREIGN KEY (`corso_id`) REFERENCES `corso` (`id`);

--
-- Limiti per la tabella `prenotazione`
--
ALTER TABLE `prenotazione`
  ADD CONSTRAINT `prenotazione_ibfk_1` FOREIGN KEY (`username`) REFERENCES `utente` (`username`),
  ADD CONSTRAINT `prenotazione_ibfk_2` FOREIGN KEY (`insegnamento_id`) REFERENCES `corso_docente` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
