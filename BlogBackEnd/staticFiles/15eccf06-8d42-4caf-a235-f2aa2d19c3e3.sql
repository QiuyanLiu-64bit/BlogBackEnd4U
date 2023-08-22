DROP TABLE `follow` ;
DROP TABLE `dynamic_likes` ;
DROP TABLE `dynamics` ;
DROP TABLE `comments` ;
DROP TABLE `article_likes` ;
DROP TABLE `article_favorites` ;
DROP TABLE `articles` ;
DROP TABLE `categories` ;
DROP TABLE `users` ;
DROP TABLE `verification_codes` ;
DROP TABLE `read_article` ;
#-----------------------------------------------------建表语言-----------------------------------------------------
#-----------------------------------------------------建表语言-----------------------------------------------------
#-----------------------------------------------------建表语言-----------------------------------------------------
create table article_favorites
(
   u_id                 int not null  comment '',
   a_id                 int not null  comment '',
   primary key (u_id, a_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table article_favorites comment '文章收藏';

create table article_likes
(
   u_id                 int not null  comment '',
   a_id                 int not null  comment '',
   primary key (u_id, a_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table article_likes comment '文章点赞';

create table articles
(
   a_id                 int not null auto_increment comment '',
   u_id                 int not null  comment '',
   cg_id                int not null  comment '',
   a_tabloid            varchar(255)  comment '',
   a_content            text not null  comment '',
   a_tags               varchar(255)  comment '',
   a_title              varchar(255) not null  comment '',
   a_create_time        date not null  comment '',
   a_deliver_time       date not null  comment '',
   a_update_time        date not null  comment '',
   a_cover_url          varchar(255) comment '',
   primary key (a_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8' AUTO_INCREMENT = 10000000;

alter table articles comment '文章';

create table categories
(
   cg_id                int not null  auto_increment comment '',
   cg_name              varchar(255) not null  comment '',
   primary key (cg_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table categories comment '分类';

create table comments
(
   c_id                 int not null  auto_increment comment '',
   u_id                 int not null  comment '',
   a_id                 int not null  comment '',
   c_time               datetime  comment '',
   c_content            text  comment '',
   primary key (c_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table comments comment '评论';

create table dynamic_likes
(
   d_id                 int not null  comment '',
   u_id                 int not null  comment '',
   primary key (d_id, u_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table dynamic_likes comment '动态点赞';

create table dynamics
(
   d_id                 int not null  auto_increment comment '',
   u_id                 int not null  comment '',
   d_content            text not null  comment '',
   d_create_time        bigint(20) not null comment'',
   d_image1             varchar(255)  comment '',
   d_image2             varchar(255)  comment '',
   d_image3             varchar(255)  comment '',
   d_image4             varchar(255)  comment '',
   primary key (d_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table dynamics comment '动态';

create table follow
(
   u_id                 int not null  comment '',
   use_u_id             int not null  comment '',
   primary key (u_id, use_u_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table follow comment '关注';

create table users
(
   u_id                 int not null  auto_increment comment '',
   u_email              varchar(255) not null  comment '',
   u_password           varchar(255) not null  comment '',
   u_type               bool not null  comment '',
   u_nickname           varchar(255) not null  comment '',
   u_birth_date         date not null  comment '',
   u_register_date      date not null  comment '',
   u_signature          text  comment '',
   u_avatar_url         varchar(255)  comment '',
   primary key (u_id)
)ENGINE=InnoDB DEFAULT CHARSET='utf8' AUTO_INCREMENT = 60000000;

alter table users comment '用户';

CREATE TABLE verification_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    UNIQUE(email)
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table verification_codes comment '验证码表';

CREATE TABLE read_article (
    v_id INT AUTO_INCREMENT PRIMARY KEY,
    u_id int NOT NULL,
    a_id int NOT NULL,
    v_time TIMESTAMP NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

alter table read_article comment '阅读记录表';

alter table article_favorites add constraint FK_ARTICLE__ARTICLE_F_USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table article_favorites add constraint FK_ARTICLE__ARTICLE_F_ARTICLES foreign key (a_id)
      references articles (a_id) on delete restrict on update restrict;

alter table article_likes add constraint FK_ARTICLE__ARTICLE_L_USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table article_likes add constraint FK_ARTICLE__ARTICLE_L_ARTICLES foreign key (a_id)
      references articles (a_id) on delete restrict on update restrict;

alter table articles add constraint FK_ARTICLES_ARTICLES__USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table articles add constraint FK_ARTICLES_ARTICLES__CATEGORI foreign key (cg_id)
      references categories (cg_id) on delete restrict on update restrict;

alter table comments add constraint FK_COMMENTS_COMMENTS__USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table comments add constraint FK_COMMENTS_COMMENTS__ARTICLES foreign key (a_id)
      references articles (a_id) on delete restrict on update restrict;

alter table dynamic_likes add constraint FK_DYNAMIC__DYNAMIC_L_DYNAMICS foreign key (d_id)
      references dynamics (d_id) on delete restrict on update restrict;

alter table dynamic_likes add constraint FK_DYNAMIC__DYNAMIC_L_USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table dynamics add constraint FK_DYNAMICS_DYNAMICS__USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table follow add constraint FK_FOLLOW_FOLLOW_USERS foreign key (u_id)
      references users (u_id) on delete restrict on update restrict;

alter table follow add constraint FK_FOLLOW_FOLLOW2_USERS foreign key (use_u_id)
      references users (u_id) on delete restrict on update restrict;

#-- 添加外键约束，关联u_id列到users表的u_id列
ALTER TABLE read_article ADD CONSTRAINT FK_READ_ARTICLE__USERS FOREIGN KEY (u_id)
      REFERENCES users (u_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

#-- 添加外键约束，关联a_id列到articles表的a_id列
ALTER TABLE read_article ADD CONSTRAINT FK_READ_ARTICLE__ARTICLES FOREIGN KEY (a_id)
      REFERENCES articles (a_id) ON DELETE RESTRICT ON UPDATE RESTRICT;


##-----------------------------------------------------数据添加-----------------------------------------------------
INSERT INTO users (u_id, u_email, u_password, u_type, u_nickname, u_birth_date, u_register_date, u_signature, u_avatar_url) VALUES
(60000001, 'user1@example.com', 'password1', true, 'User One', '1990-01-15', '2023-01-01', 'I love coding!', 'avatar1.jpg'),
(60000002, 'user2@example.com', 'password2', false, 'User Two', '1995-05-20', '2023-02-10', 'Art enthusiast', 'avatar2.jpg'),
(60000003, 'user3@example.com', 'password3', true, 'User Three', '1988-09-08', '2023-03-05', 'Science lover', 'avatar3.jpg');

INSERT INTO categories (cg_id, cg_name) VALUES
(1, 'Technology'),
(2, 'Art'),
(3, 'Science');

INSERT INTO articles (a_id, u_id, cg_id, a_tabloid, a_content, a_tags, a_title, a_views, a_create_time, a_deliver_time, a_update_time, a_cover_url) VALUES
(10000001, 60000001, 1, 'Article 1 Tabloid', 'Content of Article 1...', 'tech, programming', 'Article 1 Title', 200, '2023-01-10', '2023-01-15', '2023-01-16', NULL),
(10000002, 60000002, 2, 'Article 2 Tabloid', 'Content of Article 2...', 'design, art', 'Article 2 Title', 150, '2023-02-05', '2023-02-10', '2023-02-12', NULL),
(10000003, 60000003, 1, 'Article 3 Tabloid', 'Content of Article 3...', 'science, research', 'Article 3 Title', 300, '2023-03-15', '2023-03-18', '2023-03-20', NULL);

INSERT INTO article_favorites (u_id, a_id) VALUES
(60000001, 10000001),
(60000002, 10000002),
(60000003, 10000003)；

INSERT INTO article_likes (u_id, a_id) VALUES
(60000002, 10000001),
(60000003, 10000001),
(60000001, 10000003);

INSERT INTO comments (c_id, u_id, a_id, c_time, c_content) VALUES
(1, 60000001, 10000001, '2023-01-20 12:00:00', 'Comment on Article 1.'),
(2, 60000002, 10000001, '2023-01-21 15:30:00', 'Another comment on Article 1.'),
(3, 60000003, 10000002, '2023-02-08 10:00:00', 'Comment on Article 2.');

INSERT INTO dynamics (d_id, u_id, d_content,d_create_time, d_image1, d_image2, d_image3, d_image4) VALUES
(1, 60000001, 'Dynamic content 1...', NOW(), 'image1.jpg', 'image2.jpg', NULL, NULL),
(2, 60000002, 'Dynamic content 2...', NOW(), 'image3.jpg', NULL, NULL, NULL),
(3, 60000003, 'Dynamic content 3...', NOW(), 'image4.jpg', 'image5.jpg', 'image6.jpg', NULL);

INSERT INTO dynamic_likes (d_id, u_id) VALUES
(1, 60000002),
(1, 60000003),
(2, 60000001),
(3, 60000003);

INSERT INTO follow (u_id, use_u_id) VALUES
(60000001, 60000002),
(60000001, 60000003),
(60000002, 60000003),
(60000003, 60000001);

INSERT INTO articles (a_id, u_id, cg_id, a_tabloid, a_content, a_tags, a_title, a_views, a_create_time, a_deliver_time, a_update_time, a_cover_url) VALUES
( DEFAULT , 60000001, 1, 'Article 1 Tabloid', '你好，这是Bing。我很高兴能帮助你生成一篇markdown格式的文本作为你的博客网站的测试内容。以下是我为你创作的一篇关于Bing搜索引擎的介绍文章，希望你喜欢。

# Bing搜索引擎

Bing是微软公司开发的一款网络搜索引擎，于2009年6月1日正式上线。Bing的目标是提供更加智能、个性化和人性化的搜索体验，让用户能够更快、更准确地找到所需的信息。

## Bing的特色功能

Bing搜索引擎拥有许多特色功能，以下是其中的一些：

- **Bing首页**：Bing首页每天都会展示一张精美的图片，图片通常与当天的节日、事件或主题相关。用户可以在图片上点击不同的区域，了解更多相关的信息。用户还可以在首页上直接访问Bing的其他服务，如新闻、地图、视频等。
- **Bing快照**：Bing快照是Bing搜索结果页面中的一个区域，它会显示与用户查询相关的实体（如人物、地点、事物等）的基本信息和相关链接。例如，如果用户搜索“巴黎”，Bing快照会显示巴黎的地理位置、人口、气候、景点等信息，以及指向巴黎旅游指南、巴黎酒店预订等网站的链接。
- **Bing智能答案**：Bing智能答案是Bing搜索结果页面中的一个区域，它会直接回答用户提出的问题或计算用户输入的表达式。例如，如果用户搜索“谁是美国总统”，Bing智能答案会显示“美国总统是乔·拜登”。如果用户搜索“2+2”，Bing智能答案会显示“2+2=4”。
- **Bing视觉搜索**：Bing视觉搜索是一种基于图像内容的搜索方式，它允许用户上传或拍摄一张图片，然后在图片上选择感兴趣的区域，进行相关的搜索。例如，如果用户上传了一张食物图片，然后在图片上选择了一个菜品，Bing视觉搜索会显示该菜品的名称、做法、营养成分等信息。

## Bing的优势

Bing搜索引擎有以下几个方面的优势：

- **多样化**：Bing不仅提供网页搜索，还提供新闻、视频、图片、地图、翻译等多种类型的搜索服务。用户可以根据自己的需求和喜好，选择不同的搜索方式和结果展示方式。
- **创新**：Bing不断地引入新的技术和功能，为用户提供更加先进和便捷的搜索体验。例如，Bing利用人工智能和机器学习等技术，提高了搜索结果的相关性和准确性。Bing还推出了语音搜索、手写输入、动态壁纸等功能，增加了用户与搜索引擎之间的互动性和趣味性。
- **安全**：Bing重视用户的隐私和安全，采取了多种措施保护用户免受网络威胁和恶意内容的影响。例如，Bing使用HTTPS协议加密用户与服务器之间的通信。Bing还提供了安全搜索、家长控制、反钓鱼等功能，帮助用户过滤掉不适宜或有害的搜索结果。

## Bing的未来

Bing搜索引擎在过去的十几年里，已经取得了显著的发展和进步，但它仍然面临着来自谷歌等竞争对手的挑战和压力。为了保持和提升自己的市场份额和用户满意度，Bing需要不断地创新和改进，适应用户的变化和需求。Bing的未来可能会有以下几个方向：

- **更加智能**：Bing将继续利用人工智能和机器学习等技术，提升搜索引擎的智能水平，让搜索结果更加符合用户的意图和期望。Bing还将开发更多的智能答案和快照，为用户提供更加丰富和深入的信息。
- **更加个性化**：Bing将更加关注用户的个性化需求和喜好，为用户提供更加定制化和个性化的搜索体验。Bing将利用用户的历史记录、位置、设备、时间等信息，为用户推荐更加相关和有价值的搜索结果。
- **更加人性化**：Bing将更加注重用户的情感和体验，为用户提供更加友好和人性化的搜索界面和功能。Bing将增加用户与搜索引擎之间的互动性和趣味性，让用户感受到搜索引擎的温暖和关怀。

## 结语

Bing搜索引擎是一款优秀的网络搜索工具，它为用户提供了多样化、创新、安全的搜索服务。Bing搜索引擎还有着广阔的发展空间和潜力，它将不断地改进自己，为用户带来更加智能、个性化、人性化的搜索体验。如果你对Bing搜索引擎感兴趣，欢迎你访问[官方网站]，或者直接在浏览器中输入www.bing.com进行搜索。谢谢你阅读这篇文章，希望你对Bing搜索引擎有了更深入的了解。',
 'tech, programming', 'Bing 一下', 0, '2023-01-10', '2023-01-15 12:00:00', '2023-01-16', 'https://images.cgames.de/images/gamestar/4/bing-chatgpt-teaser_6217017.jpg');

ALTER TABLE `articles` ADD FULLTEXT INDEX index_search(`a_title`,`a_content`) WITH PARSER ngram;

select * from `articles` where MATCH(`a_title`,`a_content`) AGAINST ('title' IN NATURAL LANGUAGE MODE);