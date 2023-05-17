DROP TABLE IF EXISTS `badges`;
DROP TABLE IF EXISTS `badge_names`;
DROP TABLE IF EXISTS `comments`;
DROP TABLE IF EXISTS `likes`;
DROP TABLE IF EXISTS `using_courses`;
DROP TABLE IF EXISTS `course_tags`;
DROP TABLE IF EXISTS `courses`;
DROP TABLE IF EXISTS `images`;
DROP TABLE IF EXISTS `advertisements`;
DROP TABLE IF EXISTS `shops`;
DROP TABLE IF EXISTS `tokens`;
DROP TABLE IF EXISTS `follows`;
DROP TABLE IF EXISTS `subscribes`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `id` integer AUTO_INCREMENT,
                         `social_login_id` varchar(255),
                         `provider` enum('KAKAO', 'GOOGLE', 'APPLE'),
                         `name` varchar(255),
                         `introduction` varchar(255),
                         `role` enum('USER', 'ADMIN'),
                         `created_date` timestamp,
                         CONSTRAINT USERS_PK PRIMARY KEY (`id`),
                         CONSTRAINT USERS_CK UNIQUE(`social_login_id`, `provider`)
);

CREATE TABLE `subscribes` (
                              `id` integer AUTO_INCREMENT,
                              `user_id` integer,
                              `pay_type` varchar(255),
                              `created_at` time,
                              `successed_at` time,
                              `expiration_date` date,
                              `next_order_date` date,
                              `biliing_key` varchar(300),
                              `next_refresh` boolean,
                              CONSTRAINT SUBSCRIBES_PK PRIMARY KEY (`id`),
                              CONSTRAINT SUBSCRIBES_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `follows` (
                           `id` integer AUTO_INCREMENT,
                           `following_user_id` integer,
                           `followed_user_id` integer,
                           `created_at` timestamp,
                           CONSTRAINT FOLLOWS_PK PRIMARY KEY (`id`),
                           CONSTRAINT FOLLOWS_CK UNIQUE(`following_user_id`, `followed_user_id`),
                           CONSTRAINT FOLLOWS_FOLLOWING_FK FOREIGN KEY (`following_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                           CONSTRAINT FOLLOWS_FOLLOWED_FK FOREIGN KEY (`followed_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `tokens` (
                          `id` integer AUTO_INCREMENT,
                          `user_id` integer UNIQUE,
                          `refresh_token` varchar(300),
                          CONSTRAINT TOKENS_PK PRIMARY KEY (`id`),
                          CONSTRAINT TOKENS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `shops` (
                         `id` integer AUTO_INCREMENT,
                         `shop_name` varchar(255),
                         `shop_introduction` varchar(255),
                         `shop_location` point,
                         CONSTRAINT SHOPS_PK PRIMARY KEY (`id`)
);

CREATE TABLE `advertisements` (
                                  `id` integer AUTO_INCREMENT,
                                  `enterprise_name` varchar(255),
                                  `enterprise_url` varchar(255),
                                  CONSTRAINT ADVERTISEMENTS_PK PRIMARY KEY (`id`)
);

CREATE TABLE `images` (
                          `id` integer AUTO_INCREMENT,
                          `use_user` integer,
                          `use_shop` integer,
                          `use_advertisement` integer,
                          `path` varchar(255),
                          CONSTRAINT IMAGES_PK PRIMARY KEY (`id`),
                          CONSTRAINT IMAGES_USER_FK FOREIGN KEY (`use_user`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                          CONSTRAINT IMAGES_SHOP_FK FOREIGN KEY (`use_shop`) REFERENCES `shops` (`id`) ON DELETE CASCADE,
                          CONSTRAINT IMAGES_ADVERTISEMENT_FK FOREIGN KEY (`use_advertisement`) REFERENCES advertisements(`id`) ON DELETE CASCADE
);

CREATE TABLE `courses` (
                           `id` integer AUTO_INCREMENT,
                           `user_id` integer DEFAULT 1,
                           `title` varchar(255),
                           `created_date` timestamp,
                           `introduction` varchar(255),
                           `start_location_name` varchar(255),
                           `start_location` point,
                           `locations` multipoint,
                           `distance` int,
                           `status` boolean,
                           CONSTRAINT COURSES_PK PRIMARY KEY (`id`),
                           CONSTRAINT COURSES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET DEFAULT
);

CREATE TABLE `course_tags` (
                               `id` integer AUTO_INCREMENT,
                               `course_id` integer,
                               `tag` varchar(255),
                               CONSTRAINT COURSE_TYPES_PK PRIMARY KEY (`id`),
                               CONSTRAINT COURSE_TYPES_SCOURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

CREATE TABLE `using_courses` (
                                 `id` integer AUTO_INCREMENT,
                                 `user_id` integer,
                                 `course_id` integer DEFAULT 1,
                                 `finish_date` timestamp,
                                 CONSTRAINT FINISH_COURSES_PK PRIMARY KEY (`id`),
                                 CONSTRAINT FINISH_COURSES_USER_FK  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT FINISH_COURSES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE SET DEFAULT
);

CREATE TABLE `likes` (
                         `id` integer AUTO_INCREMENT,
                         `user_id` integer,
                         `course_id` integer,
                         CONSTRAINT LIKES_PK PRIMARY KEY (`id`),
                         CONSTRAINT LIKES_CK UNIQUE(`user_id`, `course_id`),
                         CONSTRAINT LIKES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                         CONSTRAINT LIKES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

CREATE TABLE `comments` (
                            `id` integer AUTO_INCREMENT,
                            `user_id` integer,
                            `course_id` integer,
                            `create_date` timestamp,
                            `isEdit` boolean,
                            `status` boolean,
                            CONSTRAINT COMMENTS_PK PRIMARY KEY (`id`),
                            CONSTRAINT COMMENTS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                            CONSTRAINT COMMENTS_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

CREATE TABLE `badge_names` (
                               `id` integer AUTO_INCREMENT,
                               `name` varchar(255),
                               CONSTRAINT BADGE_NAMES_PK PRIMARY KEY (`id`)
);

CREATE TABLE `badges` (
                          `id` integer AUTO_INCREMENT,
                          `user_id` integer,
                          `badge_id` integer,
                          `get_date` timestamp,
                          CONSTRAINT BADGES_PK PRIMARY KEY (`id`),
                          CONSTRAINT BADGES_CK UNIQUE(`user_id`, `badge_id`),
                          CONSTRAINT BADGES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                          CONSTRAINT BADGES_COURSE_FK FOREIGN KEY (`badge_id`) REFERENCES `badge_names` (`id`) ON DELETE CASCADE
);

CREATE TABLE `notifications` (
    `id` integer AUTO_INCREMENT,
    `user_id` integer,
    `content` varchar(300),
    `create_date` timestamp,
    `is_read_status` boolean,
    CONSTRAINT NOTIFICATIONS_PK PRIMARY KEY (`id`),
    CONSTRAINT NOTIFICATIONS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
)

INSERT INTO users (`social_login_id`, `provider`, `name`, `introduction`, `role`, `created_date`)VALUES ("00000000", 'KAKAO', "DEFAULT_ADMIM", "THIS IS ADIMN", 'ADMIN', null);
INSERT INTO tokens (`user_id`, `refresh_token`) VALUES (1, NULL);
INSERT INTO images (`use_user`, `use_shop`, `use_advertisement`, `path`) VALUES (1, NULL, NULL, "이미지 경로입니다.");