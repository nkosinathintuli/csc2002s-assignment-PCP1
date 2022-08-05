# CSC2002S Assignment 1 Makefile
# Nkosinathi Ntuli
# 2022/08/05

JAVAC=/usr/bin/javac

.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= MeanFilterSerial.class MeanFilterParallel.class MedianFilterSerial.class MedianFilterParallel.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)
clean:
	rm $(BINDIR)/*.class

run: $(CLASS_FILES)
	java -cp $(BINDIR) MeanFilterSerial $(input)

docs:
	javadoc -d $(DOCDIR)/ $(SRCDIR)/*.java
