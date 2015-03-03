OBJ = obj/
SRC = src/
BIN = bin/
INC = -Iinclude
VPATH = src:include:bin
CC = javac
CFLAGS = -g 

ALL: Bio

Bio: $(SRC)/*.java
	$(CC) $(CFLAGS) $(SRC)*.java -d $(OBJ) 

launch:
	java $(OBJ)Main

clean:
	rm -r $(OBJ)/*.class

