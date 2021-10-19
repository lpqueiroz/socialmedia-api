# socialmedia-api

This is a Social Media API which uses the following technologies: 

1. Scala
2. Play Framework
3. Slick
4. PostgreSQL
5. Cats
6. Auth0/JWT
7. AWS-JAVA-SDK Library.

This api is deployed on Heroku: https://social-media-v1-api.herokuapp.com/

Run the tests in the terminal with the following command: `sbt test`

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

This endpoint is using a jwt authentication. Use this token in the header to get the response: 
`Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImcxbXJ4Rm5yMmMwQm1VOElOUDNDeiJ9.eyJpc3MiOiJodHRwczovL2Rldi1haGV0Nzd5eS51cy5hdXRoMC5jb20vIiwic3ViIjoiV2M1cWRHSTdFODY0QzRDdzRubk90djNYRHEybUlkNHNAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vZGV2LWFoZXQ3N3l5LnVzLmF1dGgwLmNvbS9hcGkvdjIvIiwiaWF0IjoxNjM0NjU0ODY3LCJleHAiOjE2MzUyNTk2NjcsImF6cCI6IldjNXFkR0k3RTg2NEM0Q3c0bm5PdHYzWERxMm1JZDRzIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.TZUlfSTWaIl7xCy1wzO3LxGgQ-B5dcsaHhs1894CWcEhD2xNZ8NQegyj2h-UoJN19gMPgnfDZG-SerohYwriEX3R_tZgn7R13NKpxzFL95oka8G_2GiEc2ouW8ZMgXP9YNJ64SDW3exg7c6ciPFVR9g1jk9QaDEgbULq1mbV0PaHTtKl-7yQ6s1iJKmdTJBqMFmlOxQMKbFOouSB_mUti5VKgqeyFtWwu-NP12TvniwnTRNeMkeVQXfMnIMGjDrW4E_CloelbmFOgLXrIAxTwKVkg85wjt037NomFgnKrRUQPXuHOKhGymxHYBH4-VFMj-6VEXwWVrX9dMQWFrBQkQ`

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
