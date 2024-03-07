# Issue Tracker
REST API for an issue tracker for a small team of developers.

## Running application
* Run docker compose build and docker compose up
* You can reach the app via -> [localhost:8080/](http://localhost:8080/)

## How to use the app?
* There are pre-populated data for the api. You can check it from [here](src/main/resources/data.sql)
* You can plan issues for the existing developers and issue by using getPlan endpoint from [here](http://localhost:8080/swagger-ui/index.html#/plan-controller/getPlan). <br />
**Please don't forget only issues have estimated status and story type will be included in the plan!**
* You can add, update, delete and get issues by using createIssue endpoint from [here](http://localhost:8080/swagger-ui/index.html#/issue-controller)
* You can add, update, delete and list developers by using createIssue endpoint from [here](http://localhost:8080/swagger-ui/index.html#/issue-controller)

## Technical choices made
* Implemented issues in one entity called [Issue](src/main/java/com/kaganuk/issuetracker/model/Issue.java) to prevent duplicating common attributes for bugs and stories, making the database design as simple as possible. The downside of this design having null fields in the database so can make the database size grow unnecessarily. But I ignored this since this is only will be used by small teams. Also created a simple entity for developers called [Developer](src/main/java/com/kaganuk/issuetracker/model/Developer.java)
* I created [IssueDto](src/main/java/com/kaganuk/issuetracker/model/IssueDto.java) for handling patch,delete,get,create(response) bodies and [IssueCreateDto](src/main/java/com/kaganuk/issuetracker/model/IssueCreateDto.java) for handling create(request) body because of removing assigneeId from post requests.
* I have written a validator called [IssueValidator](src/main/java/com/kaganuk/issuetracker/validation/IssueValidator.java) for being able to restrict data can be saved for different issue types. For example: A story cannot have a priority
* I created basic models, controllers and services for planing, developers and issues.
* I have written unit tests for all services and validator to be able to sure they are all working right.
* I created a [GlobalExceptionHandler](src/main/java/com/kaganuk/issuetracker/exception/GlobalExceptionHandler.java) for being able to control the API responses for different type of exceptions application creates.
* I used a first fit decreasing algorithm to create the least amount of weeks from given stories. Since we know estimations before calculation this was easiest to implement and faster to solve the problem. A short explanation of how the algorithm works: <br />
![first-fit-decreasing-algorithm](https://github.com/kaganuk/issue-tracker/assets/12846311/ba0d6c9a-9cd5-4319-962c-fe7638dc415f)
* Auto-generated swagger docs: <br />
<img width="1503" alt="image" src="https://github.com/kaganuk/issue-tracker/assets/12846311/f230c507-37b7-451f-a208-56be63fdb4a5">
