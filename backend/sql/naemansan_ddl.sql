DROP TABLE IF EXISTS `badges`;
DROP TABLE IF EXISTS `badge_names`;
DROP TABLE IF EXISTS `comments`;
DROP TABLE IF EXISTS `likes`;
DROP TABLE IF EXISTS `course_tags`;
DROP TABLE IF EXISTS `using_courses`;
DROP TABLE IF EXISTS `enrollment_courses`;
DROP TABLE IF EXISTS `individual_courses`;
DROP TABLE IF EXISTS `images`;
DROP TABLE IF EXISTS `advertisements`;
DROP TABLE IF EXISTS `shops`;
DROP TABLE IF EXISTS `tokens`;
DROP TABLE IF EXISTS `follows`;
DROP TABLE IF EXISTS `subscribes`;
DROP TABLE IF EXISTS `notifications`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `id` integer AUTO_INCREMENT,
                         `social_id` varchar(255) NOT NULL,
                         `provider` enum('KAKAO', 'GOOGLE', 'APPLE') NOT NULL,
                         `name` varchar(255) NOT NULL,
                         `introduction` varchar(255),
                         `role` enum('USER', 'ADMIN') NOT NULL,
                         `created_date` timestamp NOT NULL,
                         `is_login` boolean NOT NULL,
                         `refresh_token` varchar(300),
                         `device_token` varchar(300),
                         CONSTRAINT USERS_PK PRIMARY KEY (`id`),
                         CONSTRAINT USERS_CK UNIQUE(`social_id`, `provider`)
);

CREATE TABLE `notifications` (
                                 `id` integer AUTO_INCREMENT,
                                 `user_id` integer NOT NULL,
                                 `title` varchar(255),
                                 `content` varchar(300),
                                 `create_date` timestamp NOT NULL,
                                 `is_read_status` boolean NOT NULL,
                                 CONSTRAINT NOTIFICATIONS_PK PRIMARY KEY (`id`),
                                 CONSTRAINT NOTIFICATIONS_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `subscribes` (
                              `id` integer AUTO_INCREMENT,
                              `user_id` integer NOT NULL,
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
                           `following_user_id` integer NOT NULL,
                           `followed_user_id` integer NOT NULL,
                           `created_at` timestamp NOT NULL,
                           CONSTRAINT FOLLOWS_PK PRIMARY KEY (`id`),
                           CONSTRAINT FOLLOWS_CK UNIQUE(`following_user_id`, `followed_user_id`),
                           CONSTRAINT FOLLOWS_FOLLOWING_FK FOREIGN KEY (`following_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                           CONSTRAINT FOLLOWS_FOLLOWED_FK FOREIGN KEY (`followed_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `shops` (
                         `id` integer AUTO_INCREMENT,
                         `shop_name` varchar(255) NOT NULL,
                         `shop_introduction` varchar(255) NOT NULL,
                         `shop_location` point NOT NULL,

                         CONSTRAINT SHOPS_PK PRIMARY KEY (`id`)
);

CREATE TABLE `advertisements` (
                                  `id` integer AUTO_INCREMENT,
                                  `enterprise_name` varchar(255) NOT NULL,
                                  `enterprise_url` varchar(255) NOT NULL,

                                  CONSTRAINT ADVERTISEMENTS_PK PRIMARY KEY (`id`)
);

CREATE TABLE `images` (
                          `id` integer AUTO_INCREMENT,
                          `use_user` integer,
                          `use_shop` integer,
                          `use_advertisement` integer,
                          `origin_name` varchar(300) NOT NULL,
                          `uuid_name` varchar(300) NOT NULL,
                          `path` varchar(255) NOT NULL,
                          `type` varchar(30) NOT NULL,
                          CONSTRAINT IMAGES_PK PRIMARY KEY (`id`),
                          CONSTRAINT IMAGES_USER_FK FOREIGN KEY (`use_user`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                          CONSTRAINT IMAGES_SHOP_FK FOREIGN KEY (`use_shop`) REFERENCES `shops` (`id`) ON DELETE CASCADE,
                          CONSTRAINT IMAGES_ADVERTISEMENT_FK FOREIGN KEY (`use_advertisement`) REFERENCES advertisements(`id`) ON DELETE CASCADE
);

CREATE TABLE `individual_courses` (
                                      `id` integer AUTO_INCREMENT,
                                      `user_id` integer NOT NULL,
                                      `title` varchar(255),
                                      `locations` multipoint NOT NULL,
                                      `created_date` timestamp NOT NULL,
                                      CONSTRAINT INDIVIDUAL_COURSES_PK PRIMARY KEY (`id`),
                                      CONSTRAINT INDIVIDUAL_COURSES_USER_FK  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `enrollment_courses` (
                                      `id` integer AUTO_INCREMENT,
                                      `user_id` integer DEFAULT 1,
                                      `title` varchar(255),
                                      `introduction` varchar(255),
                                      `start_location_name` varchar(255) NOT NULL,
                                      `start_location` point NOT NULL,
                                      `locations` multipoint NOT NULL,
                                      `distance` double NOT NULL,
                                      `created_date` timestamp NOT NULL,
                                      `status` boolean NOT NULL,
                                      CONSTRAINT ENROLLMENT_COURSES_PK PRIMARY KEY (`id`),
                                      CONSTRAINT ENROLLMENT_COURSES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
);

CREATE TABLE `using_courses` (
                                 `id` integer AUTO_INCREMENT,
                                 `user_id` integer NOT NULL,
                                 `course_id` integer NOT NULL,
                                 `using_date` timestamp NOT NULL,
                                 `finish_status` boolean NOT NULL,
                                 CONSTRAINT USING_COURSES_PK PRIMARY KEY (`id`),
                                 CONSTRAINT USING_COURSES_USER_FK  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT USING_COURSES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `enrollment_courses` (`id`)
);

CREATE TABLE `course_tags` (
                               `id` integer AUTO_INCREMENT,
                               `course_id` integer NOT NULL,
                               `tag` varchar(255) NOT NULL,

                               CONSTRAINT COURSE_TYPES_PK PRIMARY KEY (`id`),
                               CONSTRAINT COURSE_TYPES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `enrollment_courses` (`id`) ON DELETE CASCADE
);

CREATE TABLE `likes` (
                         `id` integer AUTO_INCREMENT,
                         `user_id` integer NOT NULL,
                         `course_id` integer NOT NULL,
                         CONSTRAINT LIKES_PK PRIMARY KEY (`id`),
                         CONSTRAINT LIKES_CK UNIQUE(`user_id`, `course_id`),
                         CONSTRAINT LIKES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                         CONSTRAINT LIKES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `enrollment_courses` (`id`) ON DELETE CASCADE
);

CREATE TABLE `comments` (
                            `id` integer AUTO_INCREMENT,
                            `user_id` integer NOT NULL,
                            `course_id` integer NOT NULL,
                            `create_date` timestamp NOT NULL,
                            `context` varchar(100),
                            `is_edit` boolean,
                            `status` boolean,
                            CONSTRAINT COMMENTS_PK PRIMARY KEY (`id`),
                            CONSTRAINT COMMENTS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                            CONSTRAINT COMMENTS_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `enrollment_courses` (`id`) ON DELETE CASCADE
);

CREATE TABLE `badge_names` (
                               `id` integer AUTO_INCREMENT,
                               `name` varchar(255) NOT NULL,
                               CONSTRAINT BADGE_NAMES_PK PRIMARY KEY (`id`)
);

CREATE TABLE `badges` (
                          `id` integer AUTO_INCREMENT,
                          `user_id` integer NOT NULL,
                          `badge_id` integer NOT NULL,
                          `get_date` timestamp NOT NULL,
                          CONSTRAINT BADGES_PK PRIMARY KEY (`id`),
                          CONSTRAINT BADGES_CK UNIQUE(`user_id`, `badge_id`),
                          CONSTRAINT BADGES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                          CONSTRAINT BADGES_COURSE_FK FOREIGN KEY (`badge_id`) REFERENCES `badge_names` (`id`) ON DELETE CASCADE
);

INSERT INTO users (`social_id`, `provider`, `name`, `introduction`, `role`, `created_date`, `is_login`) VALUES ("0000000git 0", 'KAKAO', "SUPER_ADMIM", "THIS IS ADIMN", 'ADMIN', now(), true);
INSERT INTO images (`use_user`, `use_shop`, `use_advertisement`, `origin_name`, `uuid_name`, `type`, `path`)
VALUES (1, null, null, "default_image.png", "0_default_image.png", "image/png", "C:\Users\HyungJoon\Documents\0_OSSP\resources\images\0_default_image.png");