# Issue Tracker
REST API for an issue tracker for a small team of developers.

## Running application
* Run docker compose build and docker compose up
* You can reach the app via -> [localhost:8080/](http://localhost:8080/)

## How to use the app?
* There are pre-populated data for the api. You can check it from [here](src/main/resources/data.sql)
* You can plan issues for the existing developers and issue by using getPlan endpoint from [here](http://localhost:8080/swagger-ui/index.html#/plan-controller/getPlan). **Please don't forget only issues have estimated status and story type will be included in the plan!**
* You can add, update, delete, get issues by using createIssue endpoint from [here](http://localhost:8080/swagger-ui/index.html#/issue-controller)
* You can add, update, delete, list developers by using createIssue endpoint from [here](http://localhost:8080/swagger-ui/index.html#/issue-controller)

## Technical choices made
* Implemented issues in one entity called as [Issue](src/main/java/com/kaganuk/issuetracker/model/Issue.java) to prevent duplicating common atrributes for bugs and stories, making the database design simple as possible. Downside of this design having null fields in the database so that can make the database size grow unnecessarily. But I ignored this since this is only will be used by small teams. Also created a simple entity for developers called as [Developer](src/main/java/com/kaganuk/issuetracker/model/Developer.java)
* I created [IssueDto](src/main/java/com/kaganuk/issuetracker/model/IssueDto.java) for handling patch,delete,get,create(response) bodies and [IssueCreateDto](src/main/java/com/kaganuk/issuetracker/model/IssueCreateDto.java) for handling create(request) body because of removing assigneeId from post requests.
* I written a validator called as [IssueValidator](src/main/java/com/kaganuk/issuetracker/validation/IssueValidator.java) for being able to restrict data can be save for different issue types. For example: A story cannot have a priority
* I created basic model, controller and services for planing, developers and issues.
* I written unit tests for all services and validator to be able to sure they are all working right.
* I created a [GlobalExceptionHandler](src/main/java/com/kaganuk/issuetracker/exception/GlobalExceptionHandler.java) for being able to control the api responses for different type of exceptions application creates.
* I used a first fit decreasing algorithm to create the least amount of weeks from given stories. Since we know estimations before calculation this was easiest to implement and faster to solve the problem. A short explanation of how algorithm works: <br />
![first-fit-decreasing-algorithm](https://github.com/kaganuk/issue-tracker/assets/12846311/ba0d6c9a-9cd5-4319-962c-fe7638dc415f)
