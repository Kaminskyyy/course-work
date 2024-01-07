# Course Work. Inverted Index

## Installation and run

Install [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

Clone the repository:

```bash
  git clone https://github.com/Kaminskyyy/course-work.git
```

Navigate to the 'java' folder:

```bash
  cd course-work/src/main/java
```

Run java compilation:

```bash
  javac -d {build-folder} *.java
```

Navigate to the build folder and run project:

```bash
  java Main 
```
## Arguments

| argument  | Description                |
| :-------- | :------------------------- |
| `--threads=`        | Number of threads to create an index |
| `--root=`           | Root folder to create index |
| `--threadPoolSize=` | Thread pool size for socket server |
|`--queueSize=`       | Thread pool queue size |
