-- A single player (client defaults: userId 1, characterId 1) spawning on map 1.
INSERT INTO `accounts` (`id`, `user_id`, `name`, `is_administrator`) VALUES
  (1, 1, 'Evywell#1234', 0);

INSERT INTO `characters` (`id`, `account_id`, `name`, `level`, `position_x`, `position_y`, `position_z`, `orientation`, `last_selected_at`) VALUES
  (1, 1, 'Evywell', 20, 0, 0, 0, 0, NOW());
