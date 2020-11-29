# NumSequence Generator

### APIs:
* /api/generate [POST] : Generate numbers
* /api/tasks/{uuid}/status [GET] : Get status of the num generator by uuid
* /api/tasks/{uuid}?action=get_numlist : Get num list by uuid

### Advanced API:
* /api/bulkGenerate [POST] : Generate bulk numbers

### Building Jar:
Git clone the project repo.
```
https://github.com/bipinbilas/TestProject.git
```
Go inside the project folder to build.
```
mvn clean install
```

### Running App:
```
java -jar target\TestProject-1.0.jar
```
