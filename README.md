# Best-Finder-Website RESTful API
 Best-Finder is website which helps you find out best movie,serial,game,anime,song or even joke when you have no idea what you want to watch,listen,play etc.
 ## Installation
 <strong>Running the BestFinder-server RESTful API requires:</strong>
 -  JDK 17.
 -  Apache Maven 4.0.0+
 -  Data management is operated by using a relational database management system - MySQL.
   ## 🛠 Built with:
 - Spring Boot
 - ReactJS
 - MySQL
 - JavaScript
 - HTML, CSS - Without using bootstrap or other sites for helping me visualize the site.
 - Apache Maven
 - JWT token
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
- JWT token credentials should be changed from ```application.yaml```:
  
```yaml
jwt.token: ${TOKEN}
```

- ADMIN credentials should be changed from ```application.yaml```:
  
```yaml
best-finder.admin: ${ADMIN_EMAIL}
best-finder.defaultpass: ${ADMIN_PASS}
```
 ## View - Home Page
<img src="https://github.com/Georgi133/Best-Finder-Website/assets/117848275/b87f7b23-fd8c-4400-ad30-eedacd2b9a19" />
  
   


   
   
 
 
