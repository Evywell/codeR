-- Reference data loaded before every E2E fixture (after all tables are truncated).
-- Only static game content here: no accounts, no characters.

-- Maps
INSERT INTO `maps` (`id`, `name`, `width`, `height`) VALUES
  (1, 'Middle Of Nowhere', 200, 200);

-- Zones
INSERT INTO `zones` (`map_id`, `name`, `width`, `height`, `offset_x`, `offset_y`) VALUES
  (1, 'Entire zone', 200, 200, 0, 0);
