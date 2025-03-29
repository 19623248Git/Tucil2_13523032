## Compiling the program

### Ensure java versions (23.0.2 or above)

#### check java version: 
```cmd
java --version
```

#### expected output: 
```cmd
java 23.0.2 2025-01-21
Java(TM) SE Runtime Environment (build 23.0.2+7-58)
Java HotSpot(TM) 64-Bit Server VM (build 23.0.2+7-58, mixed mode, sharing)
```

#### check javac version: 
```cmd
javac --version
```

#### expected output: 
```cmd
javac 23.0.2
```

### Compile at root directory of project
```cmd
javac -d bin -cp src src/QuadTree/*.java src/Main.java
```

### Run the Program at root directory of project
```cmd
java -cp bin Main
```

## ImageProcessing class definition:

### Attributes:
- BufferedImage image
- int width
- int height
- String absPath

### Methods:
- loadImage(String absPath) -> loads image with absolute path parameter
- inputAbsPath() -> prompts user to input absolute path before loadImage(String absPath)