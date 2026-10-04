# In-Memory Workload Engine (DAA Assignment 2)

A custom implementation of basic data structures (`DynamicArray`, `MyLinkedList`, `MinHeap`) designed for in-memory workload processing, operation counting, and cache-locality benchmark evaluation[cite: 1, 2].

## Requirements
- Java 17 or higher
- Apache Maven 3.8+
- Python 3 with `pandas` and `matplotlib` (for plot generation)

## Project Structure
```text
├── pom.xml
├── README.md
├── REPORT.md
├── generate_plots.py
├── results/
│   ├── results.csv
│   └── plots/
│       ├── W1_time.png
│       └── ...
└── src/
    ├── main/java/engine/
    │   ├── DynamicArray.java
    │   ├── MyLinkedList.java
    │   ├── MinHeap.java
    │   ├── Metrics.java
    │   └── BenchmarkRunner.java
    └── test/java/engine/
        ├── DynamicArrayTest.java
        ├── MyLinkedListTest.java
        └── MinHeapTest.java
```

## How to Build
To compile the source code without running tests[cite: 4, 5]:
```bash
mvn clean compile
```

## How to Run Tests
Executes all JUnit 5 test suites covering correctness, edge cases, and the min-heap property[cite: 3, 4]:
```bash
mvn test
```

## How to Run Benchmark
Runs workloads W1–W4 for $n \in \{100, 1000, 10000, 100000\}$ with JVM warm-up and exports median measurements to `results/results.csv`[cite: 2, 3, 4]:
```bash
mvn exec:java
```

## How to Generate Plots
Generates execution time and physical operation comparison charts into `results/plots/`[cite: 3, 4]:
```bash
python generate_plots.py
```