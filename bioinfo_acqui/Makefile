OBJ = obj/
SRC = src/
BIN = bin/
INC = -Iinclude
VPATH = src:include:bin
CC = javac
CFLAGS = -g 
LIBS = lib/log4j-1.2.17.jar
ALL: Bio

Bio: $(SRC)/*.java
	$(CC) $(CFLAGS) -cp $(LIBS)  $(SRC)*.java -d $(OBJ) 

launch:
	java -cp ".:obj:lib/*" Main

clean:
	rm -r $(OBJ)/*.class

