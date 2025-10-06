MAIN = report
BUILD = build
LATEX = pdflatex
RM = rm -rf

all: $(BUILD)/$(MAIN).pdf

$(BUILD)/$(MAIN).pdf: $(MAIN).tex | $(BUILD)
	$(LATEX) -output-directory=$(BUILD) $(MAIN).tex
	$(LATEX) -output-directory=$(BUILD) $(MAIN).tex

$(BUILD):
	mkdir -p $(BUILD)

clean:
	$(RM) $(BUILD)
