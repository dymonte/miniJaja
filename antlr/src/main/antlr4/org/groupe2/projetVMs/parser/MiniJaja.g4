grammar MiniJaja;

@header {
  import org.antlr.v4.runtime.Token;
}

init: INT;

INT : [0-9]+;
