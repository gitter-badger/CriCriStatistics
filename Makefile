OBJ = obj/
SRC = src/
BIN = bin/
INC = -Iinclude
VPATH = src:include:bin
CC = javac
CFLAGS = -g 
LIBS = lib/*:

ALL: Bio

Bio: $(SRC)*.java | obj
	$(CC) $(CFLAGS) -cp $(LIBS)  $(SRC)*.java -d $(OBJ) 

obj:
	mkdir obj

launch:
	@java -cp ".:obj:lib/*" Main

debug:
	@jdb -classpath ".:obj:lib/*" Main

clean:
	@rm -r $(OBJ)/*.class genome_overview.txt bioinfo.log* 2>/dev/null     

dropdb:
	@rm database.sqlite

eclipse:
	@rm -rf Eclipse
	@mkdir -p Eclipse/genbank/bin
	@cp -r lib src Eclipse/genbank
	@cp obj/log4j.properties Eclipse/genbank/bin

jar: Bio
	@rm -rf jar
	@mkdir jar
	@cp obj/* jar
	@cp -r lib jar
	@cp MANIFEST.MF jar
	@(cd jar; jar cvmf MANIFEST.MF genbank.jar *.class log4j.properties)
	@cp jar/genbank.jar .
