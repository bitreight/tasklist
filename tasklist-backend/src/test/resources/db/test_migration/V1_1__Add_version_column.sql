ALTER TABLE `tasks`
ADD COLUMN `version` INT8 NOT NULL AFTER `is_completed`;