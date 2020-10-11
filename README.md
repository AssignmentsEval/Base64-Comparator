# Base64 String Comparison

## Requirement
1. Provide 2 http endpoints that accepts JSON base64 encoded binary data on both endpoints
   + \<host\>/v1/diff/\<ID\>/left and \<host\>/v1/diff/\<ID\>/right
2. The provided data needs to be diff-ed and the results shall be available on a third end
point
   + \<host\>/v1/diff/\<ID\>
3. The results shall provide the following info in JSON format
    + If equal return that
    + If not of equal size just return that
    + If of same size provide insight in where the diffs are, actual diffs are not needed.
        - So mainly offsets + length in the data
    + Make assumptions in the implementation explicit, choices are good but need to be
      communicated

## How to build, run and test
```
# build
cd <base64comp_dir>
mvn package
    
# run
cd target
java -jar base64comp-0.0.1.jar

# test

# test left endpoint
curl -X POST http://localhost:8080/v1/diff/<ID>/left -H "Content-Type:text/plain" --data <base64_string>

# test right endpoint
curl -X POST http://localhost:8080/v1/diff/<ID>/right -H"Content-Type:text/plain" --data <base64_string>
   
# test third endpoint
curl -X GET http://localhost:8080/v1/diff/<ID>

```

## What is the response

#### Equal case
```
# left endpoint request
curl -X POST http://localhost:8080/v1/diff/1/left -H "Content-Type:text/plain" --data "YWJjZGVmZw=="

# right endpoint request
curl -X POST http://localhost:8080/v1/diff/1/right -H "Content-Type:text/plain" --data "YWJjZGVmZw=="

# third endpoint request
curl -X GET http://localhost:8080/v1/diff/1

# third endpoint reponse
{
    "status": "EQUAL"
}
```

#### Different case
```
# left endpoint request
curl -X POST http://localhost:8080/v1/diff/2/left -H "Content-Type:text/plain" --data "YWJjZGVmZw=="

# right endpoint request
curl -X POST http://localhost:8080/v1/diff/2/right -H "Content-Type:text/plain" --data "YWJkY2VoZw=="

# third endpoint request
curl -X GET http://localhost:8080/v1/diff/2

# third endpoint reponse
{
    "status": "DIFFERENT",
    "diffList": [
        {
            "offset": 2,
            "length": 2
        },
        {
            "offset": 5,
            "length": 1
        }
    ],
}
```

#### Different size case
```
# left endpoint request
curl -X POST http://localhost:8080/v1/diff/3/left -H "Content-Type:text/plain" --data "YWJjZGVmZw=="

# right endpoint request
curl -X POST http://localhost:8080/v1/diff/3/right -H "Content-Type:text/plain" --data "YWJjZGVmZ2hpag=="

# third endpoint request
curl -X GET http://localhost:8080/v1/diff/3

# third endpoint reponse
{
    "status": "DIFFERENT_SIZE"
}
```

#### ID does not exist
```
# third endpoint request
curl -X GET http://localhost:8080/v1/diff/4

# third endpoint reponse
{
    "status": "ERROR",
    "message": "ID does not exist"
}
```

#### Data can not decode
```
# left endpoint request
curl -X POST http://localhost:8080/v1/diff/5/left -H "Content-Type:text/plain" --data "YWJjZGVmZw=="

# right endpoint request
curl -X POST http://localhost:8080/v1/diff/5/right -H "Content-Type:text/plain" --data "YWJjZGVmAAAA/ZZw=="

# third endpoint request
curl -X GET http://localhost:8080/v1/diff/5

# third endpoint reponse

{
    "status": "ERROR",
    "message": "data can not decode to compare"
}
```

## Improvement
1. Check data whether cen decode at post request.
2. Customize exception class for error handling. Ex: id does not exist or data cannot decode.
3. Provide data overwrite alert. Ex: post request with the same id.
4. ID does not need to be an integer.