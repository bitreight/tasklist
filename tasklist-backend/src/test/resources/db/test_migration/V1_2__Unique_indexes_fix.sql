ALTER TABLE `projects` DROP CONSTRAINT `projects_title_UNIQUE`;
CREATE UNIQUE INDEX `title_owner_id_UNIQUE` ON `projects`(`title` ASC, `users_owner_id` ASC);

ALTER TABLE `tasks` DROP CONSTRAINT `tasks_title_UNIQUE`;
CREATE UNIQUE INDEX `title_project_id_UNIQUE` ON `tasks`(`title` ASC, `projects_id` ASC);