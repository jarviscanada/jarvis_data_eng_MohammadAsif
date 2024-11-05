# Introduction
Discuss the design of each app. What does the app do? What technologies have you used? (e.g. core java, libraries, lambda, IDE, docker, etc..)
This Java application is a implementation of the [grep] command used in Linux systems utilized to search for a pattern in a group of files. The application takes three arguments:
`regex` a regex pattern to search for within the file
`rootDir` the root directory to direct the search for files containing the pattern
`outFile` the file to output the results of the grep command

# Quick Start
```bash
#Compile the mvn project and package the the program into a JAR file
mvn clean compile package

#Run the package program with three arguments [regex], [rootDir], [outFile]
java -cp target/grep-1.0-SNAPSHOT.jar [regex], [rootDir], [outFile]
#Example: java -cp target/grep-1.0-SNAPSHOT.jar .*Romeo.*Juliet.* ./data ./out/grep.txt

#View the output of the grep application from the arguments passed
cat [outFile]
```

#Implemenation
## Pseudocode
This application's process method highlights a high level structure for the entire program.
```java
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
The base implementation of the program stored files and matched lines in an ArrayList between operations which ultimately consumed more memory than desired. To optimize the memory usage in the program Stream were implemented. 

# Test
Testing for the application was completed via Java Unit Testing framework. The methods of the program were tested in an isolated fashion to ensure that all components work as intended.

# Deployment
```bash
docker pull mohammad0336/grep
docker run --rm \ -v `pwd`/data:/data -v `pwd`/log:/log \ mohammad0336/grep .*Romeo.*Juliet.* /data /log/grep.out
```

# Improvement
1. At the end of the program the user can see the nummber of matched lines as well as the first x lines as a preview.
2. Display metrics for memory usage, query timing and program's running time.
3. Display the results of the output on request of the end user.
