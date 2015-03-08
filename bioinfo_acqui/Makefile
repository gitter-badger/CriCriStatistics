OBJ = obj/
SRC = src/
BIN = bin/
INC = -Iinclude
VPATH = src:include:bin
CC = javac
CFLAGS = -g 
LIBS = lib/jxl.jar:lib/log4j-1.2.17.jar:lib/sqlite4java-392/sqlite4java.jar

ALL: Bio

Bio: $(SRC)*.java | obj
	$(CC) $(CFLAGS) -cp $(LIBS)  $(SRC)*.java -d $(OBJ) 

obj:
	mkdir obj

launch:
	java -cp ".:obj:lib/*:lib/sqlite4java-392/*" Main

debug:
	jdb -classpath ".:obj:lib/*:lib/sqlite4java-392/*" Main

clean:
	rm -r $(OBJ)/*.class genome_overview.txt bioinfo.log* 2>/dev/null     

dropdb:
	rm database.sqlite
