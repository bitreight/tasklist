-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(20) NOT NULL,
  `description` VARCHAR(500) NULL,
  `users_owner_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `projects_title_UNIQUE` (`title` ASC),
  CONSTRAINT `fk_projects_users`
    FOREIGN KEY (`users_owner_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tasks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(500) NULL,
  `deadline` DATE NULL,
  `task_priority` VARCHAR(10) NOT NULL,
  `projects_id` INT NOT NULL,
  `is_completed` BOOLEAN NOT NULL,
  `version` INT8 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `tasks_title_UNIQUE` (`title` ASC),
  CONSTRAINT `fk_tasks_projects`
    FOREIGN KEY (`projects_id`)
    REFERENCES `projects` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;