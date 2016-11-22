ALTER TABLE `tasks`
ADD COLUMN `version` BIGINT(8) NOT NULL AFTER `is_completed`;