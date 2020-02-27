-- --------------------------------------------------------
-- Host:                         localhost
-- Versión del servidor:         10.1.38-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para pokemon
DROP DATABASE IF EXISTS `pokemon`;
CREATE DATABASE IF NOT EXISTS `pokemon` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `pokemon`;

-- Volcando estructura para tabla pokemon.habilidad
DROP TABLE IF EXISTS `habilidad`;
CREATE TABLE IF NOT EXISTS `habilidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla pokemon.habilidad: ~7 rows (aproximadamente)
DELETE FROM `habilidad`;
/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` (`id`, `nombre`) VALUES
	(6, 'ascua'),
	(5, 'electricidad estatica'),
	(2, 'foco interno'),
	(7, 'hedor'),
	(1, 'impasible'),
	(3, 'justiciero'),
	(4, 'para rayos');
/*!40000 ALTER TABLE `habilidad` ENABLE KEYS */;

-- Volcando estructura para tabla pokemon.pokemon
DROP TABLE IF EXISTS `pokemon`;
CREATE TABLE IF NOT EXISTS `pokemon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla pokemon.pokemon: ~4 rows (aproximadamente)
DELETE FROM `pokemon`;
/*!40000 ALTER TABLE `pokemon` DISABLE KEYS */;
INSERT INTO `pokemon` (`id`, `nombre`) VALUES
	(4, 'bulbasur'),
	(3, 'charmander'),
	(2, 'pikachu');
/*!40000 ALTER TABLE `pokemon` ENABLE KEYS */;

-- Volcando estructura para tabla pokemon.pokemon_habilidades
DROP TABLE IF EXISTS `pokemon_habilidades`;
CREATE TABLE IF NOT EXISTS `pokemon_habilidades` (
  `id_pokemon` int(11) NOT NULL,
  `id_hablidad` int(11) NOT NULL,
  PRIMARY KEY (`id_pokemon`,`id_hablidad`),
  KEY `FK_habilidad` (`id_hablidad`),
  CONSTRAINT `FK_habilidad` FOREIGN KEY (`id_hablidad`) REFERENCES `habilidad` (`id`),
  CONSTRAINT `FK_pokemon` FOREIGN KEY (`id_pokemon`) REFERENCES `pokemon` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla pokemon.pokemon_habilidades: ~6 rows (aproximadamente)
DELETE FROM `pokemon_habilidades`;
/*!40000 ALTER TABLE `pokemon_habilidades` DISABLE KEYS */;
INSERT INTO `pokemon_habilidades` (`id_pokemon`, `id_hablidad`) VALUES
	(2, 4),
	(2, 5),
	(3, 1),
	(3, 3),
	(3, 6),
	(4, 1),
	(4, 7);
/*!40000 ALTER TABLE `pokemon_habilidades` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
