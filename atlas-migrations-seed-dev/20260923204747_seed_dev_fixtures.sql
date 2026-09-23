-- Accounts
-- We intentionally skip user_id/account id 4 (kept free for a "no account" test case).
INSERT INTO `accounts` (`id`, `user_id`, `name`, `is_administrator`) VALUES
  (1, 1, 'Evywell#1234', 0),
  (2, 2, 'Hello#5678', 0),
  (3, 3, 'Hey#1452', 0);

-- Characters
-- We intentionally skip account_id 2 (a dedicated fixture account with no characters).
INSERT INTO `characters` (`id`, `account_id`, `name`, `level`, `position_x`, `position_y`, `position_z`, `orientation`, `last_selected_at`) VALUES
  (1, 1, 'Evywell', 20, 0, 0, 0, 0, NOW()),
  (2, 3, 'Tarthas', 20, 0, 0, 0, 0, NOW());

-- Maps
INSERT INTO `maps` (`id`, `name`, `width`, `height`) VALUES
  (1, 'Middle Of Nowhere', 200, 200);

-- Zones
INSERT INTO `zones` (`map_id`, `name`, `width`, `height`, `offset_x`, `offset_y`) VALUES
  (1, 'Nowhere top-left', 100, 50, -100, -50),
  (1, 'Nowhere top-right', 100, 50, 0, -50),
  (1, 'Nowhere bottom-left', 100, 50, -100, 0),
  (1, 'Nowhere bottom-right', 100, 50, 0, 0);
