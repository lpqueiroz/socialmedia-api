# socialmedia-api

This is a Social Media API which uses the following technologies: 

1. Scala
2. Play Framework
3. Slick
4. PostgreSQL
5. Cats
6. Auth0
7. JWT 
8. AWS-JAVA-SDK Library.

This api is deployed on Heroku: https://social-media-v1-api.herokuapp.com/

### Endpoints:

#### POST    /register-user           

**Description**: *user registration*

Example Request
```json
  "name": "Larissa",
  "email": "larissaqueiroz.p@gmail.com"
```

#### POST    /posts                      
**Description**: *save a post*

Example Request
```json
  "text": "randomText"
  "createdBy": 1
  "image": file.png
```

#### GET     /posts-user/:userId         
**Description**: *get posts by user*

#### GET     /posts                     
**Description**: *get all posts*

This endpoint is faking a jwt authentication. Use this token in the header to get the response: 
`Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkYyZGdFMzBRV09fdkUxNDF4cXctdyJ9.eyJpc3MiOiJodHRwczovL3NvY2lhbG1lZGlhLWFwaS51cy5hdXRoMC5jb20vIiwic3ViIjoiY0g1T1AxOVNLaDcxTzk4WDU4TElENXpKemJtWFZsRFlAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vc29jaWFsbWVkaWEtYXBpLnVzLmF1dGgwLmNvbS9hcGkvdjIvIiwiaWF0IjoxNjM0MDAwNDk5LCJleHAiOjE2MzQwODY4OTksImF6cCI6ImNINU9QMTlTS2g3MU85OFg1OExJRDV6SnpibVhWbERZIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.c2SG2OeP9pBQ1Jirvqp3fJccsyZsP5k92GvGVNvS3YkpiFKZEyUKxkV9DYKWS6wbNDSxIwWIJ69iGSf_b-rml6ciEAknVmws-hItKHpTtpO67SXoJshxhqKb3YimaV5pKjSkJO86NzAjAuqn36KTMig8fEsvgJsrqf-LopZE3RmnAQovF-bWPp41NGSnuk_MUJNtUnbTejpUIkZiq8bA9o68wjYVf89102ONms21SBZJhQQKC5kP-fPYb4bGx51ZHoAUa1qq8B2bXpyPOMBEklEw_AgLKObYZsR2e96wfL9eCfpeib8r8n9Bn9N6t33QfLjRb9Tq3X1uevvSJG6s-w`

#### GET     /posts-ascending-order      
**Description**: *get posts by ascending order*.

#### GET     /posts-descending-order     
**Description**: *get posts by descending order*.

#### PUT     /posts/update/:id           
**Description**: *update post*


Example Request
```json
  "text": "randomText"
  "createdBy": 1
```

#### POST    /comments                  
**Description**: *save a comment*

Example Request
```json
  "text": "randomText"
  "createdBy": 1,
  "postId": 1
```

#### GET     /comments-user/:userId      
**Description**: *get comments by user*.

#### GET     /comments                    
**Description**: *get all comments*

#### GET     /comments-ascending-order    
**Description**: *get comments in ascending order*

#### GET     /comments-descending-order   
**Description**: *get comments in descending order*

#### PUT     /comments/update/:id         
**Description**: *update comment*

Example Request
```json
  "text": "randomText"
  "createdBy": 1,
  "postId": 1
```

#### GET     /comments-post/:postId       
**Description**: *get comments by post*
