drop table users;
drop table follows;
drop table social_tokens;
drop table shops;
drop table advertisements;
drop table images;
drop table courses;
drop table course_types;
drop table finish_courses;
drop table likes;
drop table comments;
drop table badge_names;
drop table badges;


CREATE TABLE `users` (
                         `id` integer AUTO_INCREMENT,
                         `email` varchar(255) UNIQUE,
                         `name` varchar(255),
                         `introduction` varchar(255),
                         `role` boolean,
                         `created_date` timestamp,
                         `edited_date` timestamp,
                         `isPremium` boolean,
                         `pay_type` varchar(255),
                         `created_at` time,
                         `successed_at` time,
                         `expiration_date` date,
                         `next_order_date` date,
                         `biliing_key` varchar(300),
                         `next_refresh` boolean,

                         CONSTRAINT USER_PK PRIMARY KEY (`id`, `email`)
);

CREATE TABLE `follows` (
                           `following_user_id` integer,
                           `followed_user_id` integer,
                           `created_at` timestamp,

                           CONSTRAINT FOLLOWS_PK PRIMARY KEY (`following_user_id`, `followed_user_id`),
                           CONSTRAINT FOLLOWS_FOLLOWING_FK FOREIGN KEY (`following_user_id`) REFERENCES `users` (`id`),
                           CONSTRAINT FOLLOWS_FOLLOWED_FK FOREIGN KEY (`followed_user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `social_tokens` (
                                 `user_id` integer,
                                 `provide_type` varchar(255),
                                 `refresh_token` varchar(255),

                                 CONSTRAINT SOCIAL_TOKENS_PK PRIMARY KEY (`user_id`),
                                 CONSTRAINT SOCIAL_TOKENS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
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
                          CONSTRAINT IMAGES_USER_FK FOREIGN KEY (`use_user`) REFERENCES `users` (`id`),
                          CONSTRAINT IMAGES_SHOP_FK FOREIGN KEY (`use_shop`) REFERENCES `shops` (`id`),
                          CONSTRAINT IMAGES_ADVERTISEMENT_FK FOREIGN KEY (`use_advertisement`) REFERENCES advertisements(`id`)
);

CREATE TABLE `courses` (
                           `id` integer AUTO_INCREMENT,
                           `user_id` integer,
                           `title` varchar(255),
                           `created_date` timestamp,
                           `introduction` varchar(255),
                           `start_location` varchar(255),
                           `locations` multipoint,
                           `distance` int,
                           `status` boolean,
                           CONSTRAINT COURSES_PK PRIMARY KEY (`id`),
                           CONSTRAINT COURSES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `course_types` (
                                `id` integer AUTO_INCREMENT,
                                `course_id` integer,
                                `tag` varchar(255),

                                CONSTRAINT COURSE_TYPES_PK PRIMARY KEY (`id`),
                                CONSTRAINT COURSE_TYPES_SCOURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
);

CREATE TABLE `finish_courses` (
                                  `id` integer AUTO_INCREMENT,
                                  `user_id` integer,
                                  `course_id` integer,
                                  `finish_date` timestamp,

                                  CONSTRAINT FINISH_COURSES_PK PRIMARY KEY (`id`),
                                  CONSTRAINT FINISH_COURSES_USER_FK  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                  CONSTRAINT FINISH_COURSES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
);

CREATE TABLE `likes` (
                         `user_id` integer,
                         `course_id` integer,

                         CONSTRAINT LIKES_PK PRIMARY KEY (`user_id`, `course_id`),
                         CONSTRAINT LIKES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                         CONSTRAINT LIKES_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
);

CREATE TABLE `comments` (
                            `id` integer AUTO_INCREMENT,
                            `user_id` integer,
                            `course_id` integer,
                            `create_date` timestamp,
                            `isEdit` boolean,
                            `status` boolean,

                            CONSTRAINT COMMENTS_PK PRIMARY KEY (`id`),
                            CONSTRAINT COMMENTS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                            CONSTRAINT COMMENTS_COURSE_FK FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
);

CREATE TABLE `badge_names` (
                               `id` integer AUTO_INCREMENT,
                               `name` varchar(255),

                               CONSTRAINT BADGE_NAMES_PK PRIMARY KEY (`id`)
);

CREATE TABLE `badges` {
    `user_id` integer,
    `badge_id` integer,
    `get_date` timestamp,

    CONSTRAINT BADGES_PK PRIMARY KEY (`user_id`, `badge_id`),
    CONSTRAINT BADGES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT BADGES_COURSE_FK FOREIGN KEY (`badge_id`) REFERENCES `badge_names` (`id`)
    }