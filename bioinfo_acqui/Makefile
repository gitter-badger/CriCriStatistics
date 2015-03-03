OBJ = obj/
SRC = src/
BIN = bin/
INC = -Iinclude
VPATH = src:include:bin
CC = javac
CFLAGS = -g 
LIBS = lib/jxl.jar:lib/log4j-1.2.17.jar

ALL: Bio

Bio: $(SRC)*.java | obj
	$(CC) $(CFLAGS) -cp $(LIBS)  $(SRC)*.java -d $(OBJ) 

obj:
	mkdir obj

launch:
	java -cp ".:obj:lib/*" Main

clean:
	rm -r $(OBJ)/*.class

