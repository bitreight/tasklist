USE `tasklist` ;

-- -----------------------------------------------------
-- Table `tasklist`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tasklist`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasklist`.`projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tasklist`.`projects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(20) NOT NULL,
  `description` VARCHAR(500) NULL,
  `users_owner_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC),
  INDEX `fk_projects_users1_idx` (`users_owner_id` ASC),
  CONSTRAINT `fk_projects_users1`
    FOREIGN KEY (`users_owner_id`)
    REFERENCES `tasklist`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasklist`.`tasks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tasklist`.`tasks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(500) NULL,
  `deadline` DATE NULL,
  `task_priority` VARCHAR(10) NOT NULL,
  `projects_id` INT NOT NULL,
  `is_completed` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC),
  INDEX `fk_tasks_projects1_idx` (`projects_id` ASC),
  CONSTRAINT `fk_tasks_projects`
    FOREIGN KEY (`projects_id`)
    REFERENCES `tasklist`.`projects` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;