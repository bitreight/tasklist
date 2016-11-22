ALTER TABLE `projects` DROP INDEX `projects_title_UNIQUE` ,
ADD UNIQUE INDEX `title_owner_id_UNIQUE` (`title` ASC, `users_owner_id` ASC);

ALTER TABLE `tasks` DROP INDEX `tasks_title_UNIQUE` ,
ADD UNIQUE INDEX `title_project_id_UNIQUE` (`title` ASC, `projects_id` ASC);