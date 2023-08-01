# SIT Schedule Generator

---
Tool to download and export SIT timetable from IN4SIT into Excel format for quick access and easy reference.

## How to use

---
1. Extract the zip file.
2. Run sitschedule.exe.
3. Fill in your username (school email), password, first and last day of the trimester in the DDMMYYYY format.
4. Click generate.
5. The timetable will be exported and saved in the same folder as the executable.

## Build it yourself

---
Don't trust the executable? Read the code and build from source yourself.

Instructions for Windows. Tested on Java version 17.0.1.

1. Navigate to the root of the project folder.

2. Compile the code with dependencies.
```
javac -d output -cp lib\* src\sitschedule\*.java
```
3. Copy the manifest file over to the output folder.
```
copy src\META-INF\MANIFEST.MF output\
```
4. Copy the icon into the output package.
```
copy src\sitschedule\icon.png output\sitschedule\
```
5. Navigate to the output directory.
```
cd output
```
6. Create jar file.
```
jar cvfm manifest.mf sitschedule.jar sitschedule
```
7. Run the jar file (double click or through command line).
```
java -jar sitschedule.jar
```

