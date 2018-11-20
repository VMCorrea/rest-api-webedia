BEGIN TRANSACTION;
DROP TABLE IF EXISTS `comentarios`;
CREATE TABLE IF NOT EXISTS `comentarios` (
	`idComentario`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	`usuario`	TEXT NOT NULL,
	`texto`	TEXT NOT NULL,
	`idArtigo`	INTEGER NOT NULL,
	`data`	TEXT,
	FOREIGN KEY(`idArtigo`) REFERENCES `artigos`(`idArtigo`) ON DELETE CASCADE
);
INSERT INTO `comentarios` VALUES (1,'joaozinho123','Texto do comentário',3,'2018-11-16 17:44:02');
INSERT INTO `comentarios` VALUES (2,'joaozinho123','Texto do comentário',7,'2018-11-16 17:44:19');
INSERT INTO `comentarios` VALUES (3,'joaozinho123','Texto do comentário',9,'2018-11-16 17:44:30');
INSERT INTO `comentarios` VALUES (4,'alice_s2','Texto do comentário',9,'2018-11-16 17:44:44');
INSERT INTO `comentarios` VALUES (5,'alice_s2','Texto do comentário',9,'2018-11-16 17:44:47');
INSERT INTO `comentarios` VALUES (6,'pedrojr','Texto do comentário',5,'2018-11-16 17:45:17');
INSERT INTO `comentarios` VALUES (7,'pedrojr','Texto do comentário',2,'2018-11-16 17:45:31');
INSERT INTO `comentarios` VALUES (8,'pedrojr','Texto do comentário',10,'2018-11-16 17:45:42');
INSERT INTO `comentarios` VALUES (9,'lucas758','Texto do comentário',6,'2018-11-16 17:46:42');
INSERT INTO `comentarios` VALUES (10,'lucas758','Texto do comentário',6,'2018-11-16 17:46:45');
DROP TABLE IF EXISTS `autores`;
CREATE TABLE IF NOT EXISTS `autores` (
	`idAutor`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	`nome`	TEXT NOT NULL,
	`sobrenome`	TEXT NOT NULL,
	`bio`	TEXT
);
INSERT INTO `autores` VALUES (1,'Victor','Corrêa','Bio Qualquer');
INSERT INTO `autores` VALUES (2,'Ariana','Golden','Bio Qualquer');
INSERT INTO `autores` VALUES (3,'Warren','Banks','Bio Qualquer');
INSERT INTO `autores` VALUES (4,'Saul','Stokes','Bio Qualquer');
INSERT INTO `autores` VALUES (5,'Marni','Wiley','Bio Qualquer');
INSERT INTO `autores` VALUES (6,'Amanda','Costa','Bio Qualquer');
INSERT INTO `autores` VALUES (7,'Eduardo','Cardoso','Bio Qualquer');
INSERT INTO `autores` VALUES (8,'Leonardo','Gomes','Bio Qualquer');
INSERT INTO `autores` VALUES (9,'Melissa','Dias','Bio Qualquer');
INSERT INTO `autores` VALUES (10,'Ana','Castro','Bio Qualquer');
INSERT INTO `autores` VALUES (11,'Julieta','Silva','Bio Qualquer');
DROP TABLE IF EXISTS `artigos`;
CREATE TABLE IF NOT EXISTS `artigos` (
	`idArtigo`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	`titulo`	TEXT NOT NULL UNIQUE,
	`subtitulo`	TEXT NOT NULL,
	`dataPublicacao`	TEXT NOT NULL,
	`dataAtualizacao`	TEXT,
	`conteudo`	TEXT NOT NULL,
	`permalink`	TEXT NOT NULL UNIQUE
);
INSERT INTO `artigos` VALUES (2,'Programação em Java - Parte 1','Curso de Java','2018-11-16 17:28:09',NULL,'Conteúdo do artigo','programacao-em-java-parte-1');
INSERT INTO `artigos` VALUES (3,'Programação em Java - Parte 2','Curso de Java','2018-11-16 17:28:35',NULL,'Conteúdo do artigo','programacao-em-java-parte-2');
INSERT INTO `artigos` VALUES (4,'Programação em Java - Parte 3','Curso de Java','2018-11-16 17:29:23',NULL,'Conteúdo do artigo','programacao-em-java-parte-3');
INSERT INTO `artigos` VALUES (5,'HTML e CSS do Zero - Parte 1','Curso de Frontend','2018-11-16 17:30:50','2018-11-16 17:38:18','Conteúdo Atualizado','html-e-css-do-zero-parte-1');
INSERT INTO `artigos` VALUES (6,'HTML e CSS do Zero - Parte 2','Curso de Frontend','2018-11-16 17:30:55',NULL,'Conteúdo do artigo','html-e-css-do-zero-parte-2');
INSERT INTO `artigos` VALUES (7,'Aprendendo Javascript','Curso de Frontend','2018-11-16 17:31:32',NULL,'Conteúdo do artigo','aprendendo-javascript');
INSERT INTO `artigos` VALUES (8,'Aprendendo a Programar - Parte 1','Curso de Algoritmos','2018-11-16 17:33:19','2018-11-16 17:38:38','Conteúdo Atualizado','aprendendo-a-programar-parte-1');
INSERT INTO `artigos` VALUES (9,'Aprendendo a Programar - Parte 2','Curso de Algoritmos','2018-11-16 17:33:29',NULL,'Conteúdo do artigo','aprendendo-a-programar-parte-2');
INSERT INTO `artigos` VALUES (10,'Aprendendo a Programar - Parte 3','Curso de Algoritmos','2018-11-16 17:33:35',NULL,'Conteúdo do artigo','aprendendo-a-programar-parte-3');
INSERT INTO `artigos` VALUES (11,'Aprendendo SQL','Curso de Banco de Dados','2018-11-16 18:52:12',NULL,'Conteúdo do artigo','aprendendo-sql');
DROP TABLE IF EXISTS `artigoAutores`;
CREATE TABLE IF NOT EXISTS `artigoAutores` (
	`idArtigo`	INTEGER NOT NULL,
	`idAutor`	INTEGER NOT NULL,
	FOREIGN KEY(`idArtigo`) REFERENCES `artigos`(`idArtigo`) ON DELETE CASCADE,
	FOREIGN KEY(`idAutor`) REFERENCES `autores`(`idAutor`) ON DELETE CASCADE
);
INSERT INTO `artigoAutores` VALUES (2,1);
INSERT INTO `artigoAutores` VALUES (3,1);
INSERT INTO `artigoAutores` VALUES (4,1);
INSERT INTO `artigoAutores` VALUES (4,5);
INSERT INTO `artigoAutores` VALUES (5,3);
INSERT INTO `artigoAutores` VALUES (6,3);
INSERT INTO `artigoAutores` VALUES (7,7);
INSERT INTO `artigoAutores` VALUES (8,8);
INSERT INTO `artigoAutores` VALUES (8,2);
INSERT INTO `artigoAutores` VALUES (8,10);
INSERT INTO `artigoAutores` VALUES (9,8);
INSERT INTO `artigoAutores` VALUES (9,2);
INSERT INTO `artigoAutores` VALUES (9,10);
INSERT INTO `artigoAutores` VALUES (10,8);
INSERT INTO `artigoAutores` VALUES (10,2);
INSERT INTO `artigoAutores` VALUES (10,10);
INSERT INTO `artigoAutores` VALUES (11,4);
COMMIT;
