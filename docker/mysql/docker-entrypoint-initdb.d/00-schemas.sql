create table accounts
(
    id               int unsigned auto_increment
        primary key,
    user_id          int unsigned                         not null,
    is_administrator tinyint(1)                           not null,
    name             varchar(255)                         not null,
    banned_at        datetime                             null,
    is_locked        tinyint(1) default 0                 not null,
    created_at       timestamp  default CURRENT_TIMESTAMP not null,
    updated_at       timestamp                            null on update CURRENT_TIMESTAMP,
    constraint user_id
        unique (user_id)
)
    collate = utf8mb3_general_ci;

create table characters
(
    id               int unsigned auto_increment
        primary key,
    account_id       int unsigned                        null,
    name             varchar(255)                        not null,
    level            tinyint unsigned                    not null,
    position_x       float                               not null,
    position_y       float                               not null,
    position_z       float                               not null,
    orientation      float                               not null,
    last_selected_at datetime                            not null,
    created_at       timestamp default CURRENT_TIMESTAMP not null,
    updated_at       timestamp                           null on update CURRENT_TIMESTAMP,
    constraint name
        unique (name),
    constraint characters_ibfk_1
        foreign key (account_id) references accounts (id)
            on delete set null
)
    collate = utf8mb3_general_ci;

create index account_id
    on characters (account_id);

create table creature_templates
(
    id         int unsigned auto_increment
        primary key,
    name       varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_general_ci;

create table default_instances
(
    id         int unsigned auto_increment
        primary key,
    map_id     int unsigned                        not null,
    zone_id    int unsigned                        not null,
    node_name  varchar(255)                        not null,
    type       tinyint unsigned                    not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_general_ci;

create index map_id
    on default_instances (map_id, zone_id);

create table instances
(
    id         int unsigned auto_increment
        primary key,
    map_id     int unsigned                        not null,
    zone_id    int unsigned                        not null,
    type       tinyint unsigned                    not null,
    sub_type   tinyint unsigned                    null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_general_ci;

create index map_id
    on instances (map_id, type);

create table maps
(
    id     int unsigned auto_increment
        primary key,
    name   varchar(255) not null,
    width  int unsigned not null,
    height int unsigned not null
)
    collate = utf8mb3_general_ci;

create table objects
(
    id          int unsigned auto_increment
        primary key,
    map_id      int unsigned                        not null,
    template_id int unsigned                        not null comment 'Foreign key on *_templates tables',
    type        tinyint unsigned                    not null,
    position_x  float                               not null comment 'X position on the map (not the zone)',
    position_y  float                               not null comment 'Y position on the map (not the zone)',
    position_z  float                               not null comment 'Z position on the map (not the zone)',
    orientation float                               not null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    updated_at  timestamp                           null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_general_ci;

create table creatures
(
    id         int unsigned auto_increment
        primary key,
    object_id  int unsigned                        null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP,
    constraint creatures_ibfk_1
        foreign key (object_id) references objects (id)
            on delete set null
)
    collate = utf8mb3_general_ci;

create index object_id
    on creatures (object_id);

create index map_id
    on objects (map_id);

create index type
    on objects (type, template_id);

create table orchestrators
(
    id         int unsigned auto_increment
        primary key,
    address    varchar(255)                        not null,
    token      varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_general_ci;

create table security_bans
(
    id         int unsigned auto_increment
        primary key,
    ip         varchar(255)                        not null,
    service    varchar(255)                        not null,
    end_at     datetime                            not null,
    reason     varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_general_ci;

create index ip
    on security_bans (ip);

create table tickets
(
    id           int unsigned auto_increment
        primary key,
    token        varchar(40)                         not null,
    account_id   int unsigned                        not null,
    character_id int unsigned                        not null,
    source_id    int unsigned                        not null,
    target_id    int unsigned                        not null,
    is_punched   tinyint(1)                          not null,
    expire_at    datetime                            not null,
    created_at   timestamp default CURRENT_TIMESTAMP not null,
    updated_at   timestamp                           null on update CURRENT_TIMESTAMP,
    constraint token
        unique (token),
    constraint tickets_ibfk_1
        foreign key (account_id) references accounts (id)
            on delete cascade,
    constraint tickets_ibfk_2
        foreign key (character_id) references characters (id)
            on delete cascade
)
    collate = utf8mb3_general_ci;

create index account_id
    on tickets (account_id);

create index character_id
    on tickets (character_id);

create table zones
(
    id         int unsigned auto_increment
        primary key,
    map_id     int unsigned                        null,
    name       varchar(255)                        null,
    width      int unsigned                        not null,
    height     int unsigned                        not null,
    offset_x   float                               not null comment 'Represents the X coordinate of the origin point of the map',
    offset_y   float                               not null comment 'Represents the Y coordinate of the origin point of the map',
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp                           null on update CURRENT_TIMESTAMP,
    constraint zones_ibfk_1
        foreign key (map_id) references maps (id)
            on delete set null
)
    collate = utf8mb3_general_ci;

create index map_id
    on zones (map_id);

