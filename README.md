# Best-Finder-Website RESTful API
 Best-Finder is website which helps you find out best movie, serial, game, anime, song or even joke when you have no idea what you want to watch, listen, play etc.                                             
 
   # :notebook_with_decorative_cover: Table of Contents 
  * [Built with](#built-with)
  * [Features](#features)
  * [What is in Backend](#what-is-in-backend)
  * [Installation](#installation)
  * [Configuration](#configuration)
  * [View](#view)

<hr /> 

  ## Built with
 - Java 17
 - Spring Boot
 - Spring Security
 - Spring Data JPA
 - ReactJS
 - MySQL
 - JavaScript
 - HTML, CSS - Without using bootstrap or other sites for helping me visualize the site.
 - Apache Maven
 - JWT token

 ## Features
 - Multi-language support. Stores jwt token in local storage. Could be runned from any device - responsive.
 - Users - Can see all the sections, can like ,comment , sort by year descending if the section he is support years,
  can sort by likes descending, search bar combined with sorting operations. Users could change their given 
 information and change password.Could edit or delete only its own comments and unlike if torrent is liked earlier.
 - Admin - Can edit and delete all the comments no matter who wrote them, can add torrents, ban users, change user 
 roles. Can search by email to see user information.

 ## What is in Backend
 - Filter - to check for valid jwt token
 - Interseptor - to check for blacklisted (banned) users everytime when request to server is made
 - Event and Listener - when user click on forgotten password and send the request for new password event is activated and the listener comes after
 - Schedule - checking every hour if 24 hours from ban of the user is expired and if it's remove the restriction of the user
 - Custom exceptions
 - Exception handling
 - JavaMailSender, Cloudinary
 - AOP
 - JUnit, Mockito - Unit and Integration tests
 ## Installation
 <strong>Running the BestFinder-server RESTful API requires:</strong>
 -  JDK 17.
 -  Apache Maven 4.0.0+
 -  Data management is operated by using a relational database management system - MySQL.
 ## Configuration
  - Datasource location, port and name should be changed from ```application.yaml```:
```yaml 
spring.datasource.url: jdbc:mysql://localhost:${PORT}/${DATABASE_NAME}?allowPublicKeyRetrieval=true
```
- MySQL server credentials should be changed from ```application.yaml```:

```yaml 
spring.datasource.username: ${DB_USERNAME}
spring.datasource.password: ${DB_PASSWORD}
```
- Mail credentials should be changed from ```application.yaml```:
```yaml
mail.host: ${MAIL_SERVER}
mail.username: ${MAIL_USERNAME}
mail.password: ${MAIL_PASSWORD}
```

- Cloudinary credentials should be changed from ```application.yaml```:
```yaml
cloudinary.cloud-name: ${CLOUD_NAME}
cloudinary.api-key: ${CLOUD_API_KEY}
cloudinary.api-secret: ${CLOUD_API_SECRET}
```
- JWT token - SECRET KEY (must be minimum 256-bit) should be changed from ```application.yaml```:
  
```yaml
jwt.token: ${TOKEN}
```

- ADMIN credentials should be changed from ```application.yaml```:
  
```yaml
best-finder.admin: ${ADMIN_EMAIL}
best-finder.defaultpass: ${ADMIN_PASS}
```
## View 
 <strong>Home Page, Movies Page, and Movie Details </strong>
 
<img src="https://github.com/Georgi133/Best-Finder-Website/assets/117848275/b87f7b23-fd8c-4400-ad30-eedacd2b9a19" />
<img src="https://github.com/Georgi133/Best-Finder-Website/assets/117848275/51887cc6-10b9-4555-a062-2f88e93f4cb2" />
<img src="https://github.com/Georgi133/Best-Finder-Website/assets/117848275/2391c93f-db73-4a0d-92be-9d98e08d66fb" />
<img src="https://github.com/Georgi133/Best-Finder-Website/assets/117848275/fb1eb17e-2199-4af2-8bf7-9659c661f46d" />
  
   


   
   
 
 
