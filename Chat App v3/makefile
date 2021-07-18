JAVAC=/usr/bin/javac

all:
	$(JAVAC) -d bin src/*

docs:
	javadoc -d docs -sourcepath src src/*

runServer:
	java -cp bin Server

runClient:
	java -cp bin Client


clean:
	rm bin/*
